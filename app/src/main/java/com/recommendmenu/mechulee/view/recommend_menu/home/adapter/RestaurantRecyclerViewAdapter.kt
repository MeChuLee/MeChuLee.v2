package com.recommendmenu.mechulee.view.recommend_menu.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.databinding.RecyclerViewRestaurantBinding
import com.recommendmenu.mechulee.model.network.search.Item

class RestaurantRecyclerViewAdapter(
    private val restaurantClickListener: RestaurantClickListener
) : RecyclerView.Adapter<RestaurantRecyclerViewAdapter.MyViewHolder>() {

    lateinit var binding: RecyclerViewRestaurantBinding

    var restaurantList = ArrayList<Item>()

    class MyViewHolder(val binding: RecyclerViewRestaurantBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: Item) {
            // HTML 태그 삭제 후 반영
            binding.restaurantName.text = data.title.replace(Regex("<.*?>"), " ")
            binding.restaurantAddress.text = data.roadAddress?.replace(Regex("<.*?>"), " ")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.recycler_view_restaurant, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(restaurantList[position])

        holder.itemView.setOnClickListener {
            restaurantClickListener.restaurantClick(restaurantList[position].title)
        }
    }

    override fun getItemCount(): Int = restaurantList.size

    interface RestaurantClickListener {
        fun restaurantClick(restaurantName: String)
    }
}