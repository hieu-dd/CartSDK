package com.cafeinlove14h.cartwithcompose.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*

@Module
@InstallIn(SingletonComponent::class)
object ClientModule {
    @Provides
    fun provideClient(): HttpClient = HttpClient {
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
    }
}