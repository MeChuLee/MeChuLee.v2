package com.recommendmenu.mechulee.view.menu_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.recommendmenu.mechulee.model.data.MenuInfo
import com.recommendmenu.mechulee.utils.NetworkUtils

class MenuListViewModel : ViewModel() {

    companion object {
        private const val MENU_CATEGORY_ALL = "전체"
        private val MENU_CATEGORY_LIST = arrayListOf("전체", "한식", "중식", "일식", "양식", "분식", "아시안")
    }

    // 전체 메뉴 리스트 정보를 담고 있는 변수
    private var totalList = ArrayList<MenuInfo>()

    // 메뉴 카테고리 정보 리스트
    val categoryList: MutableLiveData<ArrayList<String>> = MutableLiveData()

    // 현재 RecyclerView 에 표시되고 있는 메뉴 리스트
    val menuList: MutableLiveData<ArrayList<MenuInfo>> = MutableLiveData()

    private var currentCategory = MENU_CATEGORY_ALL

    fun create() {
        totalList = NetworkUtils.totalMenuList
        menuList.value = NetworkUtils.totalMenuList

        // 메뉴 카테고리 정보 초기화
        categoryList.value = MENU_CATEGORY_LIST
    }

    // totalList 에서 searchWord 가 포함된 제목을 가지고 현재 category 에 포함된 메뉴를 menuList 에 반영
    fun searchMenuList(category: String?, searchWord: String) {
        if (category != null) currentCategory = category

        val searchList = ArrayList<MenuInfo>()

        totalList.forEach {
            if ((currentCategory == MENU_CATEGORY_ALL || currentCategory == it.category) && searchWord in it.name)
                searchList.add(it)
        }
        menuList.value = searchList
    }
}