package com.recommendmenu.mechulee.view.like_menu

import androidx.datastore.core.DataStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recommendmenu.mechulee.LikeData
import com.recommendmenu.mechulee.model.data.MenuInfo
import com.recommendmenu.mechulee.utils.DataStoreUtils
import com.recommendmenu.mechulee.utils.NetworkUtils

class LikeViewModel(private val dataStore: DataStore<LikeData>) : ViewModel() {
    private val menuInfoTotalList = NetworkUtils.totalMenuList
    private val totalList = ArrayList<String>()

    val nowList: MutableLiveData<ArrayList<MenuInfo>> = MutableLiveData()

    fun ready() {
        DataStoreUtils.loadFromLikeMenuData(viewModelScope, dataStore, onResult = {
            totalList.clear()
            totalList.addAll(it)
            val tempList = ArrayList<MenuInfo>()
            menuInfoTotalList.forEach { nowMenu ->
                if(nowMenu.name in totalList) {
                    tempList.add(nowMenu)
                }
            }
            nowList.value = tempList
        })
    }

    fun removeMenu(menu: String) {
        val tempList = ArrayList<MenuInfo>(nowList.value?.toList() ?: ArrayList())
        for(i in 0 until tempList.size) {
            if (tempList[i].name == menu) {
                tempList.removeAt(i)
                break
            }
        }
        nowList.value = tempList
    }

    fun storeLikeMenu() {
        val tempList = ArrayList<String>()
        nowList.value?.forEach {
            tempList.add(it.name)
        }
        DataStoreUtils.updateLikeMenuData(
            viewModelScope,
            dataStore,
            tempList
        )
    }
}