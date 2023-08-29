package com.recommendmenu.mechulee.view.recommend_menu.ingredient

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.model.data.IngredientInfo

class IngredientViewModel(application: Application) : AndroidViewModel(application) {

    val classificationList: MutableLiveData<ArrayList<String>> = MutableLiveData()
    private var vegetableList: ArrayList<IngredientInfo>
    private var fruitList: ArrayList<IngredientInfo>
    private var meatList: ArrayList<IngredientInfo>
    private var fishList: ArrayList<IngredientInfo>
    private var riceAndNoodleList: ArrayList<IngredientInfo>
    private var sauceList: ArrayList<IngredientInfo>
    private var otherList: ArrayList<IngredientInfo>

    val ingredientAllMap: MutableLiveData<Map<String, ArrayList<IngredientInfo>>> =
        MutableLiveData()
    val selectClassificationMap: MutableLiveData<Map<String, ArrayList<IngredientInfo>>> =
        MutableLiveData()

    init {
        classificationList.value = arrayListOf("전체", "야채", "과일", "밥/면", "고기", "생선", "소스", "기타")

        vegetableList = arrayListOf(
            IngredientInfo(R.raw.corn, "양배추", 0.0f, "야채", false),
            IngredientInfo(R.raw.corn, "오이", 0.0f, "야채", false),
            IngredientInfo(R.raw.corn, "시금치", 0.0f, "야채", false),
            IngredientInfo(R.raw.corn, "토마토", 0.0f, "야채", false),
            IngredientInfo(R.raw.corn, "고구마", 0.0f, "야채", false),
            IngredientInfo(R.raw.corn, "단호박", 0.0f, "야채", false),
            IngredientInfo(R.raw.corn, "시래기", 0.0f, "야채", false),
            IngredientInfo(R.raw.corn, "파", 0.0f, "야채", false),
        )

        fruitList = arrayListOf(
            IngredientInfo(R.raw.corn, "사과", 0.0f, "과일", false),
            IngredientInfo(R.raw.corn, "대추", 0.0f, "과일", false),
        )

        riceAndNoodleList = arrayListOf(
            IngredientInfo(R.raw.corn, "쌀", 0.0f, "밥/면", false),
            IngredientInfo(R.raw.corn, "쌀국수", 0.0f, "밥/면", false),
            IngredientInfo(R.raw.corn, "스파게티", 0.0f, "밥/면", false),
            IngredientInfo(R.raw.corn, "당면", 0.0f, "밥/면", false),
        )

        meatList = arrayListOf(
            IngredientInfo(R.raw.corn, "소고기", 0.0f, "고기", false),
            IngredientInfo(R.raw.corn, "양갈비", 0.0f, "고기", false),
            IngredientInfo(R.raw.corn, "소시지", 0.0f, "고기", false),
            IngredientInfo(R.raw.corn, "삼겹살", 0.0f, "고기", false),
            IngredientInfo(R.raw.corn, "오리고기", 0.0f, "고기", false),
            IngredientInfo(R.raw.corn, "달고기", 0.0f, "고기", false),
        )

        fishList = arrayListOf(
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

        sauceList = arrayListOf(
            IngredientInfo(R.raw.corn, "케첩", 0.0f, "소스", false),
            IngredientInfo(R.raw.corn, "고추장", 0.0f, "소스", false),
            IngredientInfo(R.raw.corn, "생크림", 0.0f, "소스", false),
            IngredientInfo(R.raw.corn, "버터", 0.0f, "소스", false),
        )

        otherList = arrayListOf(
            IngredientInfo(R.raw.corn, "우유", 0.0f, "기타", false),
            IngredientInfo(R.raw.corn, "김치", 0.0f, "기타", false),
            IngredientInfo(R.raw.corn, "두부", 0.0f, "기타", false),
            IngredientInfo(R.raw.corn, "치즈", 0.0f, "기타", false),
            IngredientInfo(R.raw.corn, "빵", 0.0f, "기타", false),
        )


        ingredientAllMap.value = mapOf(
            "야채" to vegetableList,
            "과일" to fruitList,
            "밥/면" to riceAndNoodleList,
            "고기" to meatList,
            "생선" to fishList,
            "소스" to sauceList,
            "기타" to otherList,
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
                "밥/면" -> selectClassificationMap.value = mapOf(classification to riceAndNoodleList)
                "고기" -> selectClassificationMap.value = mapOf(classification to meatList)
                "생선" -> selectClassificationMap.value = mapOf(classification to fishList)
                "소스" -> selectClassificationMap.value = mapOf(classification to sauceList)
                "기타" -> selectClassificationMap.value = mapOf(classification to otherList)
            }
        }
    }
}
