package com.recommendmenu.mechulee.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale

object LocationUtils {

    // 간단하게 표현한 주소 저장을 위한 변수 (처음 SplashActivity 에서 조회 후 저장)
    var simpleAddress = ""

    // 현재 위치 좌표로 가져오기 -> 인자에 파라미터로 콜백 함수 실행
    fun getLocationGPS(activity: FragmentActivity, onResultLocation: (Double, Double) -> Unit) {
        // 권한 미허용 시 return
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val locationListener = object : LocationListener {
            override fun onLocationChanged(currentLocation: Location) {

                // CallBack 함수 실행
                onResultLocation(currentLocation.latitude, currentLocation.longitude)

                // 반복적 위치 업데이트를 막기 위해 업데이트 삭제
                locationManager.removeUpdates(this)
            }
        }

//        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        // 위치 정보 요청
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            10000,
            10.0f,
            locationListener
        )
//        if (location == null) {
//
//        } else {
//            // 최근 접근한 위치가 있을 경우 그 위치 좌표 return
//            onResultLocation(location.latitude, location.longitude)
//        }
    }

    // 현재 주소 가져오기 (activity 가 필요하므로, Activity 나 Fragment 에서 수행)
    fun getCurrentAddress(activity: FragmentActivity, onResult: (String, String) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 권한 미허용 시 return
            return
        }

        val geocoder = Geocoder(activity, Locale.KOREA)

        getLocationGPS(activity, onResultLocation = { latitude, longitude ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocation(latitude, longitude, 3) { addresses ->
                    val simpleAddress = getSimpleAddress(addresses)

                    // 주소 조회가 가능할 경우만 UI 업데이트 실행
                    if (simpleAddress != "") {
                        this@LocationUtils.simpleAddress = simpleAddress
                        onResult(simpleAddress, addresses[0].adminArea)
                    }
                }
            } else {
                // API 31 이하 deprecated 된 함수 사용 -> UI 쓰레드 차단 방지를 위해 Coroutine 사용
                activity.lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        val addresses =
                            geocoder.getFromLocation(latitude, longitude, 3) as List<Address>

                        val simpleAddress = getSimpleAddress(addresses)

                        // 주소 조회가 가능할 경우만 UI 업데이트 실행
                        if (simpleAddress != "") {
                            this@LocationUtils.simpleAddress = simpleAddress
                            withContext(Dispatchers.Main) {
                                onResult(simpleAddress, addresses[0].adminArea)
                            }
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        })
    }

    private fun getSimpleAddress(addresses: List<Address>): String {
        addresses.forEach { address ->
            return if (address.locality == null) {
                // 광역시
                val adminArea = address.adminArea
                val subLocality = address.subLocality
                val thoroughfare = address.thoroughfare

                if (subLocality == null && thoroughfare == null) {
                    adminArea
                } else if (subLocality == null && thoroughfare != null) {
                    "$adminArea $thoroughfare"
                } else if (subLocality != null && thoroughfare == null) {
                    "$adminArea $subLocality"
                } else {
                    "$adminArea $subLocality $thoroughfare"
                }
            } else {
                // 도
                val adminArea = address.adminArea
                val locality = address.locality
                val thoroughfare = address.thoroughfare

                if (locality == null && thoroughfare == null) {
                    adminArea
                } else if (locality == null && thoroughfare != null) {
                    "$adminArea $thoroughfare"
                } else if (locality != null && thoroughfare == null) {
                    "$adminArea $locality"
                } else {
                    "$adminArea $locality $thoroughfare"
                }
            }
        }

        return ""
    }
}