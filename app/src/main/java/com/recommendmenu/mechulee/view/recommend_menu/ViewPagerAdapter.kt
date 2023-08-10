package com.recommendmenu.mechulee.view.recommend_menu

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.recommendmenu.mechulee.view.recommend_menu.ai.AIFragment
import com.recommendmenu.mechulee.view.recommend_menu.home.HomeFragment
import com.recommendmenu.mechulee.view.recommend_menu.ingredient.IngredientFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> AIFragment()
            2 -> IngredientFragment()
            else -> HomeFragment()
        }
    }
}