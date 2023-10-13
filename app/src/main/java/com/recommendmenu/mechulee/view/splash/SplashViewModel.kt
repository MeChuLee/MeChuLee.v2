package com.recommendmenu.mechulee.view.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orhanobut.logger.Logger
import com.recommendmenu.mechulee.utils.NetworkUtils

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

    private fun requestAllIngredient() {
        NetworkUtils.requestAllIngredient(onResult = { isSuccess ->
            if (isSuccess) {
                completeGetIngredient = true
                isAllTrueCheck()
            } else {
                Logger.e("erooootrrrrrrrrrrrrr")
                //allComplete.value = false
                allComplete.postValue(false)
            }
        })
    }

    private fun requestAllMenu() {
        NetworkUtils.requestAllMenu(onResult = { isSuccess ->
            if (isSuccess) {
                completeGetMenu = true
                isAllTrueCheck()
            } else {
                Logger.e("erooootrrrrrrrrrrrrr")
                //allComplete.value = false
                allComplete.postValue(false)
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
                Logger.e("erooootrrrrrrrrrrrrr")
                //allComplete.value = false
                allComplete.postValue(false)
            }
        })
    }

    // 현재 주소 기반으로 날씨 정보 조회
    fun requestWeatherInfo(adminArea: String) {
        NetworkUtils.requestWeatherInfo(adminArea,onResult = { isSuccess ->
            if (isSuccess) {
                completeGetWeather = true
                isAllTrueCheck()
            } else {
                Logger.e("erooootrrrrrrrrrrrrr")
                //allComplete.value = false
                allComplete.postValue(false)
            }
        })
    }

    // 모든 요청 완료 확인
    private fun isAllTrueCheck() {
        if (completeGetIngredient && completeGetMenu && completeGetNearByRestaurant && completeGetTodayMenuList && completeGetWeather) {
            //allComplete.value = true
            allComplete.postValue(true)
        }
    }
}