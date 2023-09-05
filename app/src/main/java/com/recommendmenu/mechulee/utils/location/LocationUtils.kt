package com.recommendmenu.mechulee.utils.location

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
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale

object LocationUtils {

    // 현재 주소 가져오기 (activity 가 필요하므로, Activity 나 Fragment 에서 수행)
    fun getCurrentAddress(activity : FragmentActivity, onResult: (String) -> Unit) {
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

        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val geocoder = Geocoder(activity, Locale.KOREA)

        val locationListener = object : LocationListener {
            override fun onLocationChanged(currentLocation: Location) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    geocoder.getFromLocation(currentLocation.latitude, currentLocation.longitude, 3) { addresses ->
                        addresses.forEach { address ->
                            Logger.d(address.getAddressLine(0))
                        }

                        onResult(addresses[2].getAddressLine(0))
                    }
                } else {
                    // API 31 이하 deprecated 된 함수 사용 -> UI 쓰레드 차단 방지를 위해 Coroutine 사용
                    activity.lifecycleScope.launch(Dispatchers.IO) {
                        try {
                            val addresses = geocoder.getFromLocation(currentLocation.latitude, currentLocation.longitude, 3) as List<Address>

                            withContext(Dispatchers.Main) {
                                onResult(addresses[0].getAddressLine(0))
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }

                // 반복적 위치 업데이트를 막기 위해 업데이트 삭제
                locationManager.removeUpdates(this)
            }
        }

        // 위치 정보 요청
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10.0f, locationListener)
    }
}