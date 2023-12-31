package com.recommendmenu.mechulee.view.recommend_menu.ai.ingredient_rate

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.databinding.ActivityIngredientRateBinding
import com.recommendmenu.mechulee.model.data.IngredientInfo
import com.recommendmenu.mechulee.proto.ratingDataStore
import com.recommendmenu.mechulee.utils.Constants
import com.recommendmenu.mechulee.utils.Constants.INTENT_NAME_RESULT
import com.recommendmenu.mechulee.view.MainActivity
import com.recommendmenu.mechulee.view.dialog.LoadingDialog
import com.recommendmenu.mechulee.view.recommend_menu.ai.ingredient_rate.adapter.CustomSpinnerAdapter
import com.recommendmenu.mechulee.view.recommend_menu.ai.ingredient_rate.adapter.IngredientRateRecyclerViewAdapter
import com.recommendmenu.mechulee.view.result.menu.MenuResultActivity
import kotlin.properties.Delegates

class IngredientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIngredientRateBinding
    private lateinit var viewModel: IngredientRateViewModel
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var fadeOut: ObjectAnimator

    private var isRated = false

    private lateinit var scrollListener: RecyclerView.OnScrollListener

    // NotifyDataSetChanged가 함수가 리사이클러뷰 전체를 바꾸는 형식인데
    // 안드로이드에서는 전체를 바꾸는 것을 지양하기 때문에
    // Lint로 오류를 무시하는 suppress를 해주어야한다.
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // dataBinding으로 activity_main.xml 가져오기
        binding = DataBindingUtil.setContentView(
            this,
            com.recommendmenu.mechulee.R.layout.activity_ingredient_rate
        )

        binding.scrollUpButton.setOnClickListener {
            binding.ingredientRaterecyclerView.smoothScrollToPosition(0)
        }

        // ViewModelProvider.Factory를 생성하여 ItemViewModel 인스턴스를 생성
        val viewModelFactory =
            com.recommendmenu.mechulee.view.recommend_menu.ai.ingredient_rate.IngredientRateViewModelFactory(
                ratingDataStore
            )

        // ItemViewModelFactory를 provider에 넘겨줌으로서 viewModel에서 DataStore를 접근가능하도록 설정
        viewModel = ViewModelProvider(this, viewModelFactory)[IngredientRateViewModel::class.java]

        val fadeIn = ObjectAnimator.ofFloat(binding.scrollUpButton, "alpha", 0f, 1f).apply {
            duration = 1000 // 애니메이션 지속 시간 설정 (1초)
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    binding.scrollUpButton.isEnabled = true
                }

                override fun onAnimationEnd(animation: Animator) {}

                override fun onAnimationCancel(animation: Animator) {}

                override fun onAnimationRepeat(animation: Animator) {}
            })
        }

        fadeOut = ObjectAnimator.ofFloat(binding.scrollUpButton, "alpha", 1f, 0f).apply {
            duration = 1000 // 애니메이션 지속 시간 설정 (1초)
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {
                    binding.scrollUpButton.isEnabled = false
                }

                override fun onAnimationCancel(animation: Animator) {}

                override fun onAnimationRepeat(animation: Animator) {}
            })
        }

        scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // 스크롤을 위로 올렸을 경우, 첫 번째 항목이 완전히 보이는지 확인 (맨 위까지 스크롤),
                // 버벅거림 방지를 위해 transition 상태가 확인 후,
                // 현재 애니메이션이 진행되고 있지 않다면 motion transition 수행
                if (dy < 0) {
                    fadeOut.start()
                }

                // 스크롤을 아래로 내렸을 경우, 버벅거림 방지를 위해 transition 상태가 확인 후,
                // 현재 애니메이션이 진행되고 있지 않다면 motion transition 수행
                if (dy > 0) {
                    fadeIn.start()
                }
            }
        }

        // 메뉴 카테고리 RecyclerView 초기화
        val ingredientRateRecyclerViewAdapter = IngredientRateRecyclerViewAdapter(object :
            IngredientRateRecyclerViewAdapter.IngredientRateListener {
            // Listener 내부 함수 정의
            override fun changeCurrentItem(itemList: IngredientInfo) {
                // 카테고리 버튼을 클릭하여 현재 카테고리 변경 시
                viewModel.changeItemFromItemList(itemList)
            }

            override fun isRated(isRated: Boolean) {
                this@IngredientActivity.isRated = isRated
            }
        })

        binding.ingredientRaterecyclerView.adapter = ingredientRateRecyclerViewAdapter
        binding.ingredientRaterecyclerView.layoutManager =
            LinearLayoutManager(this) // 리사이클러뷰 레이아웃 매니저 설정
        binding.ingredientRaterecyclerView.addOnScrollListener(scrollListener)
        // menuList 정보 변경 감지 시 RecyclerView 갱신
        viewModel.menuList.observe(this) { newMenuList ->
            ingredientRateRecyclerViewAdapter.itemList =
                newMenuList.mapTo(ArrayList()) { it.copy() }
            ingredientRateRecyclerViewAdapter.notifyDataSetChanged()
        }

        // 스피너 어댑터 설정
        val spinnerData = resources.getStringArray(com.recommendmenu.mechulee.R.array.my_spinners)
        val spinnerAdapter = CustomSpinnerAdapter(this, spinnerData)
        binding.spinner.adapter = spinnerAdapter

        // status Bar색상 설정
        window.statusBarColor =
            ContextCompat.getColor(this, com.recommendmenu.mechulee.R.color.white)

        binding.refresh.setOnClickListener {
            isRated = false // 평가 안됨으로 초기화

            viewModel.refreshMenuList()
        }

        // 추천 버튼 클릭 시
        binding.completeTextView.setOnClickListener {

            if(!isRated && !viewModel.areAllRatingsZero()){ // 평가가 한 번도 되지 않은 경우
                android.app.AlertDialog.Builder(this).apply {
                    setMessage("변경 사항이 없습니다.\n추천 받으시겠습니까?")
                    setPositiveButton("추천") { _, _ ->
                        viewModel.storeRatingDataFromTotalList() // <- 내부에서 getResultMenu()함수를 호출
                        val tempOutput = viewModel.getResultMenuFromServer()
                        if(tempOutput) {
                            // 로딩 화면
                            loadingDialog = LoadingDialog(this@IngredientActivity)
                            loadingDialog.show()
                        } else {
                            Toast.makeText(this@IngredientActivity, "더 많은 재료를 평가해주세요", Toast.LENGTH_SHORT).show()
                        }
                    }
                    setNegativeButton("취소") { _, _ ->
                        //finish()
                    }
                    setCancelable(false)
                    create()
                }.show()
            }else {
                // 여기서 totalList의 정보들을 DataStore에 저장한다.
                viewModel.storeRatingDataFromTotalList() // <- 내부에서 getResultMenu()함수를 호출
                val tempOutput = viewModel.getResultMenuFromServer()
                if(tempOutput) {
                    // 로딩 화면
                    loadingDialog = LoadingDialog(this)
                    loadingDialog.show()
                } else {
                    Toast.makeText(this, "더 많은 재료를 평가해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // viewModel에 있는 resultMenu변경 시 감지 후 Intent로 넘긴다.
        viewModel.resultMenu.observe(this) { resultMenu ->
            loadingDialog.dismiss()

            // 액티비티로 전환하는 Intent 생성
            val intent = Intent(this, MenuResultActivity::class.java)
            intent.putExtra(INTENT_NAME_RESULT, resultMenu) // Intent로 사용할 정보를 옮겨준다.
            startActivity(intent) // 액티비티로 전환
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
                viewModel.showMenuList(selectedItem, searchText)
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
}