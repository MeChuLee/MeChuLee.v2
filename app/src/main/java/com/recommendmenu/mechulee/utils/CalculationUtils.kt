package com.recommendmenu.mechulee.utils

import android.graphics.Point
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.tan

object CalculationUtils {
    private const val RE = 6371.00877     // 지구 반경(km)
    private const val GRID = 5.0          // 격자 간격(km)
    private const val SLAT1 = 30.0        // 투영 위도1(degree)
    private const val SLAT2 = 60.0        // 투영 위도2(degree)
    private const val OLON = 126.0        // 기준점 경도(degree)
    private const val OLAT = 38.0         // 기준점 위도(degree)
    private const val XO = 43             // 기준점 X좌표(GRID)
    private const val YO = 136            // 기준점 Y좌표(GRID)
    private const val DEGRAD = Math.PI / 180.0

    fun getBaseTime(h: String, m: String): String {

        // 45분 전이면
        val result: String = if (m.toInt() < 46) {
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
    // 변수들 상수처리하고
    fun dfsXyConv(v1: Int, v2: Int): Point {
        val re = RE / GRID
        val slat1 = SLAT1 * DEGRAD
        val slat2 = SLAT2 * DEGRAD
        val olon = OLON * DEGRAD
        val olat = OLAT * DEGRAD

        var sn = tan(Math.PI * 0.25 + slat2 * 0.5) / tan(Math.PI * 0.25 + slat1 * 0.5)
        sn = ln(cos(slat1) / cos(slat2)) / ln(sn)
        var sf = tan(Math.PI * 0.25 + slat1 * 0.5)
        sf = sf.pow(sn) * cos(slat1) / sn
        var ro = tan(Math.PI * 0.25 + olat * 0.5)
        ro = re * sf / ro.pow(sn)

        var ra = tan(Math.PI * 0.25 + (v1) * DEGRAD * 0.5)
        ra = re * sf / ra.pow(sn)
        var theta = v2 * DEGRAD - olon
        if (theta > Math.PI) theta -= 2.0 * Math.PI
        if (theta < -Math.PI) theta += 2.0 * Math.PI
        theta *= sn

        val x = (ra * sin(theta) + XO + 0.5).toInt()
        val y = (ro - ra * cos(theta) + YO + 0.5).toInt()

        return Point(x, y)
    }
}