package com.recommendmenu.mechulee.view.settings.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.databinding.RecyclerViewLicenseBinding
import com.recommendmenu.mechulee.model.data.LicenseInfo

class LicenseAdapter : RecyclerView.Adapter<LicenseAdapter.MyViewHolder>() {
    private lateinit var binding: RecyclerViewLicenseBinding

    var list = ArrayList<LicenseInfo>()

    class MyViewHolder(val binding: RecyclerViewLicenseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(licenseInfo: LicenseInfo) {
            binding.licenseText.text = licenseInfo.name
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

        // 라이센스 정보 클릭 시 해당 url 로 이동 (url 정보가 없을 경우 x)
        holder.itemView.setOnClickListener {
            if (list[holder.absoluteAdapterPosition].url != "") {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(list[holder.absoluteAdapterPosition].url))
                it.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = list.size
}