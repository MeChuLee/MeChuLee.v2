package com.recommendmenu.mechulee.view.recommend_menu.ingredient

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.recommendmenu.mechulee.model.data.MenuInfo

class ResultViewModel : ViewModel() {
    private val totalList: ArrayList<MenuInfo> = arrayListOf(
        MenuInfo("된장찌개", "김치, 두부, 파, 양파, 고추", "한식"),
        MenuInfo("바질 페스토 파스타", "김치, 두부, 파, 양파, 고추", "양식"),
        MenuInfo("바지락칼국수", "김치, 두부, 파, 양파, 고추", "한식"),
        MenuInfo("닭볶음탕", "김치, 두부, 파, 양파, 고추", "한식"),
        MenuInfo("비빔밥", "김치, 두부, 파, 양파, 고추", "한식"),
        MenuInfo("돼지불고기", "김치, 두부, 파, 양파, 고추", "한식"),
        MenuInfo("돼지갈비찜", "김치, 두부, 파, 양파, 고추", "한식"),
        MenuInfo("닭갈비", "김치, 두부, 파, 양파, 고추", "한식"),
        MenuInfo("햄버거", "빵, 돼지고기, 양파, 토마토, 양상추, 마요네즈, 김치, 김치, 김치, 김치, 김치", "양식"),
        MenuInfo("피자", "김치, 두부, 파, 양파, 고추", "양식"),
        MenuInfo("치킨", "김치, 두부, 파, 양파, 고추", "양식"),
        MenuInfo("리조또", "김치, 두부, 파, 양파, 고추", "양식"),
    )
    var nowResult = MutableLiveData<MenuInfo>()
    val ingredientList: MutableLiveData<ArrayList<String>> = MutableLiveData()
    val otherList: MutableLiveData<ArrayList<String>> = MutableLiveData()

    fun recommendResult(nowMenu: MenuInfo) {
        nowResult.value = nowMenu
    }

    fun showIngredient(nowMenu: MenuInfo) {
        val tempSplit = nowMenu.ingredients.split(", ")
        var tempList = ArrayList<String>()
        tempSplit.forEach {
            tempList.add(it)
        }
        ingredientList.value = tempList
    }

    // 비슷한 메뉴가 선택되는 함수 (지금은 같은 category인 경우만 고려)
    fun showOthers(nowMenu: MenuInfo) {
        val tempList = ArrayList<String>()

        totalList.forEach {
            if (it.category == nowMenu.category && it.name != nowMenu.name) {
                tempList.add(it.name)
            }
        }
        otherList.value = tempList
    }
}

