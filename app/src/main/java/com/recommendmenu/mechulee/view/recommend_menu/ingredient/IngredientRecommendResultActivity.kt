package com.recommendmenu.mechulee.view.recommend_menu.ingredient

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.databinding.ActivityIngredientRecommendResultBinding

class IngredientRecommendResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIngredientRecommendResultBinding

    // TODO 결과용 새로운 ViewModel 만들기
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
        // 얻은 결과 getResult의 값으로 넣어주기
        val nowResult = viewModel.getResult("햄버거")
        binding.resultMenuName.setText(nowResult?.name)
        binding.ingredientList.setText(nowResult?.ingredients)

        // 비슷한 메뉴는 일단 result의 category와 일치하는 값들을 가져오는 걸로
        val resultAdapter = ResultAdapter()
        viewModel.recommendOthers(nowResult?.category, nowResult?.name)?.forEach {
            resultAdapter.datas.add(it.name)
        }
        binding.otherMenuRecycler.apply {
            setHasFixedSize(true)
            adapter = resultAdapter
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
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