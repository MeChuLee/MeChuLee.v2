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

        val holder = MyViewHolder(binding)

        // CardView 클릭 시 메뉴 정보 화면으로 전환할 수 있도록 HomeFragment 에 이벤트 전달
        binding.cardView.setOnClickListener {
            val position = holder.absoluteAdapterPosition % todayMenuList.size
            val todayMenu = todayMenuList[position]

            todayMenuClickListener.todayMenuClick(todayMenu)
        }

        return holder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val todayMenu = todayMenuList[position % todayMenuList.size]

        holder.onBind(todayMenu)

        // 메뉴 이미지는 테스트 이미지로 설정
        val menuName = (position % 20) + 1
        NetworkUtils.loadImage(
            holder.itemView.context,
            holder.binding.menuImage,
            "$menuName.jpg",
            Constants.URL_TYPE_MENU
        )
    }

    override fun getItemCount(): Int = 20

    interface TodayMenuClickListener {
        fun todayMenuClick(menuInfo: MenuInfo)
    }
}