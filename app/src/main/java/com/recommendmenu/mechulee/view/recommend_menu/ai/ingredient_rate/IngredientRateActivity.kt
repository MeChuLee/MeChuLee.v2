package com.recommendmenu.mechulee.view.recommend_menu.ai.ingredient_rate

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.recommendmenu.mechulee.view.recommend_menu.ai.ingredient_rate.adapter.CustomSpinnerAdapter
import com.recommendmenu.mechulee.view.recommend_menu.ai.ingredient_rate.adapter.IngredientRateRecyclerViewAdapter
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.RatingData
import com.recommendmenu.mechulee.databinding.ActivityIngredientRateBinding
import com.recommendmenu.mechulee.proto.ratingDataStore

class ItemViewModelFactory(private val dataStore: DataStore<RatingData>) : ViewModelProvider.Factory {
     override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IngredientRateViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return IngredientRateViewModel(dataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
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
        viewModel = ViewModelProvider(this, viewModelFactory)[IngredientRateViewModel::class.java]
        // viewModel 인스턴스를 생성
//        viewModel = ViewModelProvider(this)[ItemViewModel::class.java]



        // Context가 있는 곳에서만 선언가능한건가? 맞다.
        val dataStore = ratingDataStore


        // RecyclerView 초기화
        ingredientRateRecyclerViewAdapter = IngredientRateRecyclerViewAdapter() // 초기에 빈 리스트를 넘겨 초기화합니다.
        binding.recyclerView.adapter = ingredientRateRecyclerViewAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this) // 리사이클러뷰 레이아웃 매니저 설정

        viewModel.menuList.observe(this) { newMenuList ->
        // menuList 정보 변경 감지 시 RecyclerView 갱신
            ingredientRateRecyclerViewAdapter.itemList = newMenuList
            ingredientRateRecyclerViewAdapter.notifyDataSetChanged()
        }

        // 스피너 어댑터 설정
        val spinnerData = resources.getStringArray(R.array.my_spinners)
        val spinnerAdapter = CustomSpinnerAdapter(this, spinnerData)
        binding.spinner.adapter = spinnerAdapter

        // status Bar색상 설정
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        // 완료 textView클릭 시 ripple효과 주기
        binding.completeTextView.setOnClickListener {
            // 클릭 시 실행할 동작 -> 평가한 쟤료들의 점수기록을 가지고
            // menuList의 정보들을 DataStore에 저장
            viewModel.showRatingDataStrore(dataStore)

            // 여기서 menuList의 정보들을 DataStore에 저장한다.
            viewModel.storeRatingDataFromMenuList(dataStore)

            viewModel.showRatingDataStrore(dataStore)
            // 추천 결과 화면을 띄우는 동작 실행
            viewModel.menuList.value?.forEach { ingredientInfo ->
                println("Ingredient: ${ingredientInfo.title}, Rating: ${ingredientInfo.rating}")
            }

            finish()
        }

        initEditText()
        initSpinnerIngredientList()
    }

    private fun initEditText() {
        // 검색창 입력 시 마다 검색 기능 수행
        binding.menuSearchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.searchMenuList(binding.menuSearchEditText.text.toString())
            }
        })
        binding.menuSearchEditText.setOnEditorActionListener { _, actionId, _ ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // 키보드 내리기
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.menuSearchEditText.windowToken, 0)
                handled = true
            }
            handled
        }
    }

    // 각 스피너에 따라 나오는 리스트 변경
    private fun initSpinnerIngredientList(){
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) { // 선택된 아이템에 대한 효과를 적용하는 코드 작성
                val selectedItem = parent?.getItemAtPosition(position).toString()

                viewModel.showMenuList(selectedItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
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


