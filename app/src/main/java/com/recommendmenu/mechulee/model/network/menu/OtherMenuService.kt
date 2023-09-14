package com.recommendmenu.mechulee.model.network.menu

import com.recommendmenu.mechulee.model.data.MenuInfo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface OtherMenuService {
    @POST("/recommend/similar")
    fun getOtherMenuList(@Body menu: MenuInfo): Call<MenuDto>
}