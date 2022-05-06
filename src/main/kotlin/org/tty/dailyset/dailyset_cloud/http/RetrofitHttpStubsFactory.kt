package org.tty.dailyset.dailyset_cloud.http

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import retrofit2.Retrofit

@Component
class RetrofitHttpStubsFactory {
    @Autowired
    private lateinit var retrofit: Retrofit

    @Bean
    fun dailySetUnicApi(): DailySetUnicApi {
        return retrofit.create(DailySetUnicApi::class.java)
    }
}