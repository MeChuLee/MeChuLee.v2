package com.recommendmenu.mechulee.utils.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.recommendmenu.mechulee.utils.constant.Constants.URL_TYPE_INGREDIENT
import com.recommendmenu.mechulee.utils.constant.Constants.URL_TYPE_MENU
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkUtils {
    // 기기 : http://192.168.0.72:8000/
    // 에뮬레이터 : http://10.0.2.2:8000/
    private const val BASE_URL = "http://10.0.2.2:8000/"
    private const val ADD_MENU_URL = "static/menu/"
    private const val ADD_INGREDIENT_URL = "static/ingredient/"

    // 현재 기기에 인터넷 연결 여부 확인
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true // Mobile data connection.
            else -> false
        }
    }

    // return Retrofit instance
    fun getRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Glide 를 사용하여 이미지 로드
    fun loadImage(context: Context, imageView: ImageView, url: String, urlType: Int) {
        var loadUrl = BASE_URL

        when (urlType) {
            URL_TYPE_MENU -> loadUrl += ADD_MENU_URL
            URL_TYPE_INGREDIENT -> loadUrl += ADD_INGREDIENT_URL
        }

        Glide.with(context)
            .load(loadUrl + url)
            .into(imageView)
    }
}