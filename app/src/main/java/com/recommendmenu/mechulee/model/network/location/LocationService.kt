package com.recommendmenu.mechulee.model.network.location

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationService {
    @GET("/location")  // 서버의 URL 경로
    fun sendLocation(
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String
    ): Call<String>  // 서버로부터 받을 응답 데이터 유형 지정
}
