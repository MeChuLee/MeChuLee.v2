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

    companion object {
        const val BOTTOM_BAR_STATUS_SHOW = 0
        const val BOTTOM_BAR_STATUS_HIDE = 1
    }

    private lateinit var binding: ActivityMainBinding
    lateinit var mainActivityListener: MainActivityListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initBottomNavigationBar()
        initListener()

        // StatusBar 색 변경
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
    }

    // ExpandableBottomBar 에 Navigation 설정
    private fun initBottomNavigationBar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        ExpandableBottomBarNavigationUI.setupWithNavController(
            binding.expandableBottomBar,
            navController
        )
    }

    // Listener 설정
    private fun initListener() {
        mainActivityListener = object : MainActivityListener {
            override fun changeBottomBarStatus(visibility: Int) {
                // 변경할 상태에 따라 bottom bar show/hide
                when (visibility) {
                    BOTTOM_BAR_STATUS_SHOW -> binding.expandableBottomBar.show()
                    BOTTOM_BAR_STATUS_HIDE -> binding.expandableBottomBar.hide()
                }
            }
        }
    }

    // Child Fragment 들에서 BottomBar 를 Control 하기 위해 MainActivity 와 통신하는 Listener
    interface MainActivityListener {
        // visibility : BOTTOM_BAR_STATUS_SHOW, BOTTOM_BAR_STATUS_HIDE 으로 설정하기
        fun changeBottomBarStatus(visibility: Int)
    }
}