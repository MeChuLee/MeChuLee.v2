package com.recommendmenu.mechulee.view.menu_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.recommendmenu.mechulee.model.data.MenuInfo
import com.recommendmenu.mechulee.model.network.menu.MenuDto
import com.recommendmenu.mechulee.model.network.menu.MenuService
import com.recommendmenu.mechulee.utils.network.NetworkUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuListViewModel : ViewModel() {

    companion object {
        private const val MENU_CATEGORY_ALL = "전체"
        private val MENU_CATEGORY_LIST = arrayListOf("전체", "한식", "중식", "일식", "양식", "분식", "아시안")
    }

    // 전체 메뉴 리스트 정보를 담고 있는 변수
    private lateinit var totalList: ArrayList<MenuInfo>

    // 메뉴 카테고리 정보 리스트
    val categoryList: MutableLiveData<ArrayList<String>> = MutableLiveData()

    // 현재 RecyclerView 에 표시되고 있는 메뉴 리스트
    val menuList: MutableLiveData<ArrayList<MenuInfo>> = MutableLiveData()

    private var currentCategory = MENU_CATEGORY_ALL

    fun create() {
        // retrofit instance 획득
        val retrofit = NetworkUtils.getRetrofitInstance(NetworkUtils.MY_SERVER_BASE_URL)

        val service = retrofit.create(MenuService::class.java)

        // retrofit 실행
        service.getAllIngredient()
            .enqueue(object : Callback<MenuDto> {
                override fun onResponse(call: Call<MenuDto>, response: Response<MenuDto>) {
                    if (response.isSuccessful.not()) {
                        return
                    }
                    // 성공 시, response.body 에 있는 데이터(응답 데이터) 가져오기
                    response.body()?.let { menuDto ->
                        // 응답으로 온 메뉴 데이터를 viewModel data 에 반영 ( 분류 순 정렬 )
                        val resultMenuList = menuDto.menuList.sortedWith(compareBy { it.category })

                        totalList = resultMenuList.toCollection(ArrayList())
                        menuList.value = resultMenuList.toCollection(ArrayList())
                    }
                }

                override fun onFailure(call: Call<MenuDto>, t: Throwable) { }
            })

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