package com.recommendmenu.mechulee.view.like_menu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LikeViewModel : ViewModel() {
    private val totalList: ArrayList<String> = arrayListOf(
        "쿠키", "쿠키1", "쿠키2", "쿠키3", "쿠키4", "쿠키5",
        "쿠키6", "쿠키7", "쿠키8", "쿠키9", "쿠키10", "쿠키11",
        "쿠키12", "쿠키13", "쿠키14", "쿠키15", "쿠키16", "쿠키17",
        "쿠키18", "쿠키19", "쿠키20", "샐러드", "김치찌개", "된장찌개",
        "제육입니다", "안녕하십니까", "일곱글자뭐로해", "여덟글자는이걸로",
        "니가만든쿠키내가만든쿠키"
    )

    val nowList: MutableLiveData<ArrayList<String>> = MutableLiveData()

    init {
        nowList.value = totalList
    }

    fun removeMenu(menu: String) {
        val tempList = ArrayList<String>(nowList.value?.toList() ?: ArrayList())
        tempList.remove(menu)
        nowList.value = tempList
    }
}