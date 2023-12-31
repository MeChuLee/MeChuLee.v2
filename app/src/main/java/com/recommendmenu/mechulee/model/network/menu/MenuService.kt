package com.recommendmenu.mechulee.model.network.menu

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MenuService {
    @GET("/allmenu")
    fun getAllMenu(): Call<MenuDto>

    @GET("/recommend/today")
    fun getRecommendToday(): Call<MenuDto>

    @POST("/recommend/ai")
    fun getRecommendAi(
        @Body request: RecommendationRequest
    ): Call<MenuDto>
}

