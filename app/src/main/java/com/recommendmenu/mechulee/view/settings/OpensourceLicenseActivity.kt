package com.recommendmenu.mechulee.view.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.databinding.ActivityOpensourceLicenseBinding
import com.recommendmenu.mechulee.utils.Constants
import com.recommendmenu.mechulee.view.settings.adapter.LicenseAdapter

class OpensourceLicenseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOpensourceLicenseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOpensourceLicenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        // 라이센스 목록을 표시해 줄 adpater
        val opensourceLicenseAdapter = LicenseAdapter()

        opensourceLicenseAdapter.list = Constants.OPENSOURCE_LICENSE_LIST

        // RecyclerView 초기화
        binding.menuLicenseRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(
                this@OpensourceLicenseActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = opensourceLicenseAdapter
        }
    }
}