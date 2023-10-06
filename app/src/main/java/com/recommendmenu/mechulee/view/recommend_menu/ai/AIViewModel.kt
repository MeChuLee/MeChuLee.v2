package com.recommendmenu.mechulee.view.recommend_menu.ai

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.recommendmenu.mechulee.model.data.WeatherInfo
import com.recommendmenu.mechulee.utils.NetworkUtils

class AIViewModel : ViewModel() {

    var weatherInfo: MutableLiveData<WeatherInfo> = MutableLiveData()

    init {
        weatherInfo = MutableLiveData(NetworkUtils.weatherInfo)
    }
}