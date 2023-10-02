package com.recommendmenu.mechulee.view.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.databinding.ActivityImageLicenseBinding
import com.recommendmenu.mechulee.utils.Constants
import com.recommendmenu.mechulee.view.settings.adapter.LicenseAdapter

class LicenseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageLicenseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityImageLicenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        // 이미지 라이센스 / 오픈 소스 라이센스 중 어떤 타입인지 판별 후 제목에 반영
        val licenseType = intent.getStringExtra(Constants.INTENT_NAME_LICENSE)
        binding.imageTitle.text = licenseType

        // 라이센스 목록을 표시해 줄 adpater
        val licenseAdapter = LicenseAdapter()

        // 라이센스 타입에 따라 맞는 list 목록을 Constants 클래스에서 불러와 반영
        when (licenseType) {
            Constants.INTENT_VALUE_LICENSE_OPENSOURCE -> licenseAdapter.list = Constants.OPENSOURCE_LICENSE_LIST
            Constants.INTENT_VALUE_LICENSE_IMAGE -> licenseAdapter.list = Constants.IMAGE_LICENSE_LIST
        }

        // RecyclerView 초기화
        binding.imageLicenseRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@LicenseActivity, LinearLayoutManager.VERTICAL, false)
            adapter = licenseAdapter
        }
    }
}