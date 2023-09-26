package com.recommendmenu.mechulee.view.like_menu

import androidx.datastore.core.DataStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recommendmenu.mechulee.LikeData
import com.recommendmenu.mechulee.utils.DataStoreUtils
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope

class LikeViewModel(private val dataStore: DataStore<LikeData>) : ViewModel() {
    private val totalList = ArrayList<String>()

    val nowList: MutableLiveData<ArrayList<String>> = MutableLiveData()

    fun ready() {
        DataStoreUtils.loadFromLikeMenuData(viewModelScope, dataStore, onResult = {
            totalList.clear()
            totalList.addAll(it)
            nowList.value = totalList
        })
    }

    fun removeMenu(menu: String) {
        val tempList = ArrayList<String>(nowList.value?.toList() ?: ArrayList())
        tempList.remove(menu)
        nowList.value = tempList
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun storeLikeMenu() {
        val tempList = ArrayList<String>(nowList.value?.toList() ?: ArrayList())
        DataStoreUtils.updateLikeMenuData(
            GlobalScope,
            dataStore,
            tempList
        )
    }
}