package vn.teko.cart.android

import android.content.Context
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.json.Json
import vn.teko.android.core.util.Result
import vn.teko.cart.android.busmodels.*
import vn.teko.cart.android.event.EventNames.AUTH_STATE_FLOW
import vn.teko.cart.android.event.EventNames.DISCOVERY_DETAIL_PRODUCTS
import vn.teko.cart.android.event.EventNames.GET_ACCESS_TOKEN
import vn.teko.cart.android.event.EventNames.USER_PROFILE_GET_DEFAULT_CUSTOMER
import vn.teko.cart.core.infrastructure.cart.user.User
import vn.teko.terra.core.android.bus.TerraBus
import vn.teko.terra.core.android.bus.event.EventOptions
import vn.teko.terra.core.android.bus.subscriber.FlowSubscriber
import vn.teko.terra.core.android.bus.subscriber.Subscriber
import vn.teko.terra.core.android.terra.TerraApp

class AppTestBus(private val context: Context, private val terraBus: TerraBus) {

    private var accessToken: String? = null

    fun setAccessToken(token: String?) {
        if (token.isNullOrEmpty()) {
            accessToken = null
            authFlow.value = AUTH_STATE_ERROR
        } else {
            accessToken = token
            authFlow.value = AUTH_STATE_LOGGED_IN
        }
    }

    private var subscribeAuthStateFlow = object: FlowSubscriber<Unit, String>() {
        override fun stream(event: Unit, options: EventOptions): Flow<String> {
            return authFlow
        }
    }

    private var subscriberGetDiscoveryProducts =
        object : Subscriber<GetDiscoveryProductsRequest, GetDiscoverProductsResponse>() {
            override suspend fun handle(
                event: GetDiscoveryProductsRequest,
                options: EventOptions
            ): Result<GetDiscoverProductsResponse, Throwable> {
                val skus = event.skus.split(",")
                val products = getProducts(skus.joinToString(",") { "\"$it\"" })
                return Result.success(
                    GetDiscoverProductsResponse(
                        products = products
                    )
                )
            }
        }

    private var subscribeGetDefaultCustomer = object : Subscriber<Unit, CustomerProfile>() {
        override suspend fun handle(
            event: Unit,
            options: EventOptions
        ): Result<CustomerProfile, Throwable> {
            return Result.success(customer("", false, true, false))
        }
    }

    private var subscribeGetAccessToken = object : Subscriber<Unit, User>() {
        override suspend fun handle(
            event: Unit,
            options: EventOptions
        ): Result<User, Throwable> {
            if (accessToken.orEmpty().length > 20) {
                return Result.success(User(accessToken))
            } else {
                return Result.failure(Error())
            }
        }
    }

    init {
        terraBus.subscribe(DISCOVERY_DETAIL_PRODUCTS, subscriberGetDiscoveryProducts)
        terraBus.subscribe(USER_PROFILE_GET_DEFAULT_CUSTOMER, subscribeGetDefaultCustomer)
        terraBus.subscribe(GET_ACCESS_TOKEN, subscribeGetAccessToken)
        terraBus.subscribe(AUTH_STATE_FLOW, subscribeAuthStateFlow)
    }

    companion object {
        private val appWithBusMap: MutableMap<String, AppTestBus> = mutableMapOf()

        private const val AUTH_STATE_ERROR = "error"
        private const val AUTH_STATE_LOGGED_IN = "logged_in"
        private const val AUTH_STATE_LOGGED_OUT = "logged_out"

        private val authFlow = MutableStateFlow(AUTH_STATE_LOGGED_OUT)


        @JvmStatic
        fun getInstance(context: Context, terraApp: TerraApp): AppTestBus {
            val appName = terraApp.appName
            val appWithBus = appWithBusMap[appName]
            if (appWithBus == null) {
                appWithBusMap[appName] = AppTestBus(context, terraApp.getTerraBus())
            }
            return appWithBusMap[appName]!!
        }

        suspend fun getProducts(skus: String = ""): List<DiscoveryProduct> {
            return HttpClient {
                Json {
                    serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
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

            }.post<GetDiscoverProductsApiResponse>("https://discovery.stag.tekoapis.net/api/v1/search") {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                body = Json.parseToJsonElement(
                    """
                            {
                              "pagination": {
                                "pageNumber": 1,
                                "itemsPerPage": 200
                              },
                            	"terminalCode": "${LocalData.terminal}",
                            	"filter": {
                                    "skus":[$skus]
                            	}
                            }
                        """.trimIndent()
                )
            }.result.products
        }
    }
}