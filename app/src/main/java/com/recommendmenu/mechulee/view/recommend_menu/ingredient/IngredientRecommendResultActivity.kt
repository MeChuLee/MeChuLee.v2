package com.recommendmenu.mechulee.view.recommend_menu.ingredient

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.databinding.ActivityIngredientRecommendResultBinding

class IngredientRecommendResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIngredientRecommendResultBinding

    private lateinit var viewModel: ResultViewModel
    private var isLiked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_ingredient_recommend_result)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        viewModel = ViewModelProvider(this)[ResultViewModel::class.java]

        binding.heartIcon.setOnClickListener {
            isLiked = !isLiked
            animateHeart(isLiked)
        }

        viewModel.nowResult.observe(this) {
            // 선택된 메뉴 적용
            binding.resultMenuName.text = viewModel.nowResult.value?.name

            // 재료 리스트 연결
            val resultIngredientAdapter = ResultIngredientAdapter()
            viewModel.ingredientList.value?.forEach {
                resultIngredientAdapter.ingredientList.add(it)
            }
            binding.ingredientList.apply {
                adapter = resultIngredientAdapter
                layoutManager = GridLayoutManager(context, 5)
                setHasFixedSize(true)
            }

            // 비슷한 메뉴 연결
            val resultOtherMenuAdapter = ResultOtherMenuAdapter()
            viewModel.otherList.value?.forEach {
                resultOtherMenuAdapter.datas.add(it)
            }
            binding.otherMenuRecycler.apply {
                setHasFixedSize(true)
                adapter = resultOtherMenuAdapter
                layoutManager =
                    LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
            }
        }
    }

    private fun animateHeart(isLiked: Boolean) {
        if (isLiked) {
            binding.lottieAnimation.setAnimation(R.raw.heart_click)
            binding.lottieAnimation.playAnimation()
            binding.lottieAnimation.visibility = View.VISIBLE
        } else {
            binding.lottieAnimation.visibility = View.INVISIBLE
        }
    }
}