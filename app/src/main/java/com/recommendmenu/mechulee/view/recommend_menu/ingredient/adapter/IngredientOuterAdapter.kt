package com.recommendmenu.mechulee.view.recommend_menu.ingredient.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.model.data.IngredientInfo

class IngredientOuterAdapter : RecyclerView.Adapter<IngredientOuterAdapter.ViewHolder>() {
    private lateinit var recyclerViewAdapter02: IngredientInnerAdapter

    lateinit var itemList: Map<String, ArrayList<IngredientInfo>>

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView01)
        val recyclerView: RecyclerView = itemView.findViewById(R.id.recycler01)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_ingredient_outer, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // itemList 사이즈에 따라 key를 다르게 받아 Recycler View에 나타냄
        var nowKey = ""
        var englishName = ""
        if (itemList.size == 1) {
            nowKey = itemList.keys.toList()[0]
        } else {
            when (position) {
                0 -> nowKey = "야채"
                1 -> nowKey = "과일"
                2 -> nowKey = "고기"
                3 -> nowKey = "면"
                4 -> nowKey = "추가재료"
            }
        }
        when (nowKey) {
            "야채" -> englishName = "Vegetables"
            "과일" -> englishName = "Fruits"
            "고기" -> englishName = "Meat"
            "면" -> englishName = "Noodle"
            "추가재료" -> englishName = "Others"
        }

        holder.textView.text = "• $nowKey $englishName"

        recyclerViewAdapter02 = IngredientInnerAdapter()

        itemList[nowKey]?.forEach { recyclerViewAdapter02.ingredientList.add(it) }

        holder.recyclerView.apply {
            adapter = recyclerViewAdapter02
            layoutManager = GridLayoutManager(holder.itemView.context, 4)
            setHasFixedSize(true)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}
