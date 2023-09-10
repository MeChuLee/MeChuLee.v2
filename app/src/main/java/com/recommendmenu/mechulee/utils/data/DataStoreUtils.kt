package com.recommendmenu.mechulee.utils.data

import androidx.datastore.core.DataStore
import com.recommendmenu.mechulee.LikeData
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
}