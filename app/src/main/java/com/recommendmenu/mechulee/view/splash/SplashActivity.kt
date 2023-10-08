package com.recommendmenu.mechulee.view.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.utils.LocationUtils
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

        // 현재 주소 조회하여 반영, onResult : callback 함수
        LocationUtils.getCurrentAddress(this, onResult = { simpleAddress, adminArea ->
            // 도/광역시 단위 주소가 없을 경우 앱 종료
            if (adminArea == "") finish()

            // 시/도에 따른 날씨 조회
            viewModel.requestWeatherInfo(adminArea)

            // 주변 식당 검색
            viewModel.searchNearByRestaurant(simpleAddress)
        })

        // 3.5 초 이후부터 observe 처리를 위한 비동기 쓰레드
        lifecycleScope.launch {
            // 10초 동안 로딩 미완료 시 종료
            val job = launch {
                delay(10000)
                Toast.makeText(this@SplashActivity, "오류가 발생하였습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                delay(1000)
                finish()
            }

            delay(3500)

            // 3.5초 후 부터 observe 처리위해 delay(3500) 이후 observe 실행
            viewModel.allComplete.observe(this@SplashActivity) { allComplete ->
                if (allComplete) {
                    // 통신 완료 시 메인 화면 시작
                    job.cancel()
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                } else {
                    // 서버와 통신 실패 시 종료
                    launch {
                        job.cancel()
                        Toast.makeText(this@SplashActivity, "서버 오류가 발생하였습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                        delay(1000)
                        finish()
                    }
                }
            }
        }

        // StatusBar 색 변경
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
    }
}