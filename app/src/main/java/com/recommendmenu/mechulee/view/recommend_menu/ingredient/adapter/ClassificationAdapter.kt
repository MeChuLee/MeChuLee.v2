package com.recommendmenu.mechulee.view.recommend_menu.ingredient.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.recommendmenu.mechulee.R

class ClassificationAdapter(val classificationListener: ClassificationListener) :
    RecyclerView.Adapter<ClassificationAdapter.ViewHolder>() {

    var datas = ArrayList<String>()
    var nowIndex = 0

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val classificationBtn: Button = itemView.findViewById(R.id.classificationButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_ingredient_classification, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.classificationBtn.text = datas[position]

        if (nowIndex == position) {
            // 현재 position이 현재 선택된 index일 경우, click 색상으로 변경
            holder.classificationBtn.setBackgroundResource(R.drawable.clicked_button)
            holder.classificationBtn.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.white
                )
            )
        } else {
            // 현재 position이 현재 선택되지 않은 index일 경우, unclick 색상으로 변경
            holder.classificationBtn.setBackgroundResource(R.drawable.unclicked_button)
            holder.classificationBtn.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.defaultColor
                )
            )
        }

        holder.classificationBtn.setOnClickListener {
            // 선택한 재료 classification click event 처리 부분
            notifyItemChanged(nowIndex)
            nowIndex = position
            notifyItemChanged(nowIndex)

            // classification이 변경되었음을 알림
            classificationListener.changeCurrentClassification(holder.classificationBtn.text.toString())
        }
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    interface ClassificationListener {
        fun changeCurrentClassification(classification: String)
    }

}