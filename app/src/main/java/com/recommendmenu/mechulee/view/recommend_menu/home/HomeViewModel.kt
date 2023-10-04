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
    val isMapAndRestaurantReady = MutableLiveData<Boolean>()
    val todayMenuList = MutableLiveData<ArrayList<MenuInfo>>()
    val randomMenuResult = MutableLiveData<MenuInfo>()

    private var isMapReady = false
    private var isRestaurantReady = false

    init {
        readTodayMenuListWithRetrofit()
    }

    // Activity 에서 사용한 Coroutine 에서 CallBack 을 받았기 때문에 현재 Background Thread 상태
    // Background Thread 에서는 ViewModel 의 LiveData 값을 변경할 때, setValue() 대신 postValue() 사용
    fun setCurrentAddress(simpleAddress: String) {
        // retrofit 실행하여 주변 식당 검색
        startRetrofit(simpleAddress)
        currentAddress.value = simpleAddress
    }

    // 현재 주소 기반으로 주변 식당 검색
    private fun startRetrofit(address: String) {
        // retrofit instance 획득
        val retrofit = NetworkUtils.getRetrofitInstance(NetworkUtils.NAVER_SEARCH_BASE_URL)

        val service = retrofit.create(SearchService::class.java)

        // retrofit 실행
        service.getSearchRestaurant(
            BuildConfig.NAVER_CLIENT_ID,
            BuildConfig.NAVER_CLIENT_SECRET,
            "$address 맛집",
            "5",
            "1",
            "random"
        ).enqueue(object : Callback<SearchDto> {
            override fun onResponse(call: Call<SearchDto>, response: Response<SearchDto>) {
                if (response.isSuccessful.not()) {
                    Logger.e("not isSuccessful")
                    return
                }

                response.body()?.let { searchDto ->
                    restaurantList.value = searchDto.items.toCollection(ArrayList())
                }
            }

            override fun onFailure(call: Call<SearchDto>, t: Throwable) {
                Logger.e(t.message.toString())
            }
        })
    }

    // 지도 준비 완료 시 상태 표시
    fun mapReady() {
        isMapReady = true
        if (isMapAndRestaurantReady.value == null && isRestaurantReady) {
            isMapAndRestaurantReady.value = true
        }
    }

    // 식당 정보 준비 완료 시 상태 표시
    fun restaurantReady() {
        isRestaurantReady = true
        if (isMapAndRestaurantReady.value == null && isMapReady) {
            isMapAndRestaurantReady.value = true
        }
    }

    // 오늘의 추천 메뉴 조회
    private fun readTodayMenuListWithRetrofit() {
        val retrofit = NetworkUtils.getRetrofitInstance(NetworkUtils.MY_SERVER_BASE_URL)

        val service = retrofit.create(MenuService::class.java)

        service.getRecommendToday()
            .enqueue(object : Callback<MenuDto> {
                override fun onResponse(call: Call<MenuDto>, response: Response<MenuDto>) {
                    if (response.isSuccessful.not()) {
                        return
                    }
                    response.body()?.let { menuDto ->
                        todayMenuList.value = menuDto.menuList.toCollection(ArrayList())
                    }
                }

                override fun onFailure(call: Call<MenuDto>, t: Throwable) {}
            })
    }

    // 메뉴 랜덤 추천
    fun requestRecommendRandomMenu() {
        randomMenuResult.value = RecommendUtils.recommendRandom()
    }
}