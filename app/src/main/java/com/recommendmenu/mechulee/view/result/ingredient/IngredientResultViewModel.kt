package com.recommendmenu.mechulee.view.result.ingredient

import androidx.datastore.core.DataStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recommendmenu.mechulee.IngredientData
import com.recommendmenu.mechulee.model.data.MenuInfo
import com.recommendmenu.mechulee.utils.DataStoreUtils
import com.recommendmenu.mechulee.utils.NetworkUtils

class IngredientResultViewModel: ViewModel() {

    // first : 메뉴 정보, second : 없는 재료의 index, third : 있는 재료 개수
    private val ingredientResultList = ArrayList<Pair<MenuInfo, ArrayList<Int>>>()
    val recommendResultList = MutableLiveData<ArrayList<Pair<MenuInfo, ArrayList<Int>>>>()

    fun create(dataStore: DataStore<IngredientData>) {
        DataStoreUtils.loadFromSelectedIngredientData(viewModelScope, dataStore, onResult = { selectedIngredientList ->
            NetworkUtils.totalMenuList.forEach { menu ->
                val ingredientList = menu.ingredients.split(", ")

                val pair = Pair(menu, ArrayList<Int>())

                ingredientList.forEachIndexed { index, ingredient ->
                    if (ingredient !in selectedIngredientList) {
                        pair.second.add(index)
                    }
                }

                ingredientResultList.add(pair)
            }

            // 없는 재료의 카운트가 적을 수록, (메뉴 재료 - 없는 재료 = 있는 재료)의 수가 많을 수록 정렬
            recommendResultList.value = ingredientResultList.sortedWith(compareBy({it.second.size}, {-(it.first.ingredients.split(", ").size - it.second.size)})).toCollection(ArrayList())
        })
    }
}