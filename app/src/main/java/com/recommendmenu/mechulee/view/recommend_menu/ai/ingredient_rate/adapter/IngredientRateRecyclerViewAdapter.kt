package com.recommendmenu.mechulee.view.recommend_menu.ai.ingredient_rate.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.view.recommend_menu.ai.ingredient_rate.IngredientRateViewModel
import com.willy.ratingbar.RotationRatingBar

class IngredientRateRecyclerViewAdapter(private val viewModel: IngredientRateViewModel):
    RecyclerView.Adapter<IngredientRateRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textView: TextView = itemView.findViewById(R.id.textView)
        val rotationRatingBar: RotationRatingBar = itemView.findViewById(R.id.ratingBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_rate_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = viewModel.menuList.value?.get(position)

        item?.let {
            holder.imageView.setImageResource(it.imageResId)
            holder.textView.text = it.title

            // 레이팅바의 리스너를 설정하여 평가 값이 변경되었을 때 평가 값 갱신
            holder.rotationRatingBar.setOnRatingChangeListener { _, rating, _ ->
                it.rating = rating // 평가 값 갱신
            }

            // RotationRating에 적용을 안시켜서 값이 적용이 안됐던 것임.
            // 레이팅 값을 적용하는 부분을 따로 작성해야한다.
            holder.rotationRatingBar.rating = it.rating

            // recyclerview animation 효과 추가
            val animation: Animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.slide_in_row)
            holder.itemView.startAnimation(animation)
        }
    }

    override fun getItemCount(): Int {
        return viewModel.menuList.value?.size ?: 0
    }
}
