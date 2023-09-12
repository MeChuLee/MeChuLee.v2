package com.recommendmenu.mechulee.utils

import androidx.datastore.core.DataStore
import androidx.lifecycle.viewModelScope
import com.recommendmenu.mechulee.IngredientData
import com.recommendmenu.mechulee.LikeData
import com.recommendmenu.mechulee.RatingData
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

    fun initTotalListFromDataStore(myScope: CoroutineScope, myStore: DataStore<RatingData>, onResult: (List<Float>) -> Unit) {
        myScope.launch {
            val storedRatingData = myStore.data.firstOrNull()

            // RatingData가 null이 아닐 경우, totalList의 rating 값을 업데이트한다.
            storedRatingData?.let {
                onResult(it.ratingList)
            }
        }
    }

    fun updateDataStoreToRatingList(myScope: CoroutineScope, myStore: DataStore<RatingData>, ratingList: List<Float>) {
        myScope.launch {
            val ratingData = RatingData.newBuilder()
                .addAllRating(ratingList)
                .build()

            myStore.updateData {
                ratingData.toBuilder()
                    .clearRating()
                    .addAllRating(ratingData.ratingList)
                    .build()
            }
        }
    }


}