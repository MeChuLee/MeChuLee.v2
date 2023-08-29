package com.recommendmenu.mechulee.view.recommend_menu.ai.ingredient_rate

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.recommendmenu.mechulee.view.recommend_menu.ai.ingredient_rate.adapter.CustomSpinnerAdapter
import com.recommendmenu.mechulee.view.recommend_menu.ai.ingredient_rate.adapter.IngredientRateRecyclerViewAdapter
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.databinding.ActivityIngredientRateBinding
import com.recommendmenu.mechulee.proto.ratingDataStore

class IngredientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIngredientRateBinding
    private lateinit var viewModel: IngredientRateViewModel
    private lateinit var ingredientRateRecyclerViewAdapter: IngredientRateRecyclerViewAdapter

    @SuppressLint("NotifyDataSetChanged")
    // NotifyDataSetChanged 이 함수가 리사이클러뷰 전체를 바꾸는 형식인데
    // 안드로이드에서는 전체를 바꾸는 것을 지양하기 때문에
    // Lint로 오류를 무시하는 suppress를 해주어야한다.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // dataBinding으로 activity_main.xml 가져오기
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ingredient_rate)

        // ViewModelProvider.Factory를 생성하여 ItemViewModel 인스턴스를 생성
        val viewModelFactory = ItemViewModelFactory(ratingDataStore)

        // ItemViewModelFactory를 provider에 넘겨줌으로서 viewModel에서 DataStore를 접근가능하도록 설정
        viewModel = ViewModelProvider(this, viewModelFactory)[IngredientRateViewModel::class.java]

        // RecyclerView 초기화
        ingredientRateRecyclerViewAdapter = IngredientRateRecyclerViewAdapter(viewModel) // 초기에 빈 리스트를 넘겨 초기화합니다.

        binding.ingredientRaterecyclerView.adapter = ingredientRateRecyclerViewAdapter
        binding.ingredientRaterecyclerView.layoutManager = LinearLayoutManager(this) // 리사이클러뷰 레이아웃 매니저 설정

        // menuList 정보 변경 감지 시 RecyclerView 갱신
        viewModel.menuList.observe(this) {
            ingredientRateRecyclerViewAdapter.notifyDataSetChanged()
        }

        // 스피너 어댑터 설정
        val spinnerData = resources.getStringArray(R.array.my_spinners)
        val spinnerAdapter = CustomSpinnerAdapter(this, spinnerData)
        binding.spinner.adapter = spinnerAdapter

        // status Bar색상 설정
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        // 완료 버튼 클릭 시
        binding.completeTextView.setOnClickListener {

            // 여기서 totalList의 정보들을 DataStore에 저장한다.
            viewModel.storeRatingDataFromTotalList()

            // DataStore에 저장된 rating값들을 보여준다
            viewModel.showRatingDataStrore()

            // 현재 menuList정보 출력해서 확인
            viewModel.menuList.value?.forEach { ingredientInfo ->
                println("Ingredient: ${ingredientInfo.title}, Rating: ${ingredientInfo.rating}")
            }

            finish()
        }

        // text & spinner 요소 같이 검색하도록 돕는 클래스 선언
        val spinnerEditTextSearch = SpinnerEditTextSearch(this, binding, viewModel)

        spinnerEditTextSearch.initEditText()
        spinnerEditTextSearch.initSpinnerIngredientList()
    }

    override fun onDestroy() {
        // binding을 해제하는 코드
        // 액티비티나 프래그먼트가 파괴되어도 뷰와 관련된 리소스가
        // 적절하게 해제될 수 있다.
        binding.unbind()
        // UI업데이트가 발생하지 않는다는데,
        // 그럼 새로운 view를 계속 업데이트하는 식인 리사이클러뷰에는
        // 쓰면 안되는 건가?
        super.onDestroy()
    }

}


