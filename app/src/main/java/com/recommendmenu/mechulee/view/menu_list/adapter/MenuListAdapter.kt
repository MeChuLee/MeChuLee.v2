package com.recommendmenu.mechulee.view.menu_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.databinding.RecyclerViewMenuTypeListBinding
import com.recommendmenu.mechulee.model.data.MenuInfo
import com.recommendmenu.mechulee.utils.Constants.URL_TYPE_MENU
import com.recommendmenu.mechulee.utils.NetworkUtils

class MenuListAdapter(
    private var menuListClickListener: MenuListClickListener
) : RecyclerView.Adapter<MenuListAdapter.MyViewHolder>() {

    var menuList = ArrayList<MenuInfo>()

    class MyViewHolder(val binding: RecyclerViewMenuTypeListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MenuInfo) {
            binding.menuTitle.text = item.name
            binding.menuDetail.text = item.ingredients.joinToString(", ")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val listItemBinding = RecyclerViewMenuTypeListBinding.inflate(inflater, parent, false)
        return MyViewHolder(listItemBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(menuList[position])

        // recyclerview animation 효과 추가
        val animation: Animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.slide_in_row)
        holder.itemView.startAnimation(animation)

        // 메뉴 테스트 이미지
        val menuName = menuList[position].name

        // 메뉴 이미지 로드
        NetworkUtils.loadImage(
            holder.itemView.context,
            holder.binding.menuImage,
            "$menuName.jpg",
            URL_TYPE_MENU
        )

        holder.itemView.setOnClickListener {
            menuListClickListener.menuListClick(menuList[holder.absoluteAdapterPosition])
        }
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    interface MenuListClickListener {
        fun menuListClick(menuInfo: MenuInfo)
    }
}