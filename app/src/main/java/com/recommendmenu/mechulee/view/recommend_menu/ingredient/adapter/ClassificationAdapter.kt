package com.example.selectingredients

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.recommendmenu.mechulee.R

class ClassificationAdapter(val classificationListener : ClassificationListener) :
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
            holder.classificationBtn.setBackgroundResource(R.drawable.clicked_button)
            holder.classificationBtn.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.white
                )
            )
        } else {
            holder.classificationBtn.setBackgroundResource(R.drawable.unclicked_button)
            holder.classificationBtn.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.defaultColor
                )
            )
        }

        // 선택한 재료 카테고리 click event 처리 부분
        holder.classificationBtn.setOnClickListener {
            notifyItemChanged(nowIndex)
            nowIndex = position
            notifyItemChanged(nowIndex)

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