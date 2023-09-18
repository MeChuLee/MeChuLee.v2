package com.recommendmenu.mechulee.view.recommend_menu.ingredient

import androidx.datastore.core.DataStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recommendmenu.mechulee.LikeData
import com.recommendmenu.mechulee.model.data.MenuInfo
import com.recommendmenu.mechulee.model.network.menu.MenuDto
import com.recommendmenu.mechulee.model.network.menu.OtherMenuService
import com.recommendmenu.mechulee.utils.DataStoreUtils
import com.recommendmenu.mechulee.utils.NetworkUtils
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResultViewModel(private val dataStore: DataStore<LikeData>) : ViewModel() {
    private var totalList = ArrayList<MenuInfo>()

    private var myStoredMenu = ArrayList<String>()
    val likedMenuList: MutableLiveData<ArrayList<String>> = MutableLiveData()

    lateinit var nowResult: MenuInfo
    val ingredientList = ArrayList<String>()
    val otherList: MutableLiveData<ArrayList<String>> = MutableLiveData()

    fun ready() {
        val retrofit = NetworkUtils.getRetrofitInstance(NetworkUtils.MY_SERVER_BASE_URL)

        val service = retrofit.create(OtherMenuService::class.java)

        service.getOtherMenuList(nowResult)
            .enqueue(object : Callback<MenuDto> {
                override fun onResponse(call: Call<MenuDto>, response: Response<MenuDto>) {
                    if (response.isSuccessful.not()) {
                        return
                    }
                    response.body()?.let { menuDto ->
                        val tempMenuList = menuDto.menuList
                        totalList = tempMenuList.toCollection(ArrayList())
                    }
                    val tempList = ArrayList<String>()
                    totalList.forEach {
                        tempList.add(it.name)
                    }
                    otherList.value = tempList
                }

                override fun onFailure(call: Call<MenuDto>, t: Throwable) {}
            })
    }

    init {
        DataStoreUtils.loadFromLikeMenuData(viewModelScope, dataStore, onResult = {
            myStoredMenu.addAll(it)
            likedMenuList.value = myStoredMenu
        })
    }

    // TODO 정리가 필요해보임.. 불필요한 메소드로 나눈 느낌
    fun showResult(nowMenu: MenuInfo) {
        nowResult = nowMenu.copy()
        showIngredient(nowMenu)
    }

    // 결과 메뉴의 재료 보여주는 메소드
    private fun showIngredient(nowMenu: MenuInfo) {
        val tempSplit = nowMenu.ingredients.split(", ")
        tempSplit.forEach {
            ingredientList.add(it)
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
