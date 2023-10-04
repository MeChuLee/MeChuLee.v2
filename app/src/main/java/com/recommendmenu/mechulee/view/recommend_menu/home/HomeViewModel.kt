package com.recommendmenu.mechulee.view.recommend_menu.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orhanobut.logger.Logger
import com.recommendmenu.mechulee.BuildConfig
import com.recommendmenu.mechulee.model.data.MenuInfo
import com.recommendmenu.mechulee.model.network.menu.MenuDto
import com.recommendmenu.mechulee.model.network.menu.MenuService
import com.recommendmenu.mechulee.model.network.search.Item
import com.recommendmenu.mechulee.model.network.search.SearchDto
import com.recommendmenu.mechulee.model.network.search.SearchService
import com.recommendmenu.mechulee.utils.NetworkUtils
import com.recommendmenu.mechulee.utils.RecommendUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    val currentAddress = MutableLiveData<String>()
    val restaurantList = MutableLiveData<ArrayList<Item>>()
    val todayMenuList = MutableLiveData<ArrayList<MenuInfo>>()
    val randomMenuResult = MutableLiveData<MenuInfo>()

    init {
        // 오늘의 추천 메뉴 조회
        todayMenuList.value = NetworkUtils.todayMenuList
    }

    // 현재 주소와 주변 식당 리스트 반영
    fun setCurrentAddress(simpleAddress: String) {
        currentAddress.value = simpleAddress
        restaurantList.value = NetworkUtils.restaurantList
    }

    // 메뉴 랜덤 추천
    fun requestRecommendRandomMenu() {
        randomMenuResult.value = RecommendUtils.recommendRandom()
    }
}