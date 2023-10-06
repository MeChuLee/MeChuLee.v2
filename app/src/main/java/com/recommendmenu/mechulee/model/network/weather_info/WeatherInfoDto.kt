package com.recommendmenu.mechulee.model.network.weather_info

import com.google.gson.annotations.SerializedName
import com.recommendmenu.mechulee.model.data.WeatherInfo

data class WeatherInfoDto(
    @SerializedName("weatherInfo")
    var weatherInfo: WeatherInfo,      // 날씨 정보
)
