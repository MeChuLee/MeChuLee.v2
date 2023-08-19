package com.recommendmenu.mechulee.view.recommend_menu.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.databinding.ViewPagerTodayMenuBinding

class TodayMenuViewPagerAdapter : RecyclerView.Adapter<TodayMenuViewPagerAdapter.MyViewHolder>() {

    lateinit var binding: ViewPagerTodayMenuBinding

    var todayMenuList = ArrayList<String>()

    class MyViewHolder(val binding: ViewPagerTodayMenuBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: String) {
            binding.menuName.text = data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.view_pager_today_menu, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(todayMenuList[position % todayMenuList.size])
    }

    override fun getItemCount(): Int = Int.MAX_VALUE
}