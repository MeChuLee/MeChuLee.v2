package com.recommendmenu.mechulee.view.result.ingredient

import androidx.datastore.core.DataStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recommendmenu.mechulee.IngredientData
import com.recommendmenu.mechulee.model.data.MenuInfo
import com.recommendmenu.mechulee.utils.DataStoreUtils
import com.recommendmenu.mechulee.utils.NetworkUtils

class IngredientResultViewModel : ViewModel() {

    // first : 메뉴 정보, second : 없는 재료의 index, third : 있는 재료 개수
    private val ingredientResultList = ArrayList<Pair<MenuInfo, ArrayList<Int>>>()
    val recommendResultList = MutableLiveData<ArrayList<Pair<MenuInfo, ArrayList<Int>>>>()

    fun create(dataStore: DataStore<IngredientData>) {
        DataStoreUtils.loadFromSelectedIngredientData(
            viewModelScope,
            dataStore,
            onResult = { selectedIngredientList ->
                NetworkUtils.totalMenuList.forEach { menu ->
                    val ingredientList = menu.ingredients

                    val pair = Pair(menu, ArrayList<Int>())

                    ingredientList.forEachIndexed { index, ingredient ->
                        if (ingredient !in selectedIngredientList) {
                            pair.second.add(index)
                        }
                    }

                    ingredientResultList.add(pair)
                }

                // 있는 재료의 수 / 전체 재료의 수 가 높을 수록
                val sortedList = ingredientResultList.sortedWith(compareBy({
                    val allIngredientSize: Double = it.first.ingredients.size.toDouble()
                    -((allIngredientSize - it.second.size.toDouble()) / allIngredientSize)
                }, { -it.first.ingredients.size })).toCollection(ArrayList())

                val resultList = ArrayList<Pair<MenuInfo, ArrayList<Int>>>()

                sortedList.forEach {
                    val allIngredientSize: Double = it.first.ingredients.size.toDouble()
                    if (((allIngredientSize - it.second.size) / allIngredientSize) >= 0.5) {
                        resultList.add(it)
                    } else {
                        return@forEach
                    }
                }

                recommendResultList.value = resultList
            })
    }
}