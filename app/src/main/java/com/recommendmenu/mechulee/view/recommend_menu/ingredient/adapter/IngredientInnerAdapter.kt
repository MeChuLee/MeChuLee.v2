package com.example.selectingredients

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.model.data.IngredientInfo

class IngredientInnerAdapter :
    RecyclerView.Adapter<IngredientInnerAdapter.ViewHolder>() {

    var ingredientList = ArrayList<IngredientInfo>()
    var arrayIndex = 0
    var clickedArray = arrayOfNulls<String>(10)

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
        fun removeItem(array: Array<String?>, value: String): Array<String?> {
            return array.filter { it != value }.toTypedArray()
        }

        val item = ingredientList[position]
        holder.imageView.setImageResource(item.imageResId)
        holder.textView.text = item.title

        holder.itemView.setOnClickListener {
            if (ingredientList[position].title in clickedArray) {
                clickedArray = removeItem(clickedArray, ingredientList[position].title)
                arrayIndex--
                holder.itemView.setBackgroundResource(0)
            } else {
                clickedArray[arrayIndex] = ingredientList[position].title
                arrayIndex++
                holder.itemView.setBackgroundResource(R.drawable.clicked_ingredient)
            }
            var newArray = clickedArray.filter { x: String? -> x != null } // null 값을 제외한 배열 출력
            println(newArray)
        }

    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }
}