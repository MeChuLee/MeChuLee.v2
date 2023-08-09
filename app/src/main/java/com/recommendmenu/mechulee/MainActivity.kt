package com.recommendmenu.mechulee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.recommendmenu.mechulee.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)

        initBottomNavigationBar()
    }

    private fun initBottomNavigationBar() {
        binding.expandableBottomBar.onItemSelectedListener = { view, menuItem, isTrue ->
            /**
             * handle menu item clicks here,
             * but clicks on already selected item will not affect this callback
             */
        }

        binding.expandableBottomBar.onItemReselectedListener = { view, menuItem, isTrue ->
            /**
             * handle here all the click in already selected items
             */
        }
    }
}