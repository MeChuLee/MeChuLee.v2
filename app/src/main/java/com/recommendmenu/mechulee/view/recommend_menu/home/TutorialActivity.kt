package com.recommendmenu.mechulee.view.recommend_menu.home

import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.databinding.ActivityTutorialBinding
import com.recommendmenu.mechulee.view.recommend_menu.home.adapter.TutorialViewPagerAdapter

class TutorialActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTutorialBinding

    private var size = 0
    private var bannerPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTutorialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // StatusBar 색 변경
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        initViewPager()
    }

    private fun initViewPager() {
        val viewPagerAdapter = TutorialViewPagerAdapter(object : TutorialViewPagerAdapter.FinishListener {
            override fun onFinishButtonClick() {
                finish()
            }
        })

        viewPagerAdapter.tutorialImageList.add(R.raw.mechulee)
        viewPagerAdapter.tutorialImageList.add(R.raw.mechulee)
        viewPagerAdapter.tutorialImageList.add(R.raw.mechulee)
        size = viewPagerAdapter.tutorialImageList.size

        // 현재 banner 가 가리키고 있는 position 초기화
        bannerPosition =
            Int.MAX_VALUE / 2 - kotlin.math.ceil(viewPagerAdapter.tutorialImageList.size.toDouble() / 2)
                .toInt()

        binding.tutorialViewPager.apply {
            adapter = viewPagerAdapter
            offscreenPageLimit = 1

            // Page 가 바뀔 경우 callback event
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    // Page 를 옮겼을 경우, 현재 banner position 저장 및 indicator 업데이트
                    bannerPosition = position
                    setCurrentIndicator(bannerPosition)
                }
            })
        }

        // ViewPager Indicator 초기화
        setupIndicators(viewPagerAdapter.tutorialImageList.size)
    }

    // ViewPager Indicator 초기 셋팅
    private fun setupIndicators(count: Int) {
        val indicators = arrayOfNulls<ImageView>(count)
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(16, 8, 16, 8)
        for (i in indicators.indices) {
            indicators[i] = ImageView(this)
            indicators[i]!!.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.bg_indicator_inactive
                )
            )
            indicators[i]!!.layoutParams = params
            binding.layoutIndicators.addView(indicators[i])
        }
        setCurrentIndicator(0)
    }


    // 현재 Indicator 상태 ViewPager Indicator 에 반영
    private fun setCurrentIndicator(position: Int) {
        val childCount: Int = binding.layoutIndicators.childCount
        for (i in 0 until childCount) {
            val imageView = binding.layoutIndicators.getChildAt(i) as ImageView
            if (i == position % size) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.bg_indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.bg_indicator_inactive
                    )
                )
            }
        }
    }
}