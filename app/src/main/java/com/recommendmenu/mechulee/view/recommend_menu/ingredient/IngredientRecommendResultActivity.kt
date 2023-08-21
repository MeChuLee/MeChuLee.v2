package com.recommendmenu.mechulee.view.recommend_menu.ingredient

import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.recommendmenu.mechulee.R

class IngredientRecommendResultActivity : AppCompatActivity() {
    private lateinit var viewModel: IngredientViewModel

    private lateinit var heartIcon: ImageView
    private lateinit var resultOtherRecyclerView: RecyclerView
    private lateinit var lottieAnimationView: LottieAnimationView
    private var isLiked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingredient_recommend_result)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        viewModel = ViewModelProvider(this)[IngredientViewModel::class.java]

        heartIcon = findViewById(R.id.heart_icon)
        lottieAnimationView = findViewById(R.id.lottie_animation)

        heartIcon.setOnClickListener {
            isLiked = !isLiked
            animateHeart(isLiked)
        }

        val resultAdapter = ResultAdapter()
        resultOtherRecyclerView = findViewById(R.id.otherMenuRecycler)

        viewModel.classificationList.value?.forEach { resultAdapter.datas.add(it) }
        resultOtherRecyclerView.apply {
            setHasFixedSize(true)
            adapter = resultAdapter
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun animateHeart(isLiked: Boolean) {
        if (isLiked) {
            lottieAnimationView.setAnimation(R.raw.heart_click)
            lottieAnimationView.playAnimation()
            lottieAnimationView.visibility = View.VISIBLE
        } else {
            lottieAnimationView.visibility = View.INVISIBLE
        }
    }
}