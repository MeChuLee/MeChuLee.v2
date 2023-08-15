package com.recommendmenu.mechulee.view.menu_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.recommendmenu.mechulee.model.data.MenuInfo

class MenuListViewModel : ViewModel() {

    private var totalList: ArrayList<MenuInfo>

    val categoryList: MutableLiveData<ArrayList<String>> = MutableLiveData()
    val menuList: MutableLiveData<ArrayList<MenuInfo>> = MutableLiveData()

    init {
        categoryList.value = arrayListOf("전체", "한식", "중식", "일식", "양식", "분식", "아시안")
        totalList = arrayListOf(
            MenuInfo("된장찌개", "김치, 두부, 파, 양파, 고추", "한식"),
            MenuInfo("김치찌개", "김치, 두부, 파, 양파, 고추", "한식"),
            MenuInfo("바지락칼국수", "김치, 두부, 파, 양파, 고추", "한식"),
            MenuInfo("닭볶음탕", "김치, 두부, 파, 양파, 고추", "한식"),
            MenuInfo("비빔밥", "김치, 두부, 파, 양파, 고추", "한식"),
            MenuInfo("돼지불고기", "김치, 두부, 파, 양파, 고추", "한식"),
            MenuInfo("우거지감자탕", "김치, 두부, 파, 양파, 고추", "한식"),
            MenuInfo("돼지갈비찜", "김치, 두부, 파, 양파, 고추", "한식"),
            MenuInfo("오징어볶음", "김치, 두부, 파, 양파, 고추", "한식"),
            MenuInfo("멸치국수", "김치, 두부, 파, 양파, 고추", "한식"),
            MenuInfo("닭갈비", "김치, 두부, 파, 양파, 고추", "한식"),
            MenuInfo("안동찜닭", "김치, 두부, 파, 양파, 고추", "한식"),
            MenuInfo("김치볶음밥", "김치, 두부, 파, 양파, 고추", "한식"),
            MenuInfo("햄버거", "김치, 두부, 파, 양파, 고추", "양식"),
            MenuInfo("피자", "김치, 두부, 파, 양파, 고추", "양식"),
            MenuInfo("치킨", "김치, 두부, 파, 양파, 고추", "양식"),
            MenuInfo("리조또", "김치, 두부, 파, 양파, 고추", "양식"),
        )
        menuList.value = totalList
    }

    // totalList 에서 searchWord 가 포함된 제목을 가진 메뉴를 menuList 에 반영
    fun searchMenuList(searchWord: String) {
        val searchList = ArrayList<MenuInfo>()
        totalList.forEach { if (searchWord in it.title) searchList.add(it) }
        menuList.value = searchList
    }

    // totalList 에서 category 가 인자로 받은 category 와 같은 경우 menuList 에 반영
    fun searchMenuListWithCategory(category: String) {
        val searchList = ArrayList<MenuInfo>()
        totalList.forEach { if (category == it.category) searchList.add(it) }
        menuList.value = searchList
    }
}