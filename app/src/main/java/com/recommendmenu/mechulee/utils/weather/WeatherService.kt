package com.recommendmenu.mechulee.utils.weather

import com.recommendmenu.mechulee.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//Retrofit을 사용하여 웹 서버와 통신할 때 사용하는 서비스 인터페이스
interface WeatherService {

    // getUltraSrtFcst : 초단기 예보 조회 + 인증키
    @GET("getUltraSrtFcst?serviceKey=${BuildConfig.weather_key}")
    fun getWeather(
        @Query("numOfRows") num_of_rows: Int,   // 한 페이지 경과 수
        @Query("pageNo") page_no: Int,          // 페이지 번호
        @Query("dataType") data_type: String,   // 응답 자료 형식
        @Query("base_date") base_date: String,  // 발표 일자
        @Query("base_time") base_time: String,  // 발표 시각
        @Query("nx") nx: Int,                // 예보지점 X 좌표
        @Query("ny") ny: Int                 // 예보지점 Y 좌표
    ): Call<WEATHER> // 여기서 Weather는 Dto에 있는 Weather를 나타냄.
}
