package com.recommendmenu.mechulee.model.network.search

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SearchService {
    @GET("v1/search/local")
    fun getSearchRestaurant(
        @Header("X-Naver-Client-Id") clientId: String,
        @Header("X-Naver-Client-Secret") clientSecret: String,
        @Query("query") query: String,
        @Query("display") display: String,
        @Query("start") start: String,
        @Query("sort") sort: String,
    ): Call<SearchDto>
}