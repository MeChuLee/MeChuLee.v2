package com.recommendmenu.mechulee.view.result.menu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.utils.Constants.URL_TYPE_INGREDIENT
import com.recommendmenu.mechulee.utils.NetworkUtils

class MenuResultIngredientAdapter : RecyclerView.Adapter<MenuResultIngredientAdapter.ViewHolder>() {
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
        NetworkUtils.loadImage(
            holder.itemView.context,
            holder.imageView,
            "${item}.png",
            URL_TYPE_INGREDIENT
        )
    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }

}