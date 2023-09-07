package com.recommendmenu.mechulee.view.recommend_menu.ingredient

import androidx.datastore.core.DataStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recommendmenu.mechulee.IngredientData
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.model.data.IngredientInfo
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class IngredientViewModel(private val dataStore: DataStore<IngredientData>) : ViewModel() {

    val classificationList: MutableLiveData<ArrayList<String>> = MutableLiveData()
    val selectClassificationMap: MutableLiveData<Map<String, ArrayList<IngredientInfo>>> =
        MutableLiveData()
    val selectedIngredientList: MutableLiveData<ArrayList<String>> = MutableLiveData()

    private val myStoredIngredient = ArrayList<String>()

    private var vegetableList = arrayListOf(
        IngredientInfo(R.raw.corn, "양배추", 0.0f, "야채"),
        IngredientInfo(R.raw.corn, "오이", 0.0f, "야채"),
        IngredientInfo(R.raw.corn, "시금치", 0.0f, "야채"),
        IngredientInfo(R.raw.corn, "토마토", 0.0f, "야채"),
        IngredientInfo(R.raw.corn, "고구마", 0.0f, "야채"),
        IngredientInfo(R.raw.corn, "단호박", 0.0f, "야채"),
        IngredientInfo(R.raw.corn, "시래기", 0.0f, "야채"),
        IngredientInfo(R.raw.corn, "파", 0.0f, "야채"),
    )

    private var fruitList = arrayListOf(
        IngredientInfo(R.raw.corn, "사과", 0.0f, "과일"),
        IngredientInfo(R.raw.corn, "대추", 0.0f, "과일"),
    )

    private var riceAndNoodleList = arrayListOf(
        IngredientInfo(R.raw.corn, "쌀", 0.0f, "밥/면"),
        IngredientInfo(R.raw.corn, "쌀국수", 0.0f, "밥/면"),
        IngredientInfo(R.raw.corn, "스파게티", 0.0f, "밥/면"),
        IngredientInfo(R.raw.corn, "당면", 0.0f, "밥/면"),
    )

    private var meatList = arrayListOf(
        IngredientInfo(R.raw.corn, "소고기", 0.0f, "고기"),
        IngredientInfo(R.raw.corn, "양갈비", 0.0f, "고기"),
        IngredientInfo(R.raw.corn, "소시지", 0.0f, "고기"),
        IngredientInfo(R.raw.corn, "삼겹살", 0.0f, "고기"),
        IngredientInfo(R.raw.corn, "오리고기", 0.0f, "고기"),
        IngredientInfo(R.raw.corn, "달고기", 0.0f, "고기"),
    )

    private var fishList = arrayListOf(
        IngredientInfo(R.raw.corn, "생선", 0.0f, "생선"),
        IngredientInfo(R.raw.corn, "오징어", 0.0f, "생선"),
        IngredientInfo(R.raw.corn, "새우", 0.0f, "생선"),
        IngredientInfo(R.raw.corn, "조기", 0.0f, "생선"),
        IngredientInfo(R.raw.corn, "멸치", 0.0f, "생선"),
        IngredientInfo(R.raw.corn, "쭈꾸미", 0.0f, "생선"),
        IngredientInfo(R.raw.corn, "고등어", 0.0f, "생선"),
        IngredientInfo(R.raw.corn, "어묵", 0.0f, "생선"),
        IngredientInfo(R.raw.corn, "아귀", 0.0f, "생선"),
    )

    private var sauceList = arrayListOf(
        IngredientInfo(R.raw.corn, "케첩", 0.0f, "소스"),
        IngredientInfo(R.raw.corn, "고추장", 0.0f, "소스"),
        IngredientInfo(R.raw.corn, "생크림", 0.0f, "소스"),
        IngredientInfo(R.raw.corn, "버터", 0.0f, "소스"),
    )

    private var otherList = arrayListOf(
        IngredientInfo(R.raw.corn, "우유", 0.0f, "기타"),
        IngredientInfo(R.raw.corn, "김치", 0.0f, "기타"),
        IngredientInfo(R.raw.corn, "두부", 0.0f, "기타"),
        IngredientInfo(R.raw.corn, "치즈", 0.0f, "기타"),
        IngredientInfo(R.raw.corn, "빵", 0.0f, "기타"),
    )

    private var ingredientTotalMap = mapOf(
        "야채" to vegetableList,
        "과일" to fruitList,
        "밥/면" to riceAndNoodleList,
        "고기" to meatList,
        "생선" to fishList,
        "소스" to sauceList,
        "기타" to otherList,
    )

    init {
        viewModelScope.launch {
            classificationList.value = arrayListOf("전체", "야채", "과일", "밥/면", "고기", "생선", "소스", "기타")
            // DataStore에 저장된 값에 대한 초기화
            selectClassificationMap.value = ingredientTotalMap
            initTotalMapFromDataStore()
            selectedIngredientList.value = myStoredIngredient
        }
    }

    // 선택한 classification에 따라 selectClassificationMap에 반영
    fun selectClassification(classification: String) {
        viewModelScope.launch {
            initTotalMapFromDataStore()
        }
        if (classification == "전체") {
            selectClassificationMap.value = ingredientTotalMap
        } else {
            lateinit var tempMap: Map<String, ArrayList<IngredientInfo>>
            when (classification) {
                "야채" -> tempMap = mapOf(classification to vegetableList)
                "과일" -> tempMap = mapOf(classification to fruitList)
                "밥/면" -> tempMap = mapOf(classification to riceAndNoodleList)
                "고기" -> tempMap = mapOf(classification to meatList)
                "생선" -> tempMap = mapOf(classification to fishList)
                "소스" -> tempMap = mapOf(classification to sauceList)
                "기타" -> tempMap = mapOf(classification to otherList)
            }
            selectClassificationMap.value = tempMap
        }
    }

    // DataStore에 저장된 재료들을 확인해 myStoredIngredient에 넣고 그 값을 selectedIngredientList에 적용시킨다.
    private suspend fun initTotalMapFromDataStore() {
        val storedCheckedData = dataStore.data.firstOrNull()

        storedCheckedData?.let {
            ingredientTotalMap.forEach { nowMap ->
                nowMap.value.forEach { nowIngredient ->
                    if (nowIngredient.title in it.selectedIngredientList) {
                        myStoredIngredient.add(nowIngredient.title)
                    }
                }
            }
        }
    }

    // 입력받은 리스트의 값들을 모두 DataStore에 저장하는 메소드
    fun addCheckedIngredientInfo(ingredientList: ArrayList<String>) {
        viewModelScope.launch {
            dataStore.updateData { ingredientData ->
                ingredientData.toBuilder()
                    .clearSelectedIngredient()
                    .addAllSelectedIngredient(ingredientList)
                    .build()
            }
        }
    }

    // ViewModel에 있는 selectedIngredientList(선택된 재료들)에 추가하거나 제거하는 메소드
    // 없는 재료면 추가해주고, 있는 재료면 제거해준다.
    fun checkClickedIngredient(clickedData: String) {
        val tempList = ArrayList<String>(selectedIngredientList.value?.toList() ?: ArrayList())
        if (clickedData in tempList) {
            tempList.remove(clickedData)
        } else {
            tempList.add(clickedData)
        }
        selectedIngredientList.value = tempList
    }
}
