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

        lifecycleScope.launch {
            // 10초 동안 로딩 미완료 시 종료
            val job = launch {
                delay(10000)
                Toast.makeText(this@SplashActivity, "오류가 발생하였습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                delay(1000)
                finish()
            }

            delay(3500)

            viewModel.allComplete.observe(this@SplashActivity) { allComoplete ->
                if (allComoplete) {
                    // 통신 완료 시 메인 화면 시작
                    job.cancel()
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                } else {
                    // 서버와 통신 실패 시 종료
                    launch {
                        Toast.makeText(this@SplashActivity, "오류가 발생하였습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
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