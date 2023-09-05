package com.recommendmenu.mechulee.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.databinding.ActivityMainBinding
import com.recommendmenu.mechulee.utils.constant.Constants.BOTTOM_BAR_STATUS_HIDE
import com.recommendmenu.mechulee.utils.constant.Constants.BOTTOM_BAR_STATUS_SHOW
import com.recommendmenu.mechulee.view.like_menu.LikeFragment
import com.recommendmenu.mechulee.view.menu_list.MenuListFragment
import com.recommendmenu.mechulee.view.recommend_menu.RecommendFragment
import com.recommendmenu.mechulee.view.settings.SettingsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var mainActivityListener: MainActivityListener

    private var backPressedTime: Long = 0

    private lateinit var activeFragment: Fragment
    private val recommendFragment by lazy { RecommendFragment() }
    private val menuListFragment by lazy { MenuListFragment() }
    private val likeFragment by lazy { LikeFragment() }
    private val settingFragment by lazy { SettingsFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initBottomNavigationBar()
        initListener()

        // StatusBar 색 변경
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        // 뒤로 가기 두 번 클릭 시 종료
        onBackPressedDispatcher.addCallback(this) {
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                finish()
            } else {
                Toast.makeText(this@MainActivity, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
            }
            backPressedTime = System.currentTimeMillis()
        }
    }

    // ExpandableBottomBar 에 Navigation 설정
    private fun initBottomNavigationBar() {
        activeFragment = recommendFragment

        supportFragmentManager.beginTransaction()
            .add(R.id.nav_host_fragment, recommendFragment).show(recommendFragment)
            .add(R.id.nav_host_fragment, menuListFragment).hide(menuListFragment)
            .add(R.id.nav_host_fragment, likeFragment).hide(likeFragment)
            .add(R.id.nav_host_fragment, settingFragment).hide(settingFragment)
            .commit()

        binding.expandableBottomBar.onItemSelectedListener = { view, menuItem, fromUser ->
            when (menuItem.id) {
                R.id.navigation_recommend -> {
                    supportFragmentManager.beginTransaction()
                        .hide(activeFragment).show(recommendFragment).commit()
                    activeFragment = recommendFragment
                }

                R.id.navigation_menu_list -> {
                    supportFragmentManager.beginTransaction()
                        .hide(activeFragment).show(menuListFragment).commit()
                    activeFragment = menuListFragment
                }

                R.id.navigation_like -> {
                    supportFragmentManager.beginTransaction()
                        .hide(activeFragment).show(likeFragment).commit()
                    activeFragment = likeFragment
                }

                R.id.navigation_settings -> {
                    supportFragmentManager.beginTransaction()
                        .hide(activeFragment).show(settingFragment).commit()
                    activeFragment = settingFragment
                }
            }
        }
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