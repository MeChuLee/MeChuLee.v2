package com.recommendmenu.mechulee.view.recommend_menu.ingredient

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.recommendmenu.mechulee.model.data.MenuInfo

class ResultViewModel : ViewModel() {
    private val totalList: ArrayList<MenuInfo> = arrayListOf(
        MenuInfo("된장찌개", "김치, 두부, 파, 양파, 고추", "한식"),
        MenuInfo("김치찌개", "김치, 두부, 파, 양파, 고추", "한식"),
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

    init {
        // 최종 결과를 myResult에 넣고 초기화
        var myResult = "햄버거"
        totalList.forEach {
            if (it.name == myResult) {
                nowResult.value = it
            }
        }
        nowResult.value?.let {
            getIngredient(it)
            recommendOthers(it.category, it.name)
        }
    }

    fun getIngredient(nowMenu: MenuInfo) {
        val tempSplit = nowMenu.ingredients.split(", ")
        var tempList = ArrayList<String>()
        tempSplit.forEach {
            tempList.add(it)
        }
        ingredientList.value = tempList
    }

    // 비슷한 메뉴가 선택되는 함수 (지금은 같은 category인 경우만 고려)
    fun recommendOthers(nowTarget: String, nowMenu: String) {
        val tempList = ArrayList<String>()

        totalList.forEach {
            if (it.category == nowTarget && it.name != nowMenu) {
                tempList.add(it.name)
            }
        }
        otherList.value = tempList
    }
}

