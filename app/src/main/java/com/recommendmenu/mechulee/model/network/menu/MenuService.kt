package com.recommendmenu.mechulee.model.network.menu

import com.recommendmenu.mechulee.model.data.MenuInfo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MenuService {
    @GET("/allmenu")
    fun getAllMenu(): Call<MenuDto>

    @GET("/recommend/today")
    fun getRecommendToday(): Call<MenuDto>

    @GET("/recommend/random")
    fun getRecommendRandom(): Call<MenuDto>

    @POST("/recommend/similar")
    fun getOtherMenuList(@Body menu: MenuInfo): Call<MenuDto>
}