package com.recommendmenu.mechulee.view.recommend_menu.ai

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orhanobut.logger.Logger
import com.recommendmenu.mechulee.utils.weather.WEATHER
import com.recommendmenu.mechulee.utils.weather.WeatherInfo
import com.recommendmenu.mechulee.utils.weather.WeatherUtils
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AIViewModel : ViewModel() {

    val weatherInfo: MutableLiveData<WeatherInfo> = MutableLiveData()

    init {
        // AIFragment에서 AIViewModel이 생성될때 날씨 API서버와 연결
        getWeatherInfo()
    }

    private fun getWeatherInfo() {
        val numOfRows = 60 // 한 페이지 결과 수
        val pageNo = 1 // 페이지 번호
        val dataType = "JSON" // 응답 자료 형식
        val baseDate: String // 발표 일자
        val baseTime: String // 발표 시각
        val nx = 66 // 예보지점 x좌표
        val ny = 100 // 예보지점 y좌표

        // 준비 단계 : base_date(발표 일자), base_time(발표 시각)
        // 현재 날짜, 시간 정보 가져오기
        val cal = Calendar.getInstance()
        cal.add(Calendar.MINUTE, -30) // 현재 시간에서 30분을 뺍니다.
        baseDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(/* date = */ cal.time) // 현재 날짜
        baseTime = SimpleDateFormat("HHmm", Locale.getDefault()).format(cal.time) // 현재 시, 분

        Logger.d(baseDate + baseTime)

        // 날씨 정보 가져오기
        // (한 페이지 결과 수 = 60, 페이지 번호 = 1, 응답 자료 형식-"JSON", 발표 날싸, 발표 시각, 예보지점 좌표)
        val call = WeatherUtils.getRetrofitService().getWeather(
            numOfRows,
            pageNo,
            dataType,
            baseDate,
            baseTime,
            nx,
            ny
        )

        call.enqueue(object : retrofit2.Callback<WEATHER> {
            override fun onResponse(call: Call<WEATHER>, response: Response<WEATHER>) =
                if (response.isSuccessful) {
                    // 날씨 정보 가져오기
                    val weatherData = response.body()!!.response.body.items.item
                    Logger.d(weatherData) // 전체 카테고리별 item 출력

                    // 현재 시각부터 30분 뒤의 날씨 3개를 weatherInfo에 rainType, sky, temp 값을 담는다.
                    weatherInfo.value = WeatherInfo()

                    val totalCount = response.body()!!.response.body.totalCount - 1 // 59

                    for (i in 0..totalCount) { // 0..totalCount 0부터 59까지 60번 반복
                        if (i % 6 == 0) { // 카테고리가 6개 단위로 바뀌고 각 Item의 첫번째 것이 가장 빠른 예보시간에 접근가능
                            // 각 카테고리 별 첫번째 값 + 강수형태, 하늘상태, 기온에 해당하는 item을 선별해 가져온다.
                            when (weatherData[i].category) {
                                "PTY" -> {  // 강수 형태
                                    weatherInfo.value!!.rainType = when (weatherData[i].fcstValue) {
                                        "0" -> ""
                                        "1" -> "비"
                                        "2" -> "비/눈"
                                        "3" -> "눈"
                                        "5" -> "빗방울"
                                        "6" -> "빗방울눈날림"
                                        "7" -> "눈날림"
                                        else -> {
                                            // 어떤 값도 해당하지 않을 경우 값을 그대로 설정
                                            weatherData[i].fcstValue
                                        }
                                    }
                                }
                                "SKY" -> {  // 하늘 상태
                                    weatherInfo.value!!.sky = when (weatherData[i].fcstValue) {
                                        "1" -> "맑음"
                                        "2" -> "구름많음"
                                        "3" -> "흐림"
                                        else -> {
                                            weatherData[i].fcstValue
                                        }
                                    }
                                }
                                "T1H" -> weatherInfo.value!!.temp = weatherData[i].fcstValue  // 기온
                                else -> continue
                            }
                        }
                    }

                    Logger.d(weatherInfo.value)
                } else {
                    // API 요청이 실패한 경우에 대한 처리
                    Logger.d("API 요청 실패!!!")
                }

            override fun onFailure(call: Call<WEATHER>, t: Throwable) {
                // 네트워크 오류 등의 실패 시 처리
                Logger.d("통신 실패${t.message}")
            }
        }
        )
    }

}
