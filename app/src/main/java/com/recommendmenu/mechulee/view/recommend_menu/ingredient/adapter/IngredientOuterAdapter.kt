package com.recommendmenu.mechulee.view.recommend_menu.ingredient.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orhanobut.logger.Logger
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.model.data.IngredientInfo

class IngredientOuterAdapter : RecyclerView.Adapter<IngredientOuterAdapter.ViewHolder>() {
    val classificationValues = listOf("야채", "과일", "밥/면", "고기", "생선", "소스", "기타")
    val adapterList = arrayOfNulls<IngredientInnerAdapter>(classificationValues.size)

    var itemList= mapOf<String, ArrayList<IngredientInfo>>()
    var clickedData = ArrayList<String>()

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
        for (nowKey in itemList.keys) {
            itemList[nowKey]?.forEach {
                if(it.checked && it.title !in clickedData) {
                    clickedData.add(it.title)
                }
            }
        }
        var nowIndex: Int
        var nowKey = ""
        var englishName = ""
        if (itemList.size == 1) {
            nowKey = itemList.keys.toList()[0]
            nowIndex = classificationValues.indexOf(nowKey)
        } else {
            nowIndex = position
            when (position) {
                0 -> nowKey = "야채"
                1 -> nowKey = "과일"
                2 -> nowKey = "밥/면"
                3 -> nowKey = "고기"
                4 -> nowKey = "생선"
                5 -> nowKey = "소스"
                6 -> nowKey = "기타"
            }
        }
        when (nowKey) {
            "야채" -> englishName = "Vegetables"
            "과일" -> englishName = "Fruits"
            "밥/면" -> englishName = "Rice&Noodle"
            "고기" -> englishName = "Meat"
            "생선" -> englishName = "Fish"
            "소스" -> englishName = "Sauce"
            "기타" -> englishName = "Others"
        }
        val classificationName = "• $nowKey $englishName"

        holder.textView.text = classificationName

        var recyclerViewAdapter02 = IngredientInnerAdapter(clickedData)

        itemList[nowKey]?.forEach { recyclerViewAdapter02.ingredientList.add(it) }

        holder.recyclerView.apply {
            adapter = recyclerViewAdapter02
            layoutManager = GridLayoutManager(holder.itemView.context, 4)
            setHasFixedSize(true)
        }
        adapterList[nowIndex] = recyclerViewAdapter02

        // adapterList에 recyclerView를 스크롤할 때마다 계속 추가되는 오류 ;;
//        Logger.d("개수 확인 ${adapterList.size}")
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}
