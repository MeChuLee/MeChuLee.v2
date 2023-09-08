package com.recommendmenu.mechulee.view.recommend_menu.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orhanobut.logger.Logger
import com.recommendmenu.mechulee.BuildConfig
import com.recommendmenu.mechulee.model.network.search.Item
import com.recommendmenu.mechulee.model.network.search.SearchDto
import com.recommendmenu.mechulee.model.network.search.SearchService
import com.recommendmenu.mechulee.utils.network.NetworkUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    val currentAddress = MutableLiveData<String>()
    val restaurantList = MutableLiveData<ArrayList<Item>>()

    // Activity 에서 사용한 Coroutine 에서 CallBack 을 받았기 때문에 현재 Background Thread 상태
    // Background Thread 에서는 ViewModel 의 LiveData 값을 변경할 때, setValue() 대신 postValue() 사용
    fun setCurrentAddress(address: String, shortAddress: String) {
        // retrofit 실행하여 주변 식당 검색
        startRetrofit(shortAddress)

        val resultAddress = address.split(" ").drop(1).joinToString(" ")
        currentAddress.postValue(resultAddress)
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
}