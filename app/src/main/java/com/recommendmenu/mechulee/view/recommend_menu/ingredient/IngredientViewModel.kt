package com.recommendmenu.mechulee.view.recommend_menu.ingredient

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.model.data.IngredientInfo

class IngredientViewModel : ViewModel() {

    val classificationList: MutableLiveData<ArrayList<String>> = MutableLiveData()
    private var vegetableList: ArrayList<IngredientInfo>
    private var fruitList: ArrayList<IngredientInfo>
    private var meatList: ArrayList<IngredientInfo>
    private var noodleList: ArrayList<IngredientInfo>
    private var otherList: ArrayList<IngredientInfo>

    val ingredientAllMap: MutableLiveData<Map<String, ArrayList<IngredientInfo>>> =
        MutableLiveData()
    val selectClassificationMap: MutableLiveData<Map<String, ArrayList<IngredientInfo>>> =
        MutableLiveData()

    init {
        classificationList.value = arrayListOf("전체", "야채", "과일", "고기", "면", "추가재료")

        vegetableList = arrayListOf(
            IngredientInfo(R.raw.chili, "고추", 0.0f, "야채"),
            IngredientInfo(R.raw.potato, "감자", 0.0f, "야채"),
            IngredientInfo(R.raw.cucumber, "오이", 0.0f, "야채"),
            IngredientInfo(R.raw.carrot, "당근", 0.0f, "야채"),
            IngredientInfo(R.raw.tomato, "토마토", 0.0f, "야채"),
            IngredientInfo(R.raw.onion, "양파", 0.0f, "야채"),
            IngredientInfo(R.raw.eggplant, "가지", 0.0f, "야채"),
            IngredientInfo(R.raw.corn, "옥수수", 0.0f, "야채"),
        )

        fruitList = arrayListOf(
            IngredientInfo(R.raw.garlic, "딸기", 0.0f, "과일"),
            IngredientInfo(R.raw.garlic, "바나나", 0.0f, "과일"),
            IngredientInfo(R.raw.garlic, "사과", 0.0f, "과일"),
            IngredientInfo(R.raw.garlic, "배", 0.0f, "과일"),
            IngredientInfo(R.raw.garlic, "오렌지", 0.0f, "과일"),
        )

        meatList = arrayListOf(
            IngredientInfo(R.raw.garlic, "소고기", 0.0f, "고기"),
            IngredientInfo(R.raw.garlic, "돼지고기", 0.0f, "고기"),
            IngredientInfo(R.raw.garlic, "닭고기", 0.0f, "고기"),
        )

        noodleList = arrayListOf(
            IngredientInfo(R.raw.garlic, "국수", 0.0f, "면"),
            IngredientInfo(R.raw.garlic, "스파게티", 0.0f, "면"),
            IngredientInfo(R.raw.garlic, "칼국수", 0.0f, "면"),
            IngredientInfo(R.raw.garlic, "라면", 0.0f, "면"),
        )

        otherList = arrayListOf(
            IngredientInfo(R.raw.garlic, "쌀국수", 0.0f, "추가재료"),
            IngredientInfo(R.raw.garlic, "쯔유", 0.0f, "추가재료"),
            IngredientInfo(R.raw.garlic, "고사리", 0.0f, "추가재료"),
            IngredientInfo(R.raw.garlic, "미나리", 0.0f, "추가재료"),
            IngredientInfo(R.raw.garlic, "바질", 0.0f, "추가재료"),
        )


        ingredientAllMap.value = mapOf(
            "야채" to vegetableList,
            "과일" to fruitList,
            "고기" to meatList,
            "면" to noodleList,
            "추가재료" to otherList
        )

        selectClassificationMap.value = ingredientAllMap.value
    }

    fun selectClassification(classification: String) {
        // 선택한 classification에 따라 selectClassificationMap에 반영
        if (classification == "전체") {
            selectClassificationMap.value = ingredientAllMap.value
        } else {
            when (classification) {
                "야채" -> selectClassificationMap.value = mapOf(classification to vegetableList)
                "과일" -> selectClassificationMap.value = mapOf(classification to fruitList)
                "고기" -> selectClassificationMap.value = mapOf(classification to meatList)
                "면" -> selectClassificationMap.value = mapOf(classification to noodleList)
                "추가재료" -> selectClassificationMap.value = mapOf(classification to otherList)
            }
        }
    }
}
