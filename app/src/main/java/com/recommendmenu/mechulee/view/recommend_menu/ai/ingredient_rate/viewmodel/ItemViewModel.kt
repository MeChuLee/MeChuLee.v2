package com.recommendmenu.mechulee.view.recommend_menu.ai.ingredient_rate.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.model.data.IngredientInfo

class ItemViewModel : ViewModel() {

    private var totalList: ArrayList<IngredientInfo>

    val menuList: MutableLiveData<ArrayList<IngredientInfo>> = MutableLiveData()
    // 데이터의 변경을 관찰 가능한 형태로 만들어준다.

    init{
        // ViewModel이 생성될 때 데이터 초기화 작업 수행
        // 초기값이라고 보면 된다.
         totalList = arrayListOf(
            IngredientInfo(R.raw.pork_icon, "돼지고기", 0.0f),
             IngredientInfo(R.raw.beef_icon, "소고기", 0.0f),
             IngredientInfo(R.raw.pork_icon, "양상추", 0.0f),
             IngredientInfo(R.raw.beef_icon, "양갈비", 0.0f),
             IngredientInfo(R.raw.pork_icon, "소시지", 0.0f),
             IngredientInfo(R.raw.beef_icon, "삼겹살", 0.0f),
             IngredientInfo(R.raw.pork_icon, "양배추", 0.0f),
             IngredientInfo(R.raw.beef_icon, "오이", 0.0f),
             IngredientInfo(R.raw.pork_icon, "떡", 0.0f),
             IngredientInfo(R.raw.beef_icon, "오리고기", 0.0f),
             IngredientInfo(R.raw.pork_icon, "골뱅이", 0.0f),
             IngredientInfo(R.raw.beef_icon, "시금치", 0.0f),
             IngredientInfo(R.raw.pork_icon, "카레가루", 0.0f),
             IngredientInfo(R.raw.beef_icon, "오징어", 0.0f),
             IngredientInfo(R.raw.pork_icon, "토마토", 0.0f),
             IngredientInfo(R.raw.beef_icon, "라자냐", 0.0f),
        )

        menuList.value = totalList
        // 초기화하면서 menuList까지 동시에 totalList와 같도록
        // 초기화 시켜준다.
    }

    // 의문점 -> totalList를 mutable로 만들 수 있는건가? 그래야
    // 평가가 계속 바뀌는 걸 감지할 수 있는데, 그러면 menuList가
    // UI에 적용되도록 만들어야하나?

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
}

