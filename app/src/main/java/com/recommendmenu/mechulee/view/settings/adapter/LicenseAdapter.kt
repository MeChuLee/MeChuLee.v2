package com.recommendmenu.mechulee.view.settings.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.databinding.RecyclerViewLicenseBinding

class LicenseAdapter : RecyclerView.Adapter<LicenseAdapter.MyViewHolder>() {
    lateinit var binding: RecyclerViewLicenseBinding

    var list = ArrayList<String>()

    class MyViewHolder(val binding: RecyclerViewLicenseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(title: String) {
            binding.licenseText.text = title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recycler_view_license,
            parent,
            false
        )

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(list[holder.absoluteAdapterPosition])
    }

    override fun getItemCount(): Int = list.size
}