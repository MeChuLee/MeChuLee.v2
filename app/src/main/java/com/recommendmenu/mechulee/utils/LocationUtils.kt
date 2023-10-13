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
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.io.IOException
import java.util.Locale

object LocationUtils {
    // 간단하게 표현한 주소 저장을 위한 변수 (처음 SplashActivity 에서 조회 후 저장)
    var simpleAddress = ""

    // Coroutine 사용 -> 새로운 위치 정보 조회 후 return (5초 안에 조회 불가 시 최근에 조회한 위치 정보 return)
    private fun getLocationUpdatesAsync(
        activity: FragmentActivity,
        locationManager: LocationManager,
        provider: String
    ): Deferred<Location?> {
        return activity.lifecycleScope.async {
            val location = withTimeoutOrNull(5000) { // 5초 후에 타임아웃
                suspendCancellableCoroutine<Location?> { continuation ->
                    val locationListener = object : LocationListener {
                        override fun onLocationChanged(location: Location) {
                            continuation.resumeWith(Result.success(location))
                            locationManager.removeUpdates(this)
                        }
                    }

                    continuation.invokeOnCancellation {
                        locationManager.removeUpdates(locationListener)
                    }

                    if (ActivityCompat.checkSelfPermission(
                            activity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            activity,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return@suspendCancellableCoroutine
                    }

                    // 새로운 위치 조회
                    locationManager.requestLocationUpdates(provider, 10000, 10f, locationListener)
                }
            }

            // 타임아웃 발생 시 마지막 위치 가져오기
            location ?: run {
                val lastKnownLocation = locationManager.getLastKnownLocation(provider)
                lastKnownLocation
            }
        }
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

        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var provider = LocationManager.GPS_PROVIDER

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER
        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER
        }

        activity.lifecycleScope.launch(Dispatchers.Main) {
            val location = getLocationUpdatesAsync(activity, locationManager, provider).await()

            if (location == null) {
                // 위치 값이 null 일 경우, activity 에서 종료할 수 있게 "", "" 로 onResult() 실행
                onResult("", "")
            } else {
                // 위치 값이 존재할 경우
                val latitude = location.latitude
                val longitude = location.longitude

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
            }
        }
    }

    // 시 구 동 형태로 주소 변경 후 return (조회 불가능 데이터 제외)
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