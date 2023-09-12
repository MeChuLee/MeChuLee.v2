package com.recommendmenu.mechulee.view.recommend_menu.ingredient

import androidx.datastore.core.DataStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recommendmenu.mechulee.LikeData
import com.recommendmenu.mechulee.model.data.MenuInfo
import com.recommendmenu.mechulee.model.network.menu.MenuDto
import com.recommendmenu.mechulee.model.network.menu.MenuService
import com.recommendmenu.mechulee.utils.DataStoreUtils
import com.recommendmenu.mechulee.utils.NetworkUtils
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResultViewModel(private val dataStore: DataStore<LikeData>) : ViewModel() {
    private var totalList: ArrayList<MenuInfo> = arrayListOf(
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

//    private lateinit var totalList: ArrayList<MenuInfo>

    private var myStoredMenu = ArrayList<String>()
    val likedMenuList: MutableLiveData<ArrayList<String>> = MutableLiveData()

    lateinit var nowResult: MenuInfo
    val ingredientList = ArrayList<String>()
    val otherList = ArrayList<String>()


    fun ready() {
        val retrofit = NetworkUtils.getRetrofitInstance(NetworkUtils.MY_SERVER_BASE_URL)

        val service = retrofit.create(MenuService::class.java)

        service.getAllIngredient()
            .enqueue(object : Callback<MenuDto> {
                override fun onResponse(call: Call<MenuDto>, response: Response<MenuDto>) {
                    if (response.isSuccessful.not()) {
                        return
                    }
                    response.body()?.let { menuDto ->
                        val tempMenuList = menuDto.menuList

                        totalList = tempMenuList.toCollection(ArrayList())
                    }
                }
                override fun onFailure(call: Call<MenuDto>, t: Throwable) { }
            })
    }

    init {
        DataStoreUtils.loadFromLikeMenuData(viewModelScope, dataStore, onResult = {
            myStoredMenu.addAll(it)
            likedMenuList.value = myStoredMenu
        })
    }

    fun showResult(nowMenu: MenuInfo) {
        nowResult = nowMenu.copy()
        showIngredient(nowMenu)
        showOthers(nowMenu)
    }

    // 결과 메뉴의 재료 보여주는 메소드
    private fun showIngredient(nowMenu: MenuInfo) {
        val tempSplit = nowMenu.ingredients.split(", ")
        tempSplit.forEach {
            ingredientList.add(it)
        }
    }

    // 비슷한 메뉴가 보여주는 메소드 (지금은 같은 category인 경우만 고려)
    private fun showOthers(nowMenu: MenuInfo) {
        totalList.forEach {
            if (it.category == nowMenu.category && it.name != nowMenu.name) {
                otherList.add(it.name)
            }
        }
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
