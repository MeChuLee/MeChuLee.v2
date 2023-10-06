package com.recommendmenu.mechulee.model.network.weather_info

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherInfoService {
    @GET("/weather")  // 서버의 URL 경로
    fun sendAdminArea(
        @Query("adminArea") adminArea: String
    ): Call<WeatherInfoDto>  // 서버로부터 받을 응답 데이터 유형 지정
}
