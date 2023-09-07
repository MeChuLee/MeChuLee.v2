package com.recommendmenu.mechulee.view.recommend_menu.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orhanobut.logger.Logger
import com.recommendmenu.mechulee.BuildConfig
import com.recommendmenu.mechulee.model.network.menu.MenuDto
import com.recommendmenu.mechulee.model.network.menu.MenuService
import com.recommendmenu.mechulee.model.network.search.SearchDto
import com.recommendmenu.mechulee.model.network.search.SearchService
import com.recommendmenu.mechulee.utils.network.NetworkUtils
import com.recommendmenu.mechulee.view.menu_list.MenuListViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeViewModel : ViewModel() {

    val currentAddress = MutableLiveData<String>()

    init {

        // retrofit instance 획득
        val retrofit = NetworkUtils.getRetrofitInstance(NetworkUtils.NAVER_SEARCH_BASE_URL)

        val service = retrofit.create(SearchService::class.java)

        // retrofit 실행
        service.getSearchRestaurant(
            BuildConfig.NAVER_CLIENT_ID,
            BuildConfig.NAVER_CLIENT_SECRET,
            "대전 궁동",
            "10",
            "1",
            "random"
        ).enqueue(object : Callback<SearchDto> {
            override fun onResponse(call: Call<SearchDto>, response: Response<SearchDto>) {
                if (response.isSuccessful.not()) {
                    Logger.e("not isSuccessful")
                    return
                }

                response.body()?.let { searchDto ->
                    TODO("추가")
                }
            }

            override fun onFailure(call: Call<SearchDto>, t: Throwable) {
                Logger.e(t.message.toString())
            }
        })
    }

    fun setCurrentAddress(address: String) {
        // Activity 에서 사용한 Coroutine 에서 CallBack 을 받았기 때문에 현재 Background Thread 상태
        // Background Thread 에서는 ViewModel 의 LiveData 값을 변경할 때, setValue() 대신 postValue() 사용
        currentAddress.postValue(address)
    }
}