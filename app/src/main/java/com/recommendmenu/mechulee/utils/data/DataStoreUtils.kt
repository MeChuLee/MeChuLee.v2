package com.recommendmenu.mechulee.utils.data

import androidx.datastore.core.DataStore
import com.recommendmenu.mechulee.LikeData
import com.recommendmenu.mechulee.RatingData
import com.recommendmenu.mechulee.model.data.IngredientInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

object DataStoreUtils {
    fun loadFromLikeData(myScope: CoroutineScope, myStore: DataStore<LikeData>, onResult: (List<String>) -> Unit ) {
        myScope.launch {
            val storedLikeData = myStore.data.firstOrNull()
            storedLikeData?.let {
                onResult(it.likedMenuList)
            }
        }
    }

    fun initTotalListFromDataStore(myScope: CoroutineScope,myStore: DataStore<RatingData>, onResult: (List<Float>) -> Unit) {
        myScope.launch {
            val storedRatingData = myStore.data.firstOrNull()

            // RatingData가 null이 아닐 경우, totalList의 rating 값을 업데이트한다.
            storedRatingData?.let {
                onResult(it.ratingList)
            }
        }
    }

    suspend fun updateDataStoreToRatingList(ratingList: List<Float>, dataStore: DataStore<RatingData>) {
        val ratingData = RatingData.newBuilder()
            .addAllRating(ratingList)
            .build()

        dataStore.updateData {
            ratingData.toBuilder()
                .clearRating()
                .addAllRating(ratingData.ratingList)
                .build()
        }
    }


}