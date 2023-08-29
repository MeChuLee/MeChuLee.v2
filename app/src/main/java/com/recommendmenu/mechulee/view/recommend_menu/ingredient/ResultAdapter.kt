package com.recommendmenu.mechulee.view.recommend_menu.ingredient

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.recommendmenu.mechulee.R

class ResultAdapter: RecyclerView.Adapter<ResultAdapter.ViewHolder>() {
    var datas = ArrayList<String>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val otherMenuImage: AppCompatImageView = itemView.findViewById(R.id.resultImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_ingredient_result, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val myExample = R.drawable.example_result
        holder.otherMenuImage.setImageResource(myExample)
    }

    override fun getItemCount(): Int {
        return datas.size
    }


}