package vn.teko.cart.core

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.http.*
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import vn.teko.cart.core.infrastructure.cart.CartApi
import vn.teko.cart.core.infrastructure.cart.token.TokenDataSource
import kotlin.test.AfterTest
import kotlin.test.BeforeTest


abstract class BaseTest : BasePlatformTest() {
    private suspend fun clearData() {
        val tokenDS: TokenDataSource by getCartManager().di.instance()
        tokenDS.clearToken()
    }

    @AfterTest
    fun tearDown() = runTest {
        clearData()
    }

    @BeforeTest
    fun setup() = runTest {
        initCartManager(cartConfig {
            terminal = "vnshop_app"
            channel = "vnshop_online"
            tenant = "vnshop"
            baseUrl = "https://carts-beta.stag.tekoapis.net"
        })
    }

    fun setupMock(mapPathResponse: Map<String, Pair<HttpStatusCode, String>>) {
        getCartManager().di.addConfig {
            bind<HttpClientEngine>(overrides = true) with singleton {
                MockEngine.create {
                    addHandler { request ->
                        val responseHeaders =
                            headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
                        when {
                            mapPathResponse.contains(request.url.encodedPath) -> respond(
                                mapPathResponse[request.url.encodedPath]!!.second,
                                mapPathResponse[request.url.encodedPath]!!.first,
                                responseHeaders
                            )
                            else -> error("Unhandled ${request.url.encodedPath}")
                        }

                    }
                }
            }
            bind<HttpClient>(overrides = true) with singleton {
                HttpClient(instance()) {
                    expectSuccess = false
                    Json {
                        serializer =
                            io.ktor.client.features.json.serializer.KotlinxSerializer(kotlinx.serialization.json.Json {
                                isLenient = false
                                ignoreUnknownKeys = true
                                allowSpecialFloatingPointValues = true
                                useArrayPolymorphism = false
                            })
                    }
                    Logging {
                        logger = Logger.DEFAULT
                        level = LogLevel.ALL
                    }
                }
            }

            bind<CartApi>(overrides = true) with singleton { CartApi(instance()) }
        }
    }
}