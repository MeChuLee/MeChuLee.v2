package com.recommendmenu.mechulee.view.result.ingredient.adapter

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.databinding.RecyclerViewIngredientRecommendIngredientsBinding
import com.recommendmenu.mechulee.utils.Constants
import com.recommendmenu.mechulee.utils.NetworkUtils

class RecommendResultIngredientsAdapter :
    RecyclerView.Adapter<RecommendResultIngredientsAdapter.MyViewHolder>() {

    lateinit var binding: RecyclerViewIngredientRecommendIngredientsBinding

    var ingredientList = ArrayList<String>()
    var noIngredientIndexList = ArrayList<Int>()

    class MyViewHolder(val binding: RecyclerViewIngredientRecommendIngredientsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(ingredient: String, isBlack: Boolean) {
            if (isBlack) {
                val matrix = ColorMatrix()
                matrix.setSaturation(0f)  // 색상을 0으로 설정하여 이미지를 흑백으로 만듭니다.
                val filter = ColorMatrixColorFilter(matrix)
                binding.ingredientImage.colorFilter = filter
            }

            NetworkUtils.loadImage(
                itemView.context,
                binding.ingredientImage,
                "$ingredient.png",
                Constants.URL_TYPE_INGREDIENT
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recycler_view_ingredient_recommend_ingredients,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var isBlack = false
        if (holder.absoluteAdapterPosition in noIngredientIndexList) {
            isBlack = true
        }

        holder.onBind(ingredientList[holder.absoluteAdapterPosition], isBlack)
    }

    override fun getItemCount(): Int = ingredientList.size
}