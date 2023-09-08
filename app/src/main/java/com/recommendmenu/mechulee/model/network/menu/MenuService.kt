package com.recommendmenu.mechulee.model.network.menu

import retrofit2.Call
import retrofit2.http.GET

interface MenuService {
    @GET("/allmenu")
    fun getAllIngredient(): Call<MenuDto>
}