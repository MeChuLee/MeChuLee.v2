package com.recommendmenu.mechulee.view.recommend_menu.ai.ingredient_rate

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orhanobut.logger.Logger
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.RatingData
import com.recommendmenu.mechulee.model.data.IngredientInfo
import com.recommendmenu.mechulee.model.network.ingredient.IngredientDto
import com.recommendmenu.mechulee.model.network.ingredient.IngredientService
import com.recommendmenu.mechulee.utils.DataStoreUtils
import com.recommendmenu.mechulee.utils.NetworkUtils
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IngredientRateViewModel(private val dataStore: DataStore<RatingData>) : ViewModel() {

    // 데이터의 변경을 관찰 가능한 형태로 만들어준다.
    val menuList: MutableLiveData<ArrayList<IngredientInfo>> = MutableLiveData()

    // 두 개의 리스트를 나눠서 사용하는 이유는 개념적으로 UI에 표시되는 데이터와
    // 비즈니스 로직에 의해 가공되는 데이터를 분리하여 관리하기 위함
    private var totalList: ArrayList<IngredientInfo> = arrayListOf(
        IngredientInfo(R.raw.pork_icon, "돼지고기", 0.0f, ""),
        IngredientInfo(R.raw.beef_icon, "소고기", 0.0f, ""),
        IngredientInfo(R.raw.pork_icon, "양상추", 0.0f, ""),
        IngredientInfo(R.raw.beef_icon, "양갈비", 0.0f, ""),
        IngredientInfo(R.raw.pork_icon, "소시지", 0.0f, ""),
        IngredientInfo(R.raw.beef_icon, "삼겹살", 0.0f, ""),
        IngredientInfo(R.raw.pork_icon, "양배추", 0.0f, ""),
        IngredientInfo(R.raw.beef_icon, "오이", 0.0f, ""),
        IngredientInfo(R.raw.pork_icon, "떡", 0.0f, ""),
        IngredientInfo(R.raw.beef_icon, "오리고기", 0.0f, ""),
        IngredientInfo(R.raw.pork_icon, "골뱅이", 0.0f, ""),
        IngredientInfo(R.raw.beef_icon, "시금치", 0.0f, ""),
        IngredientInfo(R.raw.pork_icon, "카레가루", 0.0f, ""),
        IngredientInfo(R.raw.beef_icon, "오징어", 0.0f, ""),
        IngredientInfo(R.raw.pork_icon, "토마토", 0.0f, ""),
        IngredientInfo(R.raw.beef_icon, "라자냐", 0.0f, ""),
    )

    // ViewModel이 생성될 때 데이터 초기화 작업 수행
    init {

        // DataStore에서 RatingData를 비동기적으로 가져와서 totalList 초기화
        DataStoreUtils.initTotalListFromDataStore(viewModelScope, dataStore, onResult = {
            val updatedTotalList = ArrayList<IngredientInfo>()
            for (info in totalList) {
                val matchingRating = it.getOrElse(totalList.indexOf(info)) { 0.0f }
                updatedTotalList.add(info.copy(rating = matchingRating))
            }
            totalList = updatedTotalList
            getIngredients()
            // 초기화된 totalList를 menuList에 설정
            menuList.value = totalList
        })

    }

    // 쟤료 정보 요청 함수
    private fun getIngredients() {

        // URL 주소 문제 발생 서버가 잘못됐나?
        val call = NetworkUtils.getRetrofitInstance(NetworkUtils.MY_SERVER_BASE_URL).create(
            IngredientService::class.java).getAllIngredient()

        call.enqueue(object : Callback<IngredientDto> {
            override fun onResponse(call: Call<IngredientDto>, response: Response<IngredientDto>) {
                if (response.isSuccessful.not()) {  // API 요청 실패 시
                    Logger.e("not isSuccessful")
                    return
                }

                response.body()?.let { ingredientDto ->
                    totalList = ingredientDto.ingredientList.toCollection(ArrayList())

                    Logger.d(totalList)
                    Logger.d(totalList.size)
                }
            }

            override fun onFailure(call: Call<IngredientDto>, t: Throwable) {
                // 네트워크 오류 등의 실패 시 처리
                Logger.d("통신 실패${t.message.toString()}")
            }
        }
        )
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
        // 리스트에 있는 MenuInfo의 title에 seachWord가 포함돼있을 경우
        // searchList에 해당 MenuInfo를 추가

        menuList.value = spinnerList
        // UI에 보여지는 menuList를 spinnerList 초기화함으로써
        // 화면에 검색한 요소들이 보여질 수 있게 한다.
        // 값만 바꿔도 menuList가 MutableLiveData이기 때문에 변경이 감지됨
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

    // 나중에 지우면 됨
    fun showRatingDataStore() {
        viewModelScope.launch {
            // DataStore에 저장된 ratingList 값을 읽어와서 로그로 출력합니다.
            val storedRatingData = dataStore.data.firstOrNull()
            storedRatingData?.let {
                val storedRatingList = it.ratingList
                Log.d("DataStoreTest", "Stored Rating List: $storedRatingList")
            }
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



