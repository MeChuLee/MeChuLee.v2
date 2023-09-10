package com.recommendmenu.mechulee.view.recommend_menu.ingredient

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.recommendmenu.mechulee.R

class ResultIngredientAdapter : RecyclerView.Adapter<ResultIngredientAdapter.ViewHolder>() {
    var ingredientList = ArrayList<String>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.resultIngredientImage)
        val textView: TextView = itemView.findViewById(R.id.resultIngredientText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_result_ingredient, parent, false)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = ingredientList[position]
        holder.textView.text = item
        holder.imageView.setImageResource(R.drawable.example_result_ingredient)
    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }

}