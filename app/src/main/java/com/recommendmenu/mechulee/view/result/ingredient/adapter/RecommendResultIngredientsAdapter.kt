package com.recommendmenu.mechulee.view.result.ingredient.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.databinding.RecyclerViewIngredientRecommendIngredientsBinding
import com.recommendmenu.mechulee.utils.Constants
import com.recommendmenu.mechulee.utils.NetworkUtils

class RecommendResultIngredientsAdapter : RecyclerView.Adapter<RecommendResultIngredientsAdapter.MyViewHolder>() {

    lateinit var binding: RecyclerViewIngredientRecommendIngredientsBinding

    var ingredientList = ArrayList<String>()

    class MyViewHolder(val binding: RecyclerViewIngredientRecommendIngredientsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(ingredient: String) {
            NetworkUtils.loadImage(itemView.context, binding.ingredientImage, "$ingredient.png", Constants.URL_TYPE_INGREDIENT)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.recycler_view_ingredient_recommend_ingredients, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(ingredientList[holder.absoluteAdapterPosition])
    }

    override fun getItemCount(): Int = ingredientList.size
}