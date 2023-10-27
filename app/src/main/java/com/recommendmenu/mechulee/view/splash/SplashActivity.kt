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
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.utils.LocationUtils
import com.recommendmenu.mechulee.utils.NetworkUtils
import com.recommendmenu.mechulee.view.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        viewModel = ViewModelProvider(this)[SplashViewModel::class.java]

        // StatusBar 색 변경
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        viewModel.allComplete.observe(this@SplashActivity) { allComplete ->
            if (allComplete) {
                // 통신 완료 시 메인 화면 시작
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            } else {
                // 서버와 통신 실패 시 종료
                showRetryAlertDialog("서버 오류가 발생하였습니다.\n잠시 후 다시 시도해주세요.")
            }
        }
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

    private fun showRetryAlertDialog(inputString: String) {
        AlertDialog.Builder(this).apply {
            setMessage(inputString)
            setPositiveButton("다시 시도") { _, _ ->
                viewModel.checkArray = arrayOf(
                    SplashViewModel.NULL_POINT,
                    SplashViewModel.NULL_POINT,
                    SplashViewModel.NULL_POINT,
                    SplashViewModel.NULL_POINT
                )
                startApp()
            }
            setCancelable(false)
            setOnKeyListener { dialogInterface, keyCode, _ ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialogInterface.dismiss()
                    finish()
                    true
                } else {
                    false
                }
            }
            create()
        }.show()
    }

    private fun startApp() {
        // 현재 주소 조회하여 반영, onResult : callback 함수
        LocationUtils.getCurrentAddress(this, onResult = { simpleAddress, adminArea ->
            if (adminArea == "") {
                // 도/광역시 단위 주소가 없을 경우 앱 종료
                showRetryAlertDialog("위치를 조회할 수 없습니다.\n다시 시도해주세요.")
                return@getCurrentAddress
            }

            // 시/도에 따른 날씨 조회
            viewModel.requestWeatherInfo(adminArea)

            // 주변 식당 검색
            viewModel.searchNearByRestaurant(simpleAddress)
        })
    }
}