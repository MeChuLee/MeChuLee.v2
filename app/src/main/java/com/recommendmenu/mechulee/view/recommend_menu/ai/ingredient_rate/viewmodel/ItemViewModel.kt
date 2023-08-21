package com.recommendmenu.mechulee.view.recommend_menu.ai.ingredient_rate.viewmodel

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.RatingData
import com.recommendmenu.mechulee.model.data.IngredientInfo
import com.recommendmenu.mechulee.proto.RatingDataSerializer
import kotlinx.coroutines.launch

class ItemViewModel: ViewModel() {

    private var totalList: ArrayList<IngredientInfo> = arrayListOf(
       IngredientInfo(1, R.raw.pork_icon, "돼지고기", 0.0f, ""),
        IngredientInfo(2,R.raw.beef_icon, "소고기", 0.0f, ""),
        IngredientInfo(3,R.raw.pork_icon, "양상추", 0.0f, ""),
        IngredientInfo(4,R.raw.beef_icon, "양갈비", 0.0f, ""),
        IngredientInfo(5,R.raw.pork_icon, "소시지", 0.0f, ""),
        IngredientInfo(6,R.raw.beef_icon, "삼겹살", 0.0f, ""),
        IngredientInfo(7,R.raw.pork_icon, "양배추", 0.0f, ""),
        IngredientInfo(8,R.raw.beef_icon, "오이", 0.0f, ""),
        IngredientInfo(9,R.raw.pork_icon, "떡", 0.0f, ""),
        IngredientInfo(10,R.raw.beef_icon, "오리고기", 0.0f, ""),
        IngredientInfo(11,R.raw.pork_icon, "골뱅이", 0.0f, ""),
        IngredientInfo(12,R.raw.beef_icon, "시금치", 0.0f, ""),
        IngredientInfo(13,R.raw.pork_icon, "카레가루", 0.0f, ""),
        IngredientInfo(14,R.raw.beef_icon, "오징어", 0.0f, ""),
        IngredientInfo(15,R.raw.pork_icon, "토마토", 0.0f, ""),
        IngredientInfo(16,R.raw.beef_icon, "라자냐", 0.0f, ""),
   )

    val menuList: MutableLiveData<ArrayList<IngredientInfo>> = MutableLiveData()
    // 데이터의 변경을 관찰 가능한 형태로 만들어준다.

    init{
        // ViewModel이 생성될 때 데이터 초기화 작업 수행
        // 초기값이라고 보면 된다.

        menuList.value = totalList
        // 초기화하면서 menuList까지 동시에 totalList와 같도록
        // 초기화 시켜준다.
    }

    // totalList에서 searchword가 포함된 제목을 가진 메뉴를 menuList에 반영
    fun searchMenuList(searchWord: String) {
        val searchList = ArrayList<IngredientInfo>()

        totalList.forEach{
            if(searchWord in it.title) searchList.add(it)
        }
        // 리스트에 있는 MenuInfo의 title에 seachWord가 포함돼있을 경우
        // searchList에 해당 MenuInfo를 추가

        menuList.value = searchList
        // UI에 보여지는 menuList를 searchList로 초기화함으로써
        // 화면에 검색한 요소들이 보여질 수 있게 한다.
        // 값만 바꿔도 menuList가 MutableLiveData이기 때문에 변경이 감지됨
    }

    fun showMenuList(selectedItem: String) {
        val  spinnerList = ArrayList<IngredientInfo>()

        totalList.forEach {
            if(selectedItem == "평가완료"){
                if(it.rating > 0.0f) spinnerList.add(it)
            } else if(selectedItem == "미완료"){
                if(it.rating == 0.0f) spinnerList.add(it)
            } else if(selectedItem == "모두"){
                if(it.rating >= 0.0f) spinnerList.add(it)
            }
        }

        menuList.value = spinnerList
    }

    fun storeRatingDataFromMenuList() {

    }

}



