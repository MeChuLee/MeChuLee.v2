package com.recommendmenu.mechulee.view.splash

import android.net.Network
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orhanobut.logger.Logger
import com.recommendmenu.mechulee.BuildConfig
import com.recommendmenu.mechulee.model.network.search.SearchDto
import com.recommendmenu.mechulee.model.network.search.SearchService
import com.recommendmenu.mechulee.utils.NetworkUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashViewModel : ViewModel() {

    val allComplete = MutableLiveData<Boolean>()

    private var completeGetIngredient = false
    private var completeGetMenu = false
    private var completeGetWeather = false
    private var completeGetNearByRestaurant = false
    private var completeGetTodayMenuList = false

    init {
        // 전체 재료, 전체 메뉴 조회
        requestAllIngredient()
        requestAllMenu()
        requestTodayMenu()
    }

    fun requestAllWeather(coroutineScope: SplashActivity) {
        NetworkUtils.sendLocationXYToServer(coroutineScope, onResult = { isSuccess ->
            if (isSuccess) {
                completeGetWeather = true

                // isAllTrueCheck()
            } else {
                allComplete.value = false
            }
        })
    }

    private fun requestAllIngredient() {
        NetworkUtils.requestAllIngredient(onResult = { isSuccess ->
            if (isSuccess) {
                completeGetIngredient = true
                isAllTrueCheck()
            } else {
                allComplete.value = false
            }
        })
    }

    private fun requestAllMenu() {
        NetworkUtils.requestAllMenu(onResult = { isSuccess ->
            if (isSuccess) {
                completeGetMenu = true
                isAllTrueCheck()
            } else {
                allComplete.value = false
            }
        })
    }

    // 현재 주소 기반으로 주변 식당 검색 (NetWorkUtils 쪽에 주변 식당 리스트 저장)
    fun searchNearByRestaurant(address: String) {
        // 주변 식당 검색은 꼭 요청이 성공할 필요는 없으므로 isSuccess 로 구별하지 않음
        NetworkUtils.searchNearByRestaurant(address, onResult = { isSuccess ->
            completeGetNearByRestaurant = true
            isAllTrueCheck()
        })
    }

    private fun requestTodayMenu() {
        NetworkUtils.readTodayMenuListWithRetrofit(onResult = { isSuccess ->
            if (isSuccess) {
                completeGetTodayMenuList = true
                isAllTrueCheck()
            } else {
                allComplete.value = false
            }
        })
    }

    // 모든 요청 완료 확인
    private fun isAllTrueCheck() {
        //@TODO 날씨 조회는 아직 미반영
        if (completeGetIngredient && completeGetMenu && completeGetNearByRestaurant && completeGetTodayMenuList) {
            allComplete.value = true
        }
    }
}