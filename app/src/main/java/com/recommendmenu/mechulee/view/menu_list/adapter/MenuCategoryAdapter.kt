package com.recommendmenu.mechulee.view.menu_list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.recommendmenu.mechulee.R

class MenuCategoryAdapter(private val menuCategoryListener : MenuCategoryListener)
    : RecyclerView.Adapter<MenuCategoryAdapter.MyViewHolder>() {

    val list = ArrayList<String>()
    var currentClickIdx = 0

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var categoryButton: AppCompatButton = itemView.findViewById(R.id.categoryButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_menu_category, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.categoryButton.text = list[position]

        if (position == currentClickIdx) {
            // 현재 position 이 현재 선택된 index 일 경우, click 색상으로 변경
            holder.categoryButton.setBackgroundResource(R.drawable.clicked_button_green)
            holder.categoryButton.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
        } else {
            // 현재 position 이 현재 선택되지 않은 index 일 경우, unclick 색상으로 변경
            holder.categoryButton.setBackgroundResource(R.drawable.unclicked_button)
            holder.categoryButton.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.defaultColor))
        }

        holder.categoryButton.setOnClickListener {
            // notifyItemChanged 는 대기열에 추가 되기 때문에 onClickListener 가 끝나고 한 번에 실행
            notifyItemChanged(currentClickIdx)
            currentClickIdx = position
            notifyItemChanged(currentClickIdx)

            // 카테고리가 변경되었음을 알림
            menuCategoryListener.changeCurrentCategory(holder.categoryButton.text.toString())
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface MenuCategoryListener {
        fun changeCurrentCategory(category: String)
    }
}