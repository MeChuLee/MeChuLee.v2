package com.recommendmenu.mechulee.view.recommend_menu.ai

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.recommendmenu.mechulee.model.data.WeatherInfo
import com.recommendmenu.mechulee.utils.LocationUtils
import com.recommendmenu.mechulee.utils.NetworkUtils

class AIViewModel : ViewModel() {

    var weatherInfo: MutableLiveData<WeatherInfo> = MutableLiveData()
    var location: MutableLiveData<String> = MutableLiveData()

    init {
        weatherInfo.value = NetworkUtils.weatherInfo
        val temp = LocationUtils.simpleAddress.split(" ")

        if(temp.size == 1){
            location.value = temp[0]
        }else{
            location.value = temp[0] + " " + temp[1]
        }
    }
}