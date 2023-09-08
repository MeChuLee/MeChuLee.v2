package com.recommendmenu.mechulee.view.recommend_menu.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.databinding.ActivityWebviewMapBinding
import com.recommendmenu.mechulee.utils.constant.Constants
import java.net.URLEncoder

class WebViewMapActivity: AppCompatActivity() {

    private lateinit var binding: ActivityWebviewMapBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_webview_map)

        val query = intent.getStringExtra(Constants.INTENT_NAME_LOCATION) + " 맛집"
        val encodedQuery = URLEncoder.encode(query, "UTF-8")

        // WebView에서 발생하는 주요 이벤트를 처리
        // 페이지 로딩 시작/완료 시점 등의 상태 변경에 대한 콜백 함수를 제공
        binding.webView.webViewClient = WebViewClient()

        // WebView 에서 발생하는 다양한 이벤트 처리
        // JavaScript alert 메시지 처리, 진행 상태 표시 등을 담당
        binding.webView.webChromeClient = WebChromeClient()

        // 전체 화면에 맞게 로드
        binding.webView.settings.loadWithOverviewMode = true

        // HTML meta tag 에서 정의한 viewport 값을 사용
        // 모바일 웹페이지는 이 viewport 값을 통해 가장 적절한 레이아웃을 선택
        binding.webView.settings.useWideViewPort = true

        // javaScript 사용 가능하도록 설정
        binding.webView.settings.javaScriptEnabled = true

        // DOM Storage를 활성화 -> 이 설정을 해주어야 네이버 맵 사이트에서 별도 검색 가능
        binding.webView.settings.domStorageEnabled = true

        binding.webView.loadUrl("https://m.map.naver.com/search2/search.naver?query=${encodedQuery}&sm=hty&style=v5")

        window.statusBarColor = ContextCompat.getColor(this, R.color.green)
    }
}