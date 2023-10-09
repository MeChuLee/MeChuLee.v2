package com.recommendmenu.mechulee.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.orhanobut.logger.Logger
import com.recommendmenu.mechulee.BuildConfig
import com.recommendmenu.mechulee.model.data.IngredientInfo
import com.recommendmenu.mechulee.model.data.MenuInfo
import com.recommendmenu.mechulee.model.data.WeatherInfo
import com.recommendmenu.mechulee.model.network.ingredient.IngredientDto
import com.recommendmenu.mechulee.model.network.ingredient.IngredientService
import com.recommendmenu.mechulee.model.network.menu.MenuDto
import com.recommendmenu.mechulee.model.network.menu.MenuService
import com.recommendmenu.mechulee.model.network.search.Item
import com.recommendmenu.mechulee.model.network.search.SearchDto
import com.recommendmenu.mechulee.model.network.search.SearchService
import com.recommendmenu.mechulee.model.network.weather_info.WeatherInfoDto
import com.recommendmenu.mechulee.model.network.weather_info.WeatherInfoService
import com.recommendmenu.mechulee.utils.Constants.URL_TYPE_INGREDIENT
import com.recommendmenu.mechulee.utils.Constants.URL_TYPE_MENU
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkUtils {
    // 기기 : http://192.168.0.72:8000/
    // 에뮬레이터 : http://10.0.2.2:8000/
    const val MY_SERVER_BASE_URL = "http://10.0.2.2:8000/"
    const val NAVER_SEARCH_BASE_URL = "https://openapi.naver.com/"
    const val WEATHER_BASE_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/"

    private const val ADD_MENU_URL = "static/menu/"
    private const val ADD_INGREDIENT_URL = "static/ingredient/"

    // 여기에 서버에서 가져온 재료들을 우선 저장 후 Map에 분류에 맞게 저장해야함
    var totalIngredientList = ArrayList<IngredientInfo>()

    // 전체 메뉴 리스트
    var totalMenuList = ArrayList<MenuInfo>()


    // 주변 식당 리스트
    val restaurantList = ArrayList<Item>()

    // 오늘의 추천 메뉴 리스트
    val todayMenuList = ArrayList<MenuInfo>()

    // 현재 날씨 조회 정보
    var weatherInfo = WeatherInfo("","","")

    // 현재 기기에 인터넷 연결 여부 확인
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true // Mobile data connection.
            else -> false
        }
    }

    // return Retrofit instance
    fun getRetrofitInstance(url: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Glide 를 사용하여 이미지 로드
    fun loadImage(context: Context, imageView: ImageView, url: String, urlType: Int) {
        var loadUrl = MY_SERVER_BASE_URL

        when (urlType) {
            URL_TYPE_MENU -> loadUrl += ADD_MENU_URL
            URL_TYPE_INGREDIENT -> loadUrl += ADD_INGREDIENT_URL
        }

        Glide.with(context)
            .load(loadUrl + url)
            .into(imageView)
    }

    // 서버에 모든 재료 정보 요청
    fun requestAllIngredient(onResult: (isSuccess: Boolean) -> Unit) {
        val retrofit = getRetrofitInstance(MY_SERVER_BASE_URL)

        val service = retrofit.create(IngredientService::class.java)

        service.getAllIngredient()
            .enqueue(object : Callback<IngredientDto> {
                override fun onResponse(
                    call: Call<IngredientDto>,
                    response: Response<IngredientDto>
                ) {
                    if (response.isSuccessful.not()) {
                        onResult(false)
                        return
                    }
                    response.body()?.let { ingredientDto ->
                        val tempIngredientList = ingredientDto.ingredientList
                        totalIngredientList = tempIngredientList.toCollection(ArrayList())
                    }

                    onResult(true)
                }

                override fun onFailure(call: Call<IngredientDto>, t: Throwable) {
                    onResult(false)
                }
            })
    }

    // 서버에 모든 메뉴 정보 요청
    fun requestAllMenu(onResult: (isSuccess: Boolean) -> Unit) {
        // retrofit instance 획득
        val retrofit = getRetrofitInstance(MY_SERVER_BASE_URL)

        val service = retrofit.create(MenuService::class.java)

        // retrofit 실행
        service.getAllMenu()
            .enqueue(object : Callback<MenuDto> {
                override fun onResponse(call: Call<MenuDto>, response: Response<MenuDto>) {
                    if (response.isSuccessful.not()) {
                        onResult(false)
                        return
                    }
                    // 성공 시, response.body 에 있는 데이터(응답 데이터) 가져오기
                    response.body()?.let { menuDto ->
                        // 응답으로 온 메뉴 데이터를 viewModel data 에 반영 ( 분류 순 정렬 )
                        val resultMenuList = menuDto.menuList.sortedWith(compareBy { it.category })

                        totalMenuList = resultMenuList.toCollection(ArrayList())

                        onResult(true)
                    }
                }

                override fun onFailure(call: Call<MenuDto>, t: Throwable) {
                    onResult(false)
                }
            })
    }

    // 현재 주소 기준 주변 식당 리스트 요청 후 저장
    fun searchNearByRestaurant(address: String, onResult: (isSuccess: Boolean) -> Unit) {
        val retrofit = getRetrofitInstance(NAVER_SEARCH_BASE_URL)
        val service = retrofit.create(SearchService::class.java)

        // retrofit 실행
        service.getSearchRestaurant(
            BuildConfig.NAVER_CLIENT_ID,
            BuildConfig.NAVER_CLIENT_SECRET,
            "$address 맛집",
            "5",
            "1",
            "random"
        ).enqueue(object : Callback<SearchDto> {
            override fun onResponse(call: Call<SearchDto>, response: Response<SearchDto>) {
                if (response.isSuccessful.not()) {
                    Logger.e("not isSuccessful")
                    return
                }

                response.body()?.let { searchDto ->
                    restaurantList.addAll(searchDto.items)
                    onResult(true)
                }
            }

            override fun onFailure(call: Call<SearchDto>, t: Throwable) {
                Logger.e(t.message.toString())
                onResult(false)
            }
        })
    }

    fun readTodayMenuListWithRetrofit(onResult: (isSuccess: Boolean) -> Unit) {
        val retrofit = getRetrofitInstance(MY_SERVER_BASE_URL)
        val service = retrofit.create(MenuService::class.java)

        service.getRecommendToday()
            .enqueue(object : Callback<MenuDto> {
                override fun onResponse(call: Call<MenuDto>, response: Response<MenuDto>) {
                    if (response.isSuccessful.not()) {
                        return
                    }
                    response.body()?.let { menuDto ->
                        todayMenuList.addAll(menuDto.menuList)
                        onResult(true)
                    }
                }

                override fun onFailure(call: Call<MenuDto>, t: Throwable) {
                    onResult(false)
                }
            })
    }

    fun requestWeatherInfo(adminArea: String, onResult: (isSuccess: Boolean) -> Unit) {
        val retrofit = getRetrofitInstance(MY_SERVER_BASE_URL)
        val service = retrofit.create(WeatherInfoService::class.java)

        service.sendAdminArea(adminArea)
            .enqueue(object : Callback<WeatherInfoDto> {
                override fun onResponse(call: Call<WeatherInfoDto>, response: Response<WeatherInfoDto>) {
                    if (response.isSuccessful.not()) {
                        return
                    }
                    response.body()?.let { weatherInfoDto ->
                        weatherInfo = weatherInfoDto.weatherInfo

                        onResult(true)
                    }
                }

                override fun onFailure(call: Call<WeatherInfoDto>, t: Throwable) {
                    onResult(false)
                }
            })
    }
}