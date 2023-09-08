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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.recommendmenu.mechulee.databinding.ActivityIngredientRateBinding
import com.recommendmenu.mechulee.model.data.IngredientInfo
import com.recommendmenu.mechulee.proto.ratingDataStore
import com.recommendmenu.mechulee.view.recommend_menu.ai.ingredient_rate.adapter.CustomSpinnerAdapter
import com.recommendmenu.mechulee.view.recommend_menu.ai.ingredient_rate.adapter.IngredientRateRecyclerViewAdapter


class IngredientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIngredientRateBinding
    private lateinit var viewModel: IngredientRateViewModel

    @SuppressLint("NotifyDataSetChanged")
    // NotifyDataSetChanged가 함수가 리사이클러뷰 전체를 바꾸는 형식인데
    // 안드로이드에서는 전체를 바꾸는 것을 지양하기 때문에
    // Lint로 오류를 무시하는 suppress를 해주어야한다.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // dataBinding으로 activity_main.xml 가져오기
        binding = DataBindingUtil.setContentView(
            this,
            com.recommendmenu.mechulee.R.layout.activity_ingredient_rate
        )

        // ViewModelProvider.Factory를 생성하여 ItemViewModel 인스턴스를 생성
        val viewModelFactory = IngredientRateViewModelFactory(ratingDataStore)

        // ItemViewModelFactory를 provider에 넘겨줌으로서 viewModel에서 DataStore를 접근가능하도록 설정
        viewModel = ViewModelProvider(this, viewModelFactory)[IngredientRateViewModel::class.java]

        // 메뉴 카테고리 RecyclerView 초기화
        val ingredientRateRecyclerViewAdapter = IngredientRateRecyclerViewAdapter(object :
            IngredientRateRecyclerViewAdapter.IngredientRateListener {
            // Listener 내부 함수 정의
            override fun changeCurrentItem(item: IngredientInfo) {
                // 카테고리 버튼을 클릭하여 현재 카테고리 변경 시
                viewModel.changeItemFromItemList(item)
            }
        })

        binding.ingredientRaterecyclerView.adapter = ingredientRateRecyclerViewAdapter
        binding.ingredientRaterecyclerView.layoutManager =
            LinearLayoutManager(this) // 리사이클러뷰 레이아웃 매니저 설정

        // menuList 정보 변경 감지 시 RecyclerView 갱신
        viewModel.menuList.observe(this) { newMenuList ->
            ingredientRateRecyclerViewAdapter.itemList = newMenuList.mapTo(ArrayList()) { it.copy() }
            ingredientRateRecyclerViewAdapter.notifyDataSetChanged()
        }

        // 스피너 어댑터 설정
        val spinnerData = resources.getStringArray(com.recommendmenu.mechulee.R.array.my_spinners)
        val spinnerAdapter = CustomSpinnerAdapter(this, spinnerData)
        binding.spinner.adapter = spinnerAdapter

        // status Bar색상 설정
        window.statusBarColor =
            ContextCompat.getColor(this, com.recommendmenu.mechulee.R.color.white)

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

        initEditText()
        initSpinnerIngredientList()

    }


    private fun initSpinnerIngredientList() {
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = binding.spinner.selectedItem.toString()
                val searchText = binding.menuSearchEditText.text.toString()

                viewModel.changeMenuListToTotalList()
                viewModel.showMenuList(selectedItem,searchText)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    private fun initEditText() {
        // 검색창 입력 시 마다 검색 기능 수행
        binding.menuSearchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.showMenuList(
                    binding.spinner.selectedItem.toString(),
                    binding.menuSearchEditText.text.toString()
                )
            }
        })
        // 검색창 키보드 '검색' 아이콘 클릭 시 키보드 내리기
        binding.menuSearchEditText.setOnEditorActionListener { _, actionId, _ ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(
                    binding.menuSearchEditText.windowToken,
                    0
                )
                handled = true
            }
            handled
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        AlertDialog.Builder(this).apply {
            setTitle("평가 점수 저장")
            setMessage("평가 점수를 저장하시겠습니까?")
            setPositiveButton("확인") { _, _ ->
                // 여기서 totalList의 정보들을 DataStore에 저장한다.
                viewModel.storeRatingDataFromTotalList()
                finish()
            }
            setNegativeButton("취소") { _, _ ->
                finish()
            }
            create()
        }.show()
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


