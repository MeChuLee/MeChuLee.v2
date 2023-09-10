package com.recommendmenu.mechulee.utils.weather

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 초단기예보 기준 - 예보시점부터 6시간 이내에 예보, 매시 30분마다 발표
object WeatherUtils {
    private const val BASE_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/"

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Json데이터를 사용자가 정의한 Java 객채로 변환해주는 라이브러리
            .build()
    }

    fun getRetrofitService(): WeatherService {
        return  getRetrofit().create(WeatherService::class.java) //retrofit객체 만듦!
    }
}