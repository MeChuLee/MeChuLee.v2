package com.recommendmenu.mechulee.view.recommend_menu.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.recommendmenu.mechulee.model.data.MenuInfo
import com.recommendmenu.mechulee.model.network.search.Item
import com.recommendmenu.mechulee.utils.LocationUtils
import com.recommendmenu.mechulee.utils.NetworkUtils
import com.recommendmenu.mechulee.utils.RecommendUtils

class HomeViewModel : ViewModel() {

    val currentAddress = MutableLiveData<String>()
    val restaurantList = MutableLiveData<ArrayList<Item>>()
    val todayMenuList = MutableLiveData<ArrayList<MenuInfo>>()
    val randomMenuResult = MutableLiveData<MenuInfo>()

    init {
        todayMenuList.value = NetworkUtils.todayMenuList
        restaurantList.value = NetworkUtils.restaurantList
        currentAddress.value = LocationUtils.simpleAddress
    }

    // 메뉴 랜덤 추천
    fun requestRecommendRandomMenu() {
        randomMenuResult.value = RecommendUtils.recommendRandom()
    }
}