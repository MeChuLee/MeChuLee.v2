package com.recommendmenu.mechulee.view.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.databinding.ActivityImageLicenseBinding
import com.recommendmenu.mechulee.utils.Constants
import com.recommendmenu.mechulee.view.settings.adapter.LicenseAdapter

class ImageLicenseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageLicenseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityImageLicenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        // 라이센스 목록을 표시해 줄 adpater
        val menuLicenseAdapter = LicenseAdapter()
        val ingredientLicenseAdapter = LicenseAdapter()

        menuLicenseAdapter.list = Constants.MENU_LICENSE_LIST
        ingredientLicenseAdapter.list = Constants.INGREDIENT_LICENSE_LIST

        // RecyclerView 초기화
        binding.menuLicenseRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ImageLicenseActivity, LinearLayoutManager.VERTICAL, false)
            adapter = menuLicenseAdapter
        }

        binding.ingredientLicenseRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ImageLicenseActivity, LinearLayoutManager.VERTICAL, false)
            adapter = ingredientLicenseAdapter
        }
    }
}