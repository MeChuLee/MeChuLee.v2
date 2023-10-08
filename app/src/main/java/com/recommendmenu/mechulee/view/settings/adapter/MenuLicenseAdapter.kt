package com.recommendmenu.mechulee.view.settings.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.databinding.RecyclerViewMenuLicenseBinding
import com.recommendmenu.mechulee.model.data.LicenseInfo

class MenuLicenseAdapter : RecyclerView.Adapter<MenuLicenseAdapter.MyViewHolder>() {
    lateinit var binding: RecyclerViewMenuLicenseBinding

    var list = ArrayList<Pair<LicenseInfo, LicenseInfo>>()

    class MyViewHolder(val binding: RecyclerViewMenuLicenseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(licenseInfoPair: Pair<LicenseInfo, LicenseInfo>) {
            binding.licenseTitleText.text = licenseInfoPair.first.name
            binding.licenseDetailText.text = licenseInfoPair.second.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recycler_view_menu_license,
            parent,
            false
        )

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(list[holder.absoluteAdapterPosition])

        // 라이센스 정보 클릭 시 해당 url 로 이동 (url 정보가 없을 경우 x)
        binding.licenseTitleText.setOnClickListener {
            if (list[holder.absoluteAdapterPosition].first.url != "") {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(list[holder.absoluteAdapterPosition].first.url))
                it.context.startActivity(intent)
            }
        }

        binding.licenseDetailText.setOnClickListener {
            if (list[holder.absoluteAdapterPosition].second.url != "") {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(list[holder.absoluteAdapterPosition].second.url))
                it.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = list.size
}