package com.recommendmenu.mechulee.view.splash

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.orhanobut.logger.Logger
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.utils.LocationUtils
import com.recommendmenu.mechulee.utils.NetworkUtils
import com.recommendmenu.mechulee.view.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        viewModel = ViewModelProvider(this)[SplashViewModel::class.java]

        // StatusBar 색 변경
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
    }

    override fun onResume() {
        super.onResume()

        checkPermission()
    }


    // 위치 권한 요청 선언
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ||
                    permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // 위치 권한 허용 시
                startApp()
            }

            else -> {
                // 위치 권한 미허용 시 -> 권한 허용 화면으로 이동
                Toast.makeText(this, "위치 권한을 허용해주세요.", Toast.LENGTH_SHORT).show()
                val intent: Intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(
                    Uri.parse("package:${packageName}")
                )
                startActivity(intent)
            }
        }
    }

    // 위치 서비스가 켜져 있는지 확인 후 true / false return
    private fun isLocationAvailable(): Boolean {
        val fineLocationGranted = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseLocationGranted = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return fineLocationGranted || coarseLocationGranted
    }

    // 사용자가 관련 서비스를 키도록 AlertDialog 를 사용하여 유도
    private fun showAlertDialog(
        title: String,
        message: String,
        positiveIntentName: String,
        negativeMessage: String
    ) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("이동") { _, _ ->
                val intent = Intent(positiveIntentName)
                startActivity(intent)
            }
            setNegativeButton("취소") { _, _ ->
                Toast.makeText(this@SplashActivity, negativeMessage, Toast.LENGTH_SHORT).show()
                finish()
            }
            setCancelable(false)
            create()
        }.show()
    }

    private fun checkPermission() {
        if (NetworkUtils.isNetworkAvailable(this)) {
            // 네트워크 (인터넷) 연결 시
            if (isLocationAvailable()) {
                // 위치 권한 허용 상태

                // gps 켜져 있는 지 확인
                val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                ) {
                    // 위치 정보 켜져 있는 상태
                    startApp()
                } else {
                    // 사용자가 위치 서비스를 키도록 AlertDialog 를 사용하여 유도
                    showAlertDialog(
                        "위치 서비스",
                        "앱에서 위치 기능을 사용하려면 위치 서비스를 활성화해야 합니다. 위치 설정으로 이동하시겠습니까?",
                        Settings.ACTION_LOCATION_SOURCE_SETTINGS,
                        "위치 서비스를 활성화하고 시도해주세요."
                    )
                }
            } else {
                // 위치 권한 요청
                locationPermissionRequest.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        } else {
            // 네트워크 미연결 시 -> 사용자가 WIFI 서비스를 키도록 AlertDialog 를 사용하여 유도
            showAlertDialog(
                "네트워크 연결 활성화",
                "앱에서 기능을 사용하려면 네트워크를 연결해야 합니다. 네트워크 설정으로 이동하시겠습니까?",
                Settings.ACTION_WIRELESS_SETTINGS,
                "네트워크를 연결하고 시도해주세요."
            )
        }
    }

    private fun startApp() {
        // 현재 주소 조회하여 반영, onResult : callback 함수
        LocationUtils.getCurrentAddress(this, onResult = { simpleAddress, adminArea ->

            Logger.d(simpleAddress, adminArea)
            // 도/광역시 단위 주소가 없을 경우 앱 종료
            if (adminArea == "") {
                Toast.makeText(this@SplashActivity, "위치 오류가 발생하였습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                finish()
                return@getCurrentAddress
            }

            // 시/도에 따른 날씨 조회
            viewModel.requestWeatherInfo(adminArea)

            // 주변 식당 검색
            viewModel.searchNearByRestaurant(simpleAddress)
        })

        // 3.5 초 이후부터 observe 처리를 위한 비동기 쓰레드
        lifecycleScope.launch {
            // 10초 동안 로딩 미완료 시 종료
//            val job = launch {
//                delay(10000)
//                Toast.makeText(this@SplashActivity, "오류가 발생하였습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
//                delay(1000)
//                finish()
//            }

            delay(3500)

            // 3.5초 후 부터 observe 처리위해 delay(3500) 이후 observe 실행
            viewModel.allComplete.observe(this@SplashActivity) { allComplete ->
                Logger.e("옵저버 확인 메시지")
                Logger.d(allComplete)
                if (allComplete) {
                    // 통신 완료 시 메인 화면 시작
//                    job.cancel()
                    Logger.e("allcomplete 됐다 야호")
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                } else {
                    // 서버와 통신 실패 시 종료

//                        job.cancel()
                    Logger.e("asdfasdfsadfsadf")
                    Toast.makeText(this@SplashActivity, "서버 오류가 발생하였습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()

                    finish()

                }
            }
        }
    }
}