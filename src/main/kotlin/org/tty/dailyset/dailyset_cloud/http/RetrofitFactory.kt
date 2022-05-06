package org.tty.dailyset.dailyset_cloud.http

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

@Component
class RetrofitFactory {
    @Value("\${dailyset.env.unic.address}")
    private lateinit var unicAddress: String

    @Suppress("OPT_IN_USAGE")
    @Bean
    fun retrofit(): Retrofit {
        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .client(okHttpClient())
            .baseUrl(unicAddress)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
    }

    private fun okHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .callTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .build()
    }
}