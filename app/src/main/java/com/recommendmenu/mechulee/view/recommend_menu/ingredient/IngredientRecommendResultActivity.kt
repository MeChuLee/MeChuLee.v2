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
import com.recommendmenu.mechulee.model.data.MenuInfo
import com.orhanobut.logger.Logger

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

        val intent = intent
        val resultMenu = intent.getSerializableExtra("object") as MenuInfo?
//        Logger.d("전달받은 객체 $resultMenu")
//        Logger.d("전달받은 이름 ${resultMenu?.name}")
//        Logger.d("전달받은 카테 ${resultMenu?.category}")
//        Logger.d("전달받은 재료 ${resultMenu?.ingredients}")
        
        if (resultMenu != null) {
            viewModel.recommendResult(resultMenu)
            viewModel.showIngredient(resultMenu)
            viewModel.showOthers(resultMenu)

            viewModel.nowResult.observe(this) {
                // 선택된 메뉴 적용
                binding.resultMenuName.text = it.name
            }
            viewModel.ingredientList.observe(this) {
                // 재료 리스트 연결
                val resultIngredientAdapter = ResultIngredientAdapter()
                it.forEach { myIngredient ->
                    resultIngredientAdapter.ingredientList.add(myIngredient)
                }
                binding.ingredientList.apply {
                    adapter = resultIngredientAdapter
                    layoutManager = GridLayoutManager(context, 5)
                    setHasFixedSize(true)
                }
            }
            viewModel.otherList.observe(this) {
                // 비슷한 메뉴 연결
                val resultOtherMenuAdapter = ResultOtherMenuAdapter()
                it.forEach { otherMenu ->
                    resultOtherMenuAdapter.datas.add(otherMenu)
                }
                binding.otherMenuRecycler.apply {
                    setHasFixedSize(true)
                    adapter = resultOtherMenuAdapter
                    layoutManager =
                        LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
                }
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