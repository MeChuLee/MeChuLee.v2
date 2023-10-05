package com.recommendmenu.mechulee.view.result.menu

import androidx.datastore.core.DataStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recommendmenu.mechulee.LikeData
import com.recommendmenu.mechulee.model.data.MenuInfo
import com.recommendmenu.mechulee.utils.DataStoreUtils
import com.recommendmenu.mechulee.utils.NetworkUtils
import com.recommendmenu.mechulee.utils.RecommendUtils

class MenuResultViewModel(private val dataStore: DataStore<LikeData>) : ViewModel() {
    // 전체 메뉴 리스트
    private var totalList = ArrayList<MenuInfo>()

    // data store에 저장된 메뉴 리스트
    val likedMenuList: MutableLiveData<ArrayList<String>> = MutableLiveData()

    // 현재 결과 메뉴
    var nowResult: MutableLiveData<MenuInfo> = MutableLiveData()

    // 현재 결과 메뉴의 재료
    val ingredientList: MutableLiveData<ArrayList<String>> = MutableLiveData()

    // 현재 결과 메뉴와 비슷한 메뉴 (category 같은 메뉴 중 5개 랜덤 선택)
    val otherList: MutableLiveData<ArrayList<MenuInfo>> = MutableLiveData()

    init {
        // NetworkUtils에 저장된 전체 메뉴를 불러 초기화
        totalList.addAll(NetworkUtils.totalMenuList)

        DataStoreUtils.loadFromLikeMenuData(viewModelScope, dataStore, onResult = {
            val tempList = ArrayList<String>()
            tempList.addAll(it)
            likedMenuList.value = tempList
        })
    }

    // 결과메뉴 세팅하는 함수
    fun showResult(nowMenu: MenuInfo) {
        nowResult.value = nowMenu.copy()
        showIngredient(nowMenu)
        showOtherMenu(nowMenu)
    }

    // 결과 메뉴의 재료 보여주는 메소드
    private fun showIngredient(nowMenu: MenuInfo) {
        val tempSplit = nowMenu.ingredients.split(", ")
        ingredientList.value = tempSplit.toCollection(ArrayList())
    }

    private fun showOtherMenu(nowMenu: MenuInfo) {
        otherList.value = RecommendUtils.recommendOtherMenu(nowMenu)
    }

    // 좋아요 클릭하거나 클릭 취소한 메뉴를 업데이트하는 메소드
    fun updateLikeMenu(
        isAdded: Boolean,
        clickedData: String,
        beforeFunction: () -> Unit,
        afterFunction: () -> Unit
    ) {
        beforeFunction()
        val tempList = ArrayList<String>(likedMenuList.value?.toList() ?: ArrayList())
        if (isAdded) {
            tempList.add(clickedData)
        } else {
            tempList.remove(clickedData)
        }
        DataStoreUtils.updateLikeMenuData(
            viewModelScope,
            dataStore,
            tempList,
            onResult = {
                afterFunction()
            }
        )
        likedMenuList.value = tempList
    }
}
