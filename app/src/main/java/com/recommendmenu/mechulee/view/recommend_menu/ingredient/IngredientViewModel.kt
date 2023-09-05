package com.recommendmenu.mechulee.view.recommend_menu.ingredient

import androidx.datastore.core.DataStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.orhanobut.logger.Logger
import com.recommendmenu.mechulee.IngredientData
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.model.data.IngredientInfo
import com.recommendmenu.mechulee.proto.checkedIngredientDataStore
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class IngredientViewModel(private val dataStore: DataStore<IngredientData>) : ViewModel() {

    val classificationList: MutableLiveData<ArrayList<String>> = MutableLiveData()
    val selectClassificationMap: MutableLiveData<Map<String, ArrayList<IngredientInfo>>> =
        MutableLiveData()

    private var tempCheckedIngredientInfo: List<String> = emptyList()
    var checkedIngredientInfo: List<String> = emptyList()

    private var vegetableList = arrayListOf(
        IngredientInfo(R.raw.corn, "양배추", 0.0f, "야채", false),
        IngredientInfo(R.raw.corn, "오이", 0.0f, "야채", false),
        IngredientInfo(R.raw.corn, "시금치", 0.0f, "야채", false),
        IngredientInfo(R.raw.corn, "토마토", 0.0f, "야채", false),
        IngredientInfo(R.raw.corn, "고구마", 0.0f, "야채", false),
        IngredientInfo(R.raw.corn, "단호박", 0.0f, "야채", false),
        IngredientInfo(R.raw.corn, "시래기", 0.0f, "야채", false),
        IngredientInfo(R.raw.corn, "파", 0.0f, "야채", false),
    )

    private var fruitList = arrayListOf(
        IngredientInfo(R.raw.corn, "사과", 0.0f, "과일", false),
        IngredientInfo(R.raw.corn, "대추", 0.0f, "과일", false),
    )

    private var riceAndNoodleList = arrayListOf(
        IngredientInfo(R.raw.corn, "쌀", 0.0f, "밥/면", false),
        IngredientInfo(R.raw.corn, "쌀국수", 0.0f, "밥/면", false),
        IngredientInfo(R.raw.corn, "스파게티", 0.0f, "밥/면", false),
        IngredientInfo(R.raw.corn, "당면", 0.0f, "밥/면", false),
    )

    private var meatList = arrayListOf(
        IngredientInfo(R.raw.corn, "소고기", 0.0f, "고기", false),
        IngredientInfo(R.raw.corn, "양갈비", 0.0f, "고기", false),
        IngredientInfo(R.raw.corn, "소시지", 0.0f, "고기", false),
        IngredientInfo(R.raw.corn, "삼겹살", 0.0f, "고기", false),
        IngredientInfo(R.raw.corn, "오리고기", 0.0f, "고기", false),
        IngredientInfo(R.raw.corn, "달고기", 0.0f, "고기", false),
    )

    private var fishList = arrayListOf(
        IngredientInfo(R.raw.corn, "생선", 0.0f, "생선", false),
        IngredientInfo(R.raw.corn, "오징어", 0.0f, "생선", false),
        IngredientInfo(R.raw.corn, "새우", 0.0f, "생선", false),
        IngredientInfo(R.raw.corn, "조기", 0.0f, "생선", false),
        IngredientInfo(R.raw.corn, "멸치", 0.0f, "생선", false),
        IngredientInfo(R.raw.corn, "쭈꾸미", 0.0f, "생선", false),
        IngredientInfo(R.raw.corn, "고등어", 0.0f, "생선", false),
        IngredientInfo(R.raw.corn, "어묵", 0.0f, "생선", false),
        IngredientInfo(R.raw.corn, "아귀", 0.0f, "생선", false),
    )

    private var sauceList = arrayListOf(
        IngredientInfo(R.raw.corn, "케첩", 0.0f, "소스", false),
        IngredientInfo(R.raw.corn, "고추장", 0.0f, "소스", false),
        IngredientInfo(R.raw.corn, "생크림", 0.0f, "소스", false),
        IngredientInfo(R.raw.corn, "버터", 0.0f, "소스", false),
    )

    private var otherList = arrayListOf(
        IngredientInfo(R.raw.corn, "우유", 0.0f, "기타", false),
        IngredientInfo(R.raw.corn, "김치", 0.0f, "기타", false),
        IngredientInfo(R.raw.corn, "두부", 0.0f, "기타", false),
        IngredientInfo(R.raw.corn, "치즈", 0.0f, "기타", false),
        IngredientInfo(R.raw.corn, "빵", 0.0f, "기타", false),
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
            initTotalMapFromDataStore()
            selectClassificationMap.value = ingredientTotalMap
        }
    }

    fun selectClassification(classification: String) {
        // 선택한 classification에 따라 selectClassificationMap에 반영
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

    suspend fun initTotalMapFromDataStore() {
        val storedCheckedData = dataStore.data.firstOrNull()

        storedCheckedData?.let {
            val updateTotalMap = mutableMapOf<String, ArrayList<IngredientInfo>>()
            ingredientTotalMap.forEach { nowMap ->
                val tempChecked = ArrayList<IngredientInfo>()
                nowMap.value.forEach { nowIngredient ->
                    var checkedValue = false
                    if (nowIngredient.title in it.selectedIngredientList) {
                        checkedValue = true
                    }
                    tempChecked.add(nowIngredient.copy(checked = checkedValue))
                }
                updateTotalMap[nowMap.key] = tempChecked
            }
            ingredientTotalMap = updateTotalMap
        }
    }

    suspend fun addCheckedIngredientInfo(ingredientList: ArrayList<String>) {
        viewModelScope.launch {
            dataStore.updateData { ingredientData ->
                ingredientData.toBuilder()
                    .clearSelectedIngredient()
                    .addAllSelectedIngredient(ingredientList)
                    .build()
            }
        }
        Logger.d("dataStore 값 확인 ${dataStore.data.firstOrNull()?.selectedIngredientList}")
    }

    fun deleteAllCheckedIngredientInfo() {
        viewModelScope.launch {
            dataStore.updateData {
                it.toBuilder().clearSelectedIngredient().build()
            }
        }
    }
//    fun loadCheckedIngredientInfo() {
//        viewModelScope.launch {
//            val storedData = dataStore.data.firstOrNull()
//            storedData.let {
//                val newnewList = it?.selectedIngredientList
//                Logger.d("들어가 있는거 불러오기 $newnewList")
//            }
//        }
//    }
}
