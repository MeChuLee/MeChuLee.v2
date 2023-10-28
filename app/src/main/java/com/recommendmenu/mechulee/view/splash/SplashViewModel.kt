package com.recommendmenu.mechulee.view.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.recommendmenu.mechulee.utils.NetworkUtils

class SplashViewModel : ViewModel() {
    companion object {
        const val NOT_YET_POINT = 0
        const val SUCCESS_POINT = 1
        const val FAIL_POINT = 2
        const val IDX_CHECK_REQUEST_ALL_INGREDIENT = 0
        const val IDX_CHECK_REQUEST_ALL_MENU = 1
        const val IDX_CHECK_REQUEST_TODAY_MENU = 2
        const val IDX_CHECK_REQUEST_WEATHER_INFO = 3
        const val IDX_CHECK_REQUEST_NEAR_BY_RESTAURANT = 4
    }

    val allComplete = MutableLiveData<Boolean>()
    private var checkArray = arrayOf(NOT_YET_POINT, NOT_YET_POINT, NOT_YET_POINT, NOT_YET_POINT, NOT_YET_POINT)

    init {
        requestServer()
    }

    fun requestServer() {
        // 서버 통신 확인 Array 초기화
        setInitValueCheckArray()

        // 전체 재료, 전체 메뉴 조회
        requestAllIngredient()
        requestAllMenu()
        requestTodayMenu()
    }

    private fun requestAllIngredient() {
        NetworkUtils.requestAllIngredient(onResult = { isSuccess ->
            if (isSuccess) {
                checkArray[IDX_CHECK_REQUEST_ALL_INGREDIENT] = SUCCESS_POINT
            } else {
                checkArray[IDX_CHECK_REQUEST_ALL_INGREDIENT] = FAIL_POINT
            }
            isAllTrueCheck()
        })
    }

    private fun requestAllMenu() {
        NetworkUtils.requestAllMenu(onResult = { isSuccess ->
            if (isSuccess) {
                checkArray[IDX_CHECK_REQUEST_ALL_MENU] = SUCCESS_POINT
            } else {
                checkArray[IDX_CHECK_REQUEST_ALL_MENU] = FAIL_POINT
            }
            isAllTrueCheck()
        })
    }

    // 현재 주소 기반으로 주변 식당 검색 (NetWorkUtils 쪽에 주변 식당 리스트 저장)
    fun searchNearByRestaurant(address: String) {
        // 주변 식당 검색은 꼭 요청이 성공할 필요는 없으므로 isSuccess 로 구별하지 않음
        NetworkUtils.searchNearByRestaurant(address, onResult = {
            checkArray[IDX_CHECK_REQUEST_NEAR_BY_RESTAURANT] = SUCCESS_POINT
            isAllTrueCheck()
        })
    }

    private fun requestTodayMenu() {
        NetworkUtils.readTodayMenuListWithRetrofit(onResult = { isSuccess ->
            if (isSuccess) {
                checkArray[IDX_CHECK_REQUEST_TODAY_MENU] = SUCCESS_POINT
            } else {
                checkArray[IDX_CHECK_REQUEST_TODAY_MENU] = FAIL_POINT
            }
            isAllTrueCheck()
        })
    }

    // 현재 주소 기반으로 날씨 정보 조회
    fun requestWeatherInfo(adminArea: String) {
        NetworkUtils.requestWeatherInfo(adminArea, onResult = { isSuccess ->
            if (isSuccess) {
                checkArray[IDX_CHECK_REQUEST_WEATHER_INFO] = SUCCESS_POINT
            } else {
                checkArray[IDX_CHECK_REQUEST_WEATHER_INFO] = FAIL_POINT
            }
            isAllTrueCheck()
        })
    }

    // 모든 요청 완료 확인
    private fun isAllTrueCheck() {
        // 아직 통신 안된 것이 있다면 return
        if (NOT_YET_POINT in checkArray) {
            return
        }

        if (FAIL_POINT in checkArray) {
            allComplete.postValue(false)
        } else {
            allComplete.postValue(true)
        }
    }

    private fun setInitValueCheckArray() {
        checkArray.forEachIndexed { index, _ ->
            checkArray[index] = NOT_YET_POINT
        }
    }
}