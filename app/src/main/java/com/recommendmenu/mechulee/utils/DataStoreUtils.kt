package com.recommendmenu.mechulee.utils

import androidx.datastore.core.DataStore
import androidx.lifecycle.viewModelScope
import com.recommendmenu.mechulee.LikeData
import com.recommendmenu.mechulee.RatingData
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