package com.recommendmenu.mechulee.view.recommend_menu.ingredient.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.model.data.IngredientInfo

class IngredientOuterAdapter(private val ingredientOuterListener: IngredientOuterListener) :
    RecyclerView.Adapter<IngredientOuterAdapter.ViewHolder>() {

    var outerMap = mapOf<String, ArrayList<IngredientInfo>>()
    var outerSelectedIngredientList = ArrayList<String>()

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
        // itemList 사이즈 따라 key 다르게 받아 RecyclerView 반환
        var nowKey = ""
        var englishName = ""
        if (outerMap.size == 1) {
            nowKey = outerMap.keys.toList()[0]
        } else {
            when (position) {
                0 -> nowKey = "야채"
                1 -> nowKey = "밥/면"
                2 -> nowKey = "고기"
                3 -> nowKey = "생선"
                4 -> nowKey = "소스"
                5 -> nowKey = "기타"
            }
        }
        when (nowKey) {
            "야채" -> englishName = "Vegetables"
            "밥/면" -> englishName = "Rice&Noodle"
            "고기" -> englishName = "Meat"
            "생선" -> englishName = "Fish"
            "소스" -> englishName = "Sauce"
            "기타" -> englishName = "Others"
        }
        val classificationName = "• $nowKey $englishName"

        holder.textView.text = classificationName

        val ingredientInnerAdapter =
            IngredientInnerAdapter(object : IngredientInnerAdapter.IngredientInnerListener {
                override fun clickIngredient(clickedIngredient: String) {
                    ingredientOuterListener.clickIngredient(clickedIngredient)
                }
            })
        ingredientInnerAdapter.innerSelectedIngredientList.clear()
        ingredientInnerAdapter.innerSelectedIngredientList.addAll(outerSelectedIngredientList)

        outerMap[nowKey]?.forEach { ingredientInnerAdapter.innerIngredientList.add(it) }

        holder.recyclerView.apply {
            adapter = ingredientInnerAdapter
            layoutManager = GridLayoutManager(holder.itemView.context, 4)
            setHasFixedSize(true)
        }
    }

    override fun getItemCount(): Int {
        return outerMap.size
    }

    interface IngredientOuterListener {
        fun clickIngredient(clickedIngredient: String)
    }
}
