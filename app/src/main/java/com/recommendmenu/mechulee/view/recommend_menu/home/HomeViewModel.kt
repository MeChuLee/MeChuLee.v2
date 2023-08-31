package com.recommendmenu.mechulee.view.recommend_menu.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    val currentAddress = MutableLiveData<String>()

    fun setCurrentAddress(address: String) {
        // Activity 에서 사용한 Coroutine 에서 CallBack 을 받았기 때문에 현재 Background Thread 상태
        // Background Thread 에서는 ViewModel 의 LiveData 값을 변경할 때, setValue() 대신 postValue() 사용
        currentAddress.postValue(address)
    }
}