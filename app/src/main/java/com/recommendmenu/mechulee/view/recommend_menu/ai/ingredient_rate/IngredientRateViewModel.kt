package com.recommendmenu.mechulee.view.recommend_menu.ai.ingredient_rate

import androidx.datastore.core.DataStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recommendmenu.mechulee.BuildConfig.MY_SERVER_BASE_URL
import com.recommendmenu.mechulee.RatingData
import com.recommendmenu.mechulee.model.data.IngredientInfo
import com.recommendmenu.mechulee.model.data.MenuInfo
import com.recommendmenu.mechulee.model.network.menu.RecommendationRequest
import com.recommendmenu.mechulee.model.network.menu.MenuDto
import com.recommendmenu.mechulee.model.network.menu.MenuService
import com.recommendmenu.mechulee.utils.DataStoreUtils
import com.recommendmenu.mechulee.utils.LocationUtils
import com.recommendmenu.mechulee.utils.NetworkUtils
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IngredientRateViewModel(private val dataStore: DataStore<RatingData>) : ViewModel() {

    // 데이터의 변경을 관찰 가능한 형태로 만들어준다.
    val menuList: MutableLiveData<ArrayList<IngredientInfo>> = MutableLiveData()
    val resultMenu: MutableLiveData<MenuInfo> = MutableLiveData()

    // 앱 시작 로딩화면에서 미리 받아놓은 전체 재료리스트로 totalList초기화
    private var totalList = NetworkUtils.totalIngredientList

    // ViewModel이 생성될 때 데이터 초기화 작업 수행
    init {
        // DataStore에서 RatingData를 비동기적으로 가져와서 totalList 초기화
        initTotalListFromDataStore()
    }

    private fun initTotalListFromDataStore() {
        // DataStore에서 가져온 rating값들을 totalList의 각각의 요소에 적용한다.
        DataStoreUtils.initTotalListFromDataStore(viewModelScope, dataStore, onResult = {
            for (i in 0 until totalList.size) {
                val matchingRating = it.getOrElse(i) { 0.0f }

                totalList[i].rating = matchingRating
            }
            menuList.value = totalList
            }
        )
    }

    fun refreshMenuList() {
        menuList.value = menuList.value?.map { it.copy(rating = 0f) }?.let { ArrayList(it) }
    }

    fun areAllRatingsZero(): Boolean {
        // null 체크
        val currentMenuList = menuList.value ?: return false

        // any 함수를 사용하여 하나라도 0이 아닌 rating이 있으면 false 반환
        return currentMenuList.all { it.rating == 0f }
    }

    fun showMenuList(selectedItem: String, searchWord: String) {
        val spinnerList = ArrayList<IngredientInfo>()

        totalList.forEach {
            if ((selectedItem == "평가완료") && (it.rating > 0.0f) && (searchWord in it.title)) {
                spinnerList.add(it)
            } else if (selectedItem == "미완료" && it.rating == 0.0f && searchWord in it.title) {
                spinnerList.add(it)
            } else if (selectedItem == "모두" && it.rating >= 0.0f && searchWord in it.title) {
                spinnerList.add(it)
            }
        }

        // UI에 보여지는 menuList를 spinnerList 초기화함으로써
        // 화면에 검색한 요소들이 보여질 수 있게 한다.
        // 값만 바꿔도 menuList가 MutableLiveData이기 때문에 변경이 감지됨
        menuList.value = spinnerList
    }


    @OptIn(DelicateCoroutinesApi::class)
    fun storeRatingDataFromTotalList() {
        // totalList의 요소 중 menuList와 title이 같은 것들만 교체
        for (menuItem in totalList) {
            val matchingTotalItem = menuList.value?.find { it.title == menuItem.title }
            if (matchingTotalItem != null) {
                menuItem.rating = matchingTotalItem.rating
            }
        }

        val menuItems = totalList
        val ratingList = menuItems.map { it.rating }

        DataStoreUtils.updateDataStoreToRatingList(GlobalScope, dataStore, ratingList)
    }

    // refactoring 필요
    fun getResultMenuFromServer():Boolean {
        var isRight = false
        for (nowValue in totalList) {
            if (nowValue.rating >= 3.0f) {
                isRight = true
                break
            }
        }
        if (!isRight) {
            return false
        } else {
            val tempLocation = LocationUtils.simpleAddress.split(" ")
            val call = NetworkUtils.getRetrofitInstance(MY_SERVER_BASE_URL).create(
                MenuService::class.java
            ).getRecommendAi(
                RecommendationRequest(
                totalList,
                if (tempLocation.isNotEmpty()) {
                    tempLocation[0]
                } else {
                    "서울특별시"
                }
            )
            ) // 서버에 totalList와 지역 정보을 보낸다.

            call.enqueue(object : Callback<MenuDto> {  // MenuDto의 형태로 서버에서 메뉴 결과를 받아온다.
                override fun onResponse(call: Call<MenuDto>, response: Response<MenuDto>) {
                    if (response.isSuccessful.not()) {  // API 요청 실패 시
                        return
                    }

                    response.body()?.let { menuDto ->
                        resultMenu.value = menuDto.recommendAiResult
                    }
                }

                override fun onFailure(call: Call<MenuDto>, t: Throwable) {}
            })
            return true
        }
    }

    fun changeMenuListToTotalList() {
        for (menuItem in totalList) {
            val matchingTotalItem = menuList.value?.find { it.title == menuItem.title }
            if (matchingTotalItem != null) {
                menuItem.rating = matchingTotalItem.rating
            }
        }
    }

    fun changeItemFromItemList(item: IngredientInfo) {
        menuList.value?.let { menuItems ->
            for (ingredientInfo in menuItems) {
                if (ingredientInfo.title == item.title) {
                    ingredientInfo.rating = item.rating
                }
            }
        }
    }
}
