package com.recommendmenu.mechulee.view.recommend_menu.ingredient

import androidx.datastore.core.DataStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recommendmenu.mechulee.IngredientData
import com.recommendmenu.mechulee.model.data.IngredientInfo
import com.recommendmenu.mechulee.utils.DataStoreUtils
import com.recommendmenu.mechulee.utils.NetworkUtils
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope

class IngredientViewModel(private val dataStore: DataStore<IngredientData>) : ViewModel() {

    companion object {
        private val INGREDIENT_CLASSIFICATION_LIST =
            arrayListOf("전체", "야채", "과일", "밥/면", "고기", "생선", "소스", "기타")
    }

    val classificationList: MutableLiveData<ArrayList<String>> = MutableLiveData()

    private val myStoredIngredient = ArrayList<String>()
    val selectedIngredientList: MutableLiveData<ArrayList<String>> = MutableLiveData()

    // 여기에 서버에서 가져온 재료들을 우선 저장 후 Map에 분류에 맞게 저장해야함
    private var totalList = ArrayList<IngredientInfo>()
    private var ingredientTotalMap = mapOf<String, ArrayList<IngredientInfo>>()

    private var vegetableList = ArrayList<IngredientInfo>()
    private var fruitList = ArrayList<IngredientInfo>()
    private var riceAndNoodleList = ArrayList<IngredientInfo>()
    private var meatList = ArrayList<IngredientInfo>()
    private var fishList = ArrayList<IngredientInfo>()
    private var sauceList = ArrayList<IngredientInfo>()
    private var otherList = ArrayList<IngredientInfo>()

    val selectedMap: MutableLiveData<Map<String, ArrayList<IngredientInfo>>> =
        MutableLiveData()

    fun ready() {
        totalList.addAll(NetworkUtils.totalIngredientList)
        totalList.forEach {
            when (it.classification) {
                "야채" -> vegetableList.add(it)
                "소스" -> sauceList.add(it)
                "고기" -> meatList.add(it)
                "기타" -> otherList.add(it)
                "생선" -> fishList.add(it)
                "과일" -> fruitList.add(it)
                "밥/면" -> riceAndNoodleList.add(it)
                else -> {}
            }
        }
        ingredientTotalMap = mapOf(
            "야채" to vegetableList,
            "과일" to fruitList,
            "밥/면" to riceAndNoodleList,
            "고기" to meatList,
            "생선" to fishList,
            "소스" to sauceList,
            "기타" to otherList,
        )
        selectedMap.value = ingredientTotalMap

        classificationList.value = INGREDIENT_CLASSIFICATION_LIST
        // DataStore에 저장된 값에 대한 초기화
        DataStoreUtils.loadFromSelectedIngredientData(viewModelScope, dataStore, onResult = {
            myStoredIngredient.addAll(it)
            selectedIngredientList.value = myStoredIngredient
        })
    }

    // 선택한 classification에 따라 selectClassificationMap에 반영
    fun selectClassification(classification: String) {
        if (classification == "전체") {
            selectedMap.value = ingredientTotalMap
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
            selectedMap.value = tempMap
        }
    }

    // ViewModel에 있는 selectedIngredientList(선택된 재료들)에 추가하거나 제거하는 메소드
    // 없는 재료는 추가, 있는 재료는 제거.
    fun checkSelectedIngredient(clickedData: String) {
        val tempList = ArrayList<String>(selectedIngredientList.value?.toList() ?: ArrayList())
        if (clickedData in tempList) {
            tempList.remove(clickedData)
        } else {
            tempList.add(clickedData)
        }
        selectedIngredientList.value = tempList
    }

    // 선택한 재료 모두 DataStore에 저장하는 메소드
    @OptIn(DelicateCoroutinesApi::class)
    fun storeSelectedIngredient() {
        DataStoreUtils.updateSelectedIngredientData(
            GlobalScope,
            dataStore,
            ArrayList<String>(selectedIngredientList.value?.toList() ?: ArrayList())
        )
    }
}
