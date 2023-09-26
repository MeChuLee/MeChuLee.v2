package com.recommendmenu.mechulee.view.result.menu

import androidx.datastore.core.DataStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recommendmenu.mechulee.LikeData
import com.recommendmenu.mechulee.model.data.MenuInfo
import com.recommendmenu.mechulee.utils.DataStoreUtils
import com.recommendmenu.mechulee.utils.NetworkUtils
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope

class MenuResultViewModel(private val dataStore: DataStore<LikeData>) : ViewModel() {
    // 전체 메뉴 리스트
    private var totalList = ArrayList<MenuInfo>()

    // data store에 저장된 메뉴 리스트
    val likedMenuList: MutableLiveData<ArrayList<String>> = MutableLiveData()

    // 현재 결과 메뉴
    var nowResult: MutableLiveData<MenuInfo> = MutableLiveData()

    // 현재 결과 메뉴의 재료
    val ingredientList = ArrayList<String>()

    // 현재 결과 메뉴와 비슷한 메뉴 (category 같은 메뉴 중 5개 랜덤 선택)
    val otherList: MutableLiveData<ArrayList<String>> = MutableLiveData()

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
        tempSplit.forEach {
            ingredientList.add(it)
        }
    }

    // 비슷한 메뉼르 선택하는 메소드
    // 카테고리가 같은 메뉴가 5개 이하면 그냥 그대로 그 갯수만큼 출력
    // 많으면 랜덤으로 5개의 index를 뽑아 그 위치에 해당하는 메뉴 출력
    private fun showOtherMenu(nowMenu: MenuInfo) {
        val nowCategory = nowMenu.category
        val sameCategoryList = ArrayList<MenuInfo>()
        totalList.forEach {
            if (it.category == nowCategory) {
                sameCategoryList.add(it)
            }
        }
        val numList = ArrayList<Int>()
        if (sameCategoryList.size <= 5) {
            numList.addAll(0 until sameCategoryList.size)
        } else {
            while (numList.size < 5) {
                val intRandom = (0 until sameCategoryList.size).random()
                if (intRandom !in numList) {
                    numList.add(intRandom)
                }
            }
        }
        val tempOtherMenuList = ArrayList<String>()
        for (i in numList) {
            tempOtherMenuList.add(sameCategoryList[i].name)
        }
        otherList.value = tempOtherMenuList
    }

    // viewModel에 있는 likedMenuList에 추가하거나 제거하는 메소드
    // 저장되지 않은 메뉴는 추가, 있는 메뉴는 제거
    fun checkLikeMenu(clickedData: String) {
        val tempList = ArrayList<String>(likedMenuList.value?.toList() ?: ArrayList())
        if (clickedData in tempList) {
            tempList.remove(clickedData)
        } else {
            tempList.add(clickedData)
        }
        likedMenuList.value = tempList
    }

    // 좋아요 클릭한 메뉴 DataStore에 저장하는 메소드
    @OptIn(DelicateCoroutinesApi::class)
    fun storeLikeMenu() {
        DataStoreUtils.updateLikeMenuData(
            GlobalScope,
            dataStore,
            ArrayList<String>(likedMenuList.value?.toList() ?: ArrayList())
        )
    }
}
