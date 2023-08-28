package com.recommendmenu.mechulee.view.menu_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.recommendmenu.mechulee.model.data.MenuInfo
import com.recommendmenu.mechulee.model.network.MenuDto
import com.recommendmenu.mechulee.model.network.MenuService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MenuListViewModel : ViewModel() {

    companion object {
        private const val MENU_CATEGORY_ALL = "전체"
    }

    private lateinit var totalList: ArrayList<MenuInfo>

    val categoryList: MutableLiveData<ArrayList<String>> = MutableLiveData()
    val menuList: MutableLiveData<ArrayList<MenuInfo>> = MutableLiveData()

    private var currentCategory = MENU_CATEGORY_ALL

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(MenuService::class.java)

        service.getAllIngredient()
            .enqueue(object : Callback<MenuDto> {
                override fun onResponse(call: Call<MenuDto>, response: Response<MenuDto>) {
                    if (response.isSuccessful.not()) {
                        println("테스트 : not Succesful")
                        return
                    }
                    response.body()?.let {
                        menuList.value = it.menuList.toCollection(ArrayList())
                        println("테스트 : 성공")
                    }
                }

                override fun onFailure(call: Call<MenuDto>, t: Throwable) {
                    // 실패
                    println("테스트 : 실패")
                    println(t.message)
                }

            })

        categoryList.value = arrayListOf("전체", "한식", "중식", "일식", "양식", "분식", "아시안")
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