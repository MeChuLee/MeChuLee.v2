package com.recommendmenu.mechulee.view.result.ingredient.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.databinding.RecyclerViewIngredientRecommendResultBinding
import com.recommendmenu.mechulee.model.data.MenuInfo
import com.recommendmenu.mechulee.utils.Constants
import com.recommendmenu.mechulee.utils.NetworkUtils

class IngredientMenuRecyclerViewAdapter : RecyclerView.Adapter<IngredientMenuRecyclerViewAdapter.MyViewHolder>() {

    lateinit var binding: RecyclerViewIngredientRecommendResultBinding

    var menuList = ArrayList<Pair<MenuInfo, ArrayList<Int>>>()

    class MyViewHolder(val binding: RecyclerViewIngredientRecommendResultBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(menuInfo: Pair<MenuInfo, ArrayList<Int>>) {
            binding.menuName.text = menuInfo.first.name

            val recommendResultIngredientsAdapter = RecommendResultIngredientsAdapter()
            val ingredientList = menuInfo.first.ingredients.split(", ")
            recommendResultIngredientsAdapter.ingredientList.addAll(ingredientList)
            recommendResultIngredientsAdapter.noIngredientIndexList.addAll(menuInfo.second)

            binding.ingredientsRecyclerView.apply {
                adapter = recommendResultIngredientsAdapter
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            }

            //@TODO 메뉴 이미지 구해지면 이미지 로드하는 기능 아래 코드로 변경
            //NetworkUtils.loadImage(itemView.context, binding.menuImage, menu.name, Constants.URL_TYPE_MENU)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.recycler_view_ingredient_recommend_result, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(menuList[holder.absoluteAdapterPosition])

        // 테스트 메뉴 이미지
        val imageName = (position % 20) + 1
        NetworkUtils.loadImage(holder.itemView.context, binding.menuImage, "$imageName.jpg", Constants.URL_TYPE_MENU)
    }

    override fun getItemCount(): Int = menuList.size
}