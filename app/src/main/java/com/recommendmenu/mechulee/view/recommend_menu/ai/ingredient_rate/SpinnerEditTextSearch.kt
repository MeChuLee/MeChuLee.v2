package com.recommendmenu.mechulee.view.recommend_menu.ai.ingredient_rate

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import com.recommendmenu.mechulee.databinding.ActivityIngredientRateBinding
import java.lang.ref.WeakReference

class SpinnerEditTextSearch(
    context: Context,
    private val binding: ActivityIngredientRateBinding,
    private var viewModel: IngredientRateViewModel,
) {

    // context를 약한 참조를 통해서 키보드 내리는 기능에서 context를 사용할 수 있게 만듬
    // 강한 참조로 구현 시 코드 오류
    private val contextRef: WeakReference<Context> = WeakReference(context)

    // 기존에 공유가 안되던 스피너 아이템을 클래스로 만듦으로서 다른 함수에서도 사용가능해짐
    private var selectedItem: String = ""

    fun initSpinnerIngredientList() {
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedItem = parent?.getItemAtPosition(position).toString()
                viewModel.showMenuList(selectedItem, binding.menuSearchEditText.text.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    fun initEditText() {
        val context = contextRef.get()

        // context가 null이 아닐 시
        context?.let {
            // 검색창 입력 시 마다 검색 기능 수행
            binding.menuSearchEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    viewModel.showMenuList(selectedItem, binding.menuSearchEditText.text.toString())
                }
            })
            binding.menuSearchEditText.setOnEditorActionListener { _, actionId, _ ->
                var handled = false
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 키보드 내리기
                    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(binding.menuSearchEditText.windowToken, 0)
                    handled = true
                }
                handled
            }
        }
    }
}