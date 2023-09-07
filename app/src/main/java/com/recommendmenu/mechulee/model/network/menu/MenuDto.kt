package com.recommendmenu.mechulee.model.network.menu

import com.google.gson.annotations.SerializedName
import com.recommendmenu.mechulee.model.data.MenuInfo

data class MenuDto(
    @SerializedName("menuList")
    val menuList: List<MenuInfo>
)
