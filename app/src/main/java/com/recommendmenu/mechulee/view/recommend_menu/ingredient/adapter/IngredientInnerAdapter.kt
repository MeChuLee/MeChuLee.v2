package com.recommendmenu.mechulee.view.recommend_menu.ingredient.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.model.data.IngredientInfo

class IngredientInnerAdapter(private val ingredientInnerListener: IngredientInnerListener) :
    RecyclerView.Adapter<IngredientInnerAdapter.ViewHolder>() {

    var innerIngredientList = ArrayList<IngredientInfo>()
    var innerSelectedIngredientList = ArrayList<String>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView02)
        val textView: TextView = itemView.findViewById(R.id.textView02)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_ingredient_inner, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = innerIngredientList[position]

        holder.imageView.setImageResource(item.imageResId)
        holder.textView.text = item.title

        // innerSelectedIngredeintList에 들어있는 재료들은 선택한 표시를 반영
        innerSelectedIngredientList.let {
            if (item.title in it) {
                holder.itemView.setBackgroundResource(R.drawable.clicked_ingredient)
            }
        }

        holder.itemView.setOnClickListener {
            ingredientInnerListener.clickIngredient(item.title)
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return innerIngredientList.size
    }

    interface IngredientInnerListener {
        fun clickIngredient(clickedIngredient: String)
    }
}
