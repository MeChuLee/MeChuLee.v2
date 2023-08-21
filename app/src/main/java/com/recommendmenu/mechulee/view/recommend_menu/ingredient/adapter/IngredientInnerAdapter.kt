package com.recommendmenu.mechulee.view.recommend_menu.ingredient.adapter

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
            // 선택해제를 처리하기 위해 정의한 메소드
            return array.filter { it != value }.toTypedArray()
        }

        val item = ingredientList[position]
        holder.imageView.setImageResource(item.imageResId)
        holder.textView.text = item.title

        holder.itemView.setOnClickListener {
            // 선택하지 않은 재료는 테두리가 생기게끔 표현하고, 이미 클릭했던 재료인 경우는 테두리 사라지게 표현
            if (ingredientList[position].title in clickedArray) {
                clickedArray = removeItem(clickedArray, ingredientList[position].title)
                arrayIndex--
                holder.itemView.setBackgroundResource(0)
            } else {
                clickedArray[arrayIndex] = ingredientList[position].title
                arrayIndex++
                holder.itemView.setBackgroundResource(R.drawable.clicked_ingredient)
            }
        }
    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }
}