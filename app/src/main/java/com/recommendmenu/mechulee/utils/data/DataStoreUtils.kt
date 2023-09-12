package com.recommendmenu.mechulee.utils.data

import androidx.datastore.core.DataStore
import com.recommendmenu.mechulee.IngredientData
import com.recommendmenu.mechulee.LikeData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

object DataStoreUtils {
    fun loadFromLikeMenuData(
        myScope: CoroutineScope,
        myStore: DataStore<LikeData>,
        onResult: (List<String>) -> Unit
    ) {
        myScope.launch {
            val storedLikeData = myStore.data.firstOrNull()
            storedLikeData?.let {
                onResult(it.likedMenuList)
            }
        }
    }

    fun updateLikeMenuData(
        myScope: CoroutineScope,
        myStore: DataStore<LikeData>,
        myList: List<String>
    ) {
        myScope.launch {
            myStore.updateData { menuData ->
                menuData.toBuilder()
                    .clearLikedMenu()
                    .addAllLikedMenu(myList)
                    .build()
            }
        }
    }

    fun loadFromSelectedIngredientData(
        myScope: CoroutineScope,
        myStore: DataStore<IngredientData>,
        onResult: (List<String>) -> Unit
    ) {
        myScope.launch {
            val storedIngredientData = myStore.data.firstOrNull()
            storedIngredientData?.let {
                onResult(it.selectedIngredientList)
            }
        }
    }

    fun updateSelectedIngredientData(
        myScope: CoroutineScope,
        myStore: DataStore<IngredientData>,
        myList: List<String>
    ) {
        myScope.launch {
            myStore.updateData { ingredientData ->
                ingredientData.toBuilder()
                    .clearSelectedIngredient()
                    .addAllSelectedIngredient(myList)
                    .build()
            }
        }
    }
}