package com.recommendmenu.mechulee.view.like_menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.model.data.MenuInfo

class LikeAdapter : RecyclerView.Adapter<LikeAdapter.ViewHolder>() {
    var datas = ArrayList<MenuInfo>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val likeMenuImage: ImageView = itemView.findViewById(R.id.likeImageView)
        val likeMenuText: TextView = itemView.findViewById(R.id.likeTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_like, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.likeMenuImage.setImageResource(R.drawable.example_result)
        holder.likeMenuText.text = datas[position].name
    }

    override fun getItemCount(): Int {
        return datas.size
    }
}