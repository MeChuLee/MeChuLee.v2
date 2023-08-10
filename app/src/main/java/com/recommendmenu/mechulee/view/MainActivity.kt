package com.recommendmenu.mechulee.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.databinding.ActivityMainBinding
import github.com.st235.lib_expandablebottombar.navigation.ExpandableBottomBarNavigationUI

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initBottomNavigationBar()

        // StatusBar 색 변경
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
    }

    private fun initBottomNavigationBar() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        ExpandableBottomBarNavigationUI.setupWithNavController(binding.expandableBottomBar, navController)
    }
}