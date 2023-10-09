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
import java.lang.Integer.min

class IngredientMenuRecyclerViewAdapter(
    private var ingredientMenuClickListener: IngredientMenuClickListener
) : RecyclerView.Adapter<IngredientMenuRecyclerViewAdapter.MyViewHolder>() {

    lateinit var binding: RecyclerViewIngredientRecommendResultBinding

    var menuList = ArrayList<Pair<MenuInfo, ArrayList<Int>>>()

    class MyViewHolder(val binding: RecyclerViewIngredientRecommendResultBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(menuInfo: Pair<MenuInfo, ArrayList<Int>>, itemViewClickListener: RecommendResultIngredientsAdapter.ItemViewClickListener) {
            // 메뉴 이름 등록
            binding.menuName.text = menuInfo.first.name

            // 메뉴 재료 RecyclerView 등록
            val recommendResultIngredientsAdapter = RecommendResultIngredientsAdapter(itemViewClickListener)
            val ingredientList = menuInfo.first.ingredients


            val colorIngredientList = ArrayList<String>()
            val blackIngredientList = ArrayList<String>()

            for (i in ingredientList.indices) {
                if (i in menuInfo.second) {
                    blackIngredientList.add(ingredientList[i])
                } else {
                    colorIngredientList.add(ingredientList[i])
                }
            }

            recommendResultIngredientsAdapter.colorIngredientList.addAll(colorIngredientList)
            recommendResultIngredientsAdapter.blackIngredientList.addAll(blackIngredientList)

            binding.ingredientsRecyclerView.apply {
                adapter = recommendResultIngredientsAdapter
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.recycler_view_ingredient_recommend_result, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(menuList[holder.absoluteAdapterPosition], object: RecommendResultIngredientsAdapter.ItemViewClickListener {
            override fun itemClick() {
                ingredientMenuClickListener.menuClick(menuList[holder.absoluteAdapterPosition].first)
            }
        })

        // 클릭 시 메뉴 정보 결과 화면으로 전환하기 위한 클릭 이벤트
        binding.cardView.setOnClickListener {
            ingredientMenuClickListener.menuClick(menuList[holder.absoluteAdapterPosition].first)
        }

        // 테스트 메뉴 이미지
        val imageName = (position % 20) + 1
        NetworkUtils.loadImage(holder.itemView.context, binding.menuImage, "$imageName.jpg", Constants.URL_TYPE_MENU)
    }

    override fun getItemCount(): Int = min(menuList.size, 10)

    interface IngredientMenuClickListener {
        fun menuClick(menu: MenuInfo)
    }
}