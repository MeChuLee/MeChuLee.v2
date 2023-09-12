package com.recommendmenu.mechulee.view.recommend_menu.ai

import android.graphics.Point
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

    //val weatherInfo: MutableLiveData<WeatherInfo> = MutableLiveData()

    var weatherInfoRainType: MutableLiveData<String> = MutableLiveData()
    val weatherInfoSky: MutableLiveData<String> = MutableLiveData()
    val weatherInfoTemp: MutableLiveData<String> = MutableLiveData()

    private fun getWeatherInfoNew(x: Int, y: Int) {
        val numOfRows = 60 // 한 페이지 결과 수
        val pageNo = 1 // 페이지 번호
        val dataType = "JSON" // 응답 자료 형식
        var baseDate: String // 발표 일자
        val timeH: String // 기준 시
        val timeM: String // 기준 분

        val xyPoint = dfsXyConv(x, y)   // 위도 경도 좌표 변환
        val nx = xyPoint.x // 예보지점 x좌표
        val ny = xyPoint.y // 예보지점 y좌표

        // 준비 단계 : base_date(발표 일자), base_time(발표 시각)
        // 현재 날짜, 시간 정보 가져오기
        val cal = Calendar.getInstance()
        baseDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(/* date = */ cal.time) // 현재 날짜
        timeH = SimpleDateFormat("HH", Locale.getDefault()).format(cal.time) // 현재 시
        timeM = SimpleDateFormat("mm", Locale.getDefault()).format(cal.time) // 현재 분

        // API 가져오기 적당하게 변환
        val baseTime: String = getBaseTime(timeH, timeM) // 발표 시각

        // 현재 시각이 00시이고 45분 이하여서 baseTime이 2330이면 어제 정보 받아오기
        if (timeH == "00" && baseTime == "2330") {
            cal.add(Calendar.DATE, -1).toString()
            baseDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time)
        }

        Logger.d("날짜: $baseDate  시간: $baseTime 좌표: $nx  $ny")

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

                    val totalCount = response.body()!!.response.body.totalCount - 1 // 59

                    for (i in 0..totalCount) { // 0..totalCount 0부터 59까지 60번 반복
                        if (i % 6 == 0) { // 카테고리가 6개 단위로 바뀌고 각 Item의 첫번째 것이 가장 빠른 예보시간에 접근가능
                            // 각 카테고리 별 첫번째 값 + 강수형태, 하늘상태, 기온에 해당하는 item을 선별해 가져온다.
                            when (weatherData[i].category) {
                                "PTY" -> {  // 강수 형태
                                    weatherInfoRainType.value = when (weatherData[i].fcstValue) {
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
                                    weatherInfoSky.value = when (weatherData[i].fcstValue) {
                                        "1" -> "맑음"
                                        "2" -> "구름많음"
                                        "3" -> "흐림"
                                        else -> {
                                            weatherData[i].fcstValue
                                        }
                                    }
                                }
                                "T1H" -> weatherInfoTemp.value = weatherData[i].fcstValue  // 기온
                                else -> continue
                            }
                        }
                    }

                    Logger.d("${weatherInfoRainType.value}  " +
                            "${weatherInfoSky.value.toString()}  " +
                            "${weatherInfoTemp.value.toString()}  " +
                            "$nx  $ny")
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

    fun setLocationXY(latitude: Double, longitude: Double) {
        getWeatherInfoNew(latitude.toInt(), longitude.toInt())
    }

    // baseTime 설정하기
    private fun getBaseTime(h : String, m : String) : String {

        // 45분 전이면
        val result: String = if (m.toInt() < 45) {
            // 0시면 2330
            if (h == "00") "2330"
            // 아니면 1시간 전 날씨 정보 부르기
            else {
                val resultH = h.toInt() - 1
                // 1자리면 0 붙여서 2자리로 만들기
                if (resultH < 10) "0" + resultH + "30"
                // 2자리면 그대로
                else resultH.toString() + "30"
            }
        }
        // 45분 이후면 바로 정보 받아오기
        else h + "30"

        return result
    }

    // 위경도를 기상청에서 사용하는 격자 좌표로 변환
    private fun dfsXyConv(v1: Int, v2: Int) : Point {
        val RE = 6371.00877     // 지구 반경(km)
        val GRID = 5.0          // 격자 간격(km)
        val SLAT1 = 30.0        // 투영 위도1(degree)
        val SLAT2 = 60.0        // 투영 위도2(degree)
        val OLON = 126.0        // 기준점 경도(degree)
        val OLAT = 38.0         // 기준점 위도(degree)
        val XO = 43             // 기준점 X좌표(GRID)
        val YO = 136            // 기준점 Y좌표(GRID)
        val DEGRAD = Math.PI / 180.0
        val re = RE / GRID
        val slat1 = SLAT1 * DEGRAD
        val slat2 = SLAT2 * DEGRAD
        val olon = OLON * DEGRAD
        val olat = OLAT * DEGRAD

        var sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5)
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn)
        var sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5)
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn
        var ro = Math.tan(Math.PI * 0.25 + olat * 0.5)
        ro = re * sf / Math.pow(ro, sn)

        var ra = Math.tan(Math.PI * 0.25 + (v1) * DEGRAD * 0.5)
        ra = re * sf / Math.pow(ra, sn)
        var theta = v2 * DEGRAD - olon
        if (theta > Math.PI) theta -= 2.0 * Math.PI
        if (theta < -Math.PI) theta += 2.0 * Math.PI
        theta *= sn

        val x = (ra * Math.sin(theta) + XO + 0.5).toInt()
        val y = (ro - ra * Math.cos(theta) + YO + 0.5).toInt()

        return Point(x, y)
    }
}
