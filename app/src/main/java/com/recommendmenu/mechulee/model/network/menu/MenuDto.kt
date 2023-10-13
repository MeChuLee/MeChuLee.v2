package com.recommendmenu.mechulee.model.network.menu

import com.google.gson.annotations.SerializedName
import com.recommendmenu.mechulee.model.data.MenuInfo

data class MenuDto(
    @SerializedName("menuList")
    val menuList: List<MenuInfo>,

//    @SerializedName("menuInfo")
//    val menuInfo: MenuInfo,

    // return jsonify({'recommendAiResult': ai_menu}) => 서버에서 반환하는 key값과
    // SerializedName을 일치시켜준다.
    @SerializedName("recommendAiResult")
    val recommendAiResult: MenuInfo
)
