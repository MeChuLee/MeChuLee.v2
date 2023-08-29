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
        MenuInfo("햄버거", "빵, 돼지고기, 양파, 토마토, 양상추, 마요네즈", "양식"),
        MenuInfo("피자", "김치, 두부, 파, 양파, 고추", "양식"),
        MenuInfo("치킨", "김치, 두부, 파, 양파, 고추", "양식"),
        MenuInfo("리조또", "김치, 두부, 파, 양파, 고추", "양식"),
    )
    private val otherList: MutableLiveData<ArrayList<MenuInfo>> = MutableLiveData()
    private var nowResult = MutableLiveData<MenuInfo>()

    // 비슷한 메뉴가 선택되는 함수 (지금은 같은 category인 경우만 고려)
    fun recommendOthers(nowTarget: String?, nowMenu: String?): ArrayList<MenuInfo>? {
        val tempList = ArrayList<MenuInfo>()

        totalList.forEach {
            if (it.category == nowTarget && it.title != nowMenu) {
                tempList.add(it)
            }
        }
        otherList.value = tempList
        return otherList.value
    }

    // 결과를 return하는 함수
    fun getResult(menuName: String): MenuInfo? {
        totalList.forEach {
            if (it.title == menuName) {
                nowResult.value = it
            }
        }
        return nowResult.value
    }
}










