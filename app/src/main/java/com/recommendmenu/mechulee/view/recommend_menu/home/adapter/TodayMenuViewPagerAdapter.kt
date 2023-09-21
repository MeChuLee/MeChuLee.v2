package com.recommendmenu.mechulee.view.recommend_menu.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.databinding.ViewPagerTodayMenuBinding
import com.recommendmenu.mechulee.model.data.MenuInfo
import com.recommendmenu.mechulee.utils.Constants
import com.recommendmenu.mechulee.utils.NetworkUtils

class TodayMenuViewPagerAdapter(
    var todayMenuList: ArrayList<MenuInfo>,
    private var todayMenuClickListener: TodayMenuClickListener
) : RecyclerView.Adapter<TodayMenuViewPagerAdapter.MyViewHolder>() {

    lateinit var binding: ViewPagerTodayMenuBinding

    class MyViewHolder(val binding: ViewPagerTodayMenuBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(menuInfo: MenuInfo) {
            binding.menuName.text = menuInfo.name
            binding.mainIngredient.text = menuInfo.ingredients
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.view_pager_today_menu, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(todayMenuList[position % todayMenuList.size])

        // 메뉴 이미지는 테스트 이미지로 설정
        val menuName = (position % 20) + 1
        NetworkUtils.loadImage(
            holder.itemView.context,
            holder.binding.menuImage,
            "$menuName.jpg",
            Constants.URL_TYPE_MENU
        )

        binding.cardView.setOnClickListener {
            todayMenuClickListener.todayMenuClick(todayMenuList[position % todayMenuList.size])
        }
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    interface TodayMenuClickListener {
        fun todayMenuClick(menuInfo: MenuInfo)
    }
}