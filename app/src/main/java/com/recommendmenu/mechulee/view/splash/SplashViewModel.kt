package com.recommendmenu.mechulee.view.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orhanobut.logger.Logger
import com.recommendmenu.mechulee.utils.NetworkUtils

class SplashViewModel : ViewModel() {
    companion object {
        const val NULL_POINT = 0
        const val SUCCESS_POINT = 1
        const val FAIL_POINT = 2
    }

    val allComplete = MutableLiveData<Boolean>()
    var checkArray = arrayOf(NULL_POINT, NULL_POINT, NULL_POINT, NULL_POINT)

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
                checkArray[0] = SUCCESS_POINT
                completeGetIngredient = true
                isAllTrueCheck()
            } else {
                checkArray[0] = FAIL_POINT
                arrayCheck()
            }
        })
    }

    private fun requestAllMenu() {
        NetworkUtils.requestAllMenu(onResult = { isSuccess ->
            if (isSuccess) {
                checkArray[1] = SUCCESS_POINT
                completeGetMenu = true
                isAllTrueCheck()
            } else {
                checkArray[1] = FAIL_POINT
                arrayCheck()
            }
        })
    }

    // 현재 주소 기반으로 주변 식당 검색 (NetWorkUtils 쪽에 주변 식당 리스트 저장)
    fun searchNearByRestaurant(address: String) {
        // 주변 식당 검색은 꼭 요청이 성공할 필요는 없으므로 isSuccess 로 구별하지 않음
        NetworkUtils.searchNearByRestaurant(address, onResult = {
            completeGetNearByRestaurant = true
            isAllTrueCheck()
        })
    }

    private fun requestTodayMenu() {
        NetworkUtils.readTodayMenuListWithRetrofit(onResult = { isSuccess ->
            if (isSuccess) {
                checkArray[2] = SUCCESS_POINT
                completeGetTodayMenuList = true
                isAllTrueCheck()
            } else {
                checkArray[2] = FAIL_POINT
                arrayCheck()
            }
        })
    }

    // 현재 주소 기반으로 날씨 정보 조회
    fun requestWeatherInfo(adminArea: String) {
        NetworkUtils.requestWeatherInfo(adminArea, onResult = { isSuccess ->
            if (isSuccess) {
                checkArray[3] = SUCCESS_POINT
                completeGetWeather = true
                isAllTrueCheck()
            } else {
                checkArray[3] = FAIL_POINT
                arrayCheck()
            }
        })
    }

    private fun arrayCheck() {

        Logger.d("checkArray $checkArray")

        if (NULL_POINT !in checkArray) {
            Logger.e("들어온다들어녿다옫나")
            if (FAIL_POINT in checkArray) {
                allComplete.postValue(false)
            }
        } else {
            Logger.e("여긴 들ㄹ어옴 ㅇㅇ")
        }
    }

    // 모든 요청 완료 확인
    private fun isAllTrueCheck() {
        if (completeGetIngredient && completeGetMenu && completeGetNearByRestaurant && completeGetTodayMenuList && completeGetWeather) {
            allComplete.postValue(true)
        }
    }
}