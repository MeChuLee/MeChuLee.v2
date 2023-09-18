package com.recommendmenu.mechulee.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.recommendmenu.mechulee.model.data.IngredientInfo
import com.recommendmenu.mechulee.model.data.MenuInfo
import com.recommendmenu.mechulee.model.network.ingredient.IngredientDto
import com.recommendmenu.mechulee.model.network.ingredient.IngredientService
import com.recommendmenu.mechulee.model.network.menu.MenuDto
import com.recommendmenu.mechulee.model.network.menu.MenuService
import com.recommendmenu.mechulee.utils.Constants.URL_TYPE_INGREDIENT
import com.recommendmenu.mechulee.utils.Constants.URL_TYPE_MENU
import com.recommendmenu.mechulee.view.menu_list.MenuListViewModel
import com.recommendmenu.mechulee.view.recommend_menu.ingredient.IngredientViewModel
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
    private var totalIngredientList = ArrayList<IngredientInfo>()
    private var ingredientTotalMap = mapOf<String, ArrayList<IngredientInfo>>()

    private var vegetableList = ArrayList<IngredientInfo>()
    private var fruitList = ArrayList<IngredientInfo>()
    private var riceAndNoodleList = ArrayList<IngredientInfo>()
    private var meatList = ArrayList<IngredientInfo>()
    private var fishList = ArrayList<IngredientInfo>()
    private var sauceList = ArrayList<IngredientInfo>()
    private var otherList = ArrayList<IngredientInfo>()
    private var noodleList = ArrayList<IngredientInfo>()

    // 전체 메뉴 리스트 정보를 담고 있는 변수
    private var totalMenuList = ArrayList<MenuInfo>()

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

    // 모든 재료 정보 요청
    fun requestAllIngredient(onResult: () -> Unit) {
        val retrofit = getRetrofitInstance(MY_SERVER_BASE_URL)

        val service = retrofit.create(IngredientService::class.java)

        service.getAllIngredient()
            .enqueue(object : Callback<IngredientDto> {
                override fun onResponse(
                    call: Call<IngredientDto>,
                    response: Response<IngredientDto>
                ) {
                    if (response.isSuccessful.not()) {
                        return
                    }
                    response.body()?.let { ingredientDto ->
                        val tempIngredientList = ingredientDto.ingredientList
                        totalIngredientList = tempIngredientList.toCollection(ArrayList())
                    }
                    totalIngredientList.map {
                        when (it.classification) {
                            "야채" -> vegetableList.add(it)
                            "소스" -> sauceList.add(it)
                            "고기" -> meatList.add(it)
                            "기타" -> otherList.add(it)
                            "생선" -> fishList.add(it)
                            "과일" -> fruitList.add(it)
                            "밥/면" -> riceAndNoodleList.add(it)
                            "면" -> noodleList.add(it)
                            else -> {}
                        }
                    }
                    ingredientTotalMap = mapOf(
                        "야채" to vegetableList,
                        "과일" to fruitList,
                        "밥/면" to riceAndNoodleList,
                        "고기" to meatList,
                        "생선" to fishList,
                        "소스" to sauceList,
                        "기타" to otherList,
                        "면" to noodleList,
                    )

                    onResult()
                }

                override fun onFailure(call: Call<IngredientDto>, t: Throwable) {}
            })
    }

    fun requestAllMenu(onResult: () -> Unit) {
        // retrofit instance 획득
        val retrofit = getRetrofitInstance(MY_SERVER_BASE_URL)

        val service = retrofit.create(MenuService::class.java)

        // retrofit 실행
        service.getAllMenu()
            .enqueue(object : Callback<MenuDto> {
                override fun onResponse(call: Call<MenuDto>, response: Response<MenuDto>) {
                    if (response.isSuccessful.not()) {
                        return
                    }
                    // 성공 시, response.body 에 있는 데이터(응답 데이터) 가져오기
                    response.body()?.let { menuDto ->
                        // 응답으로 온 메뉴 데이터를 viewModel data 에 반영 ( 분류 순 정렬 )
                        val resultMenuList = menuDto.menuList.sortedWith(compareBy { it.category })

                        totalMenuList = resultMenuList.toCollection(ArrayList())

                        onResult()
                    }
                }

                override fun onFailure(call: Call<MenuDto>, t: Throwable) {}
            })
    }
}