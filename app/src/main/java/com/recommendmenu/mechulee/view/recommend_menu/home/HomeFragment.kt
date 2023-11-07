package com.recommendmenu.mechulee.view.recommend_menu.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.databinding.FragmentHomeBinding
import com.recommendmenu.mechulee.model.data.MenuInfo
import com.recommendmenu.mechulee.utils.Constants
import com.recommendmenu.mechulee.utils.Constants.INTENT_NAME_RESULT
import com.recommendmenu.mechulee.utils.LocationUtils
import com.recommendmenu.mechulee.view.MainActivity
import com.recommendmenu.mechulee.view.dialog.LoadingDialog
import com.recommendmenu.mechulee.view.recommend_menu.home.adapter.RestaurantRecyclerViewAdapter
import com.recommendmenu.mechulee.view.recommend_menu.home.adapter.TodayMenuViewPagerAdapter
import com.recommendmenu.mechulee.view.result.menu.MenuResultActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.URLEncoder

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel

    private var bannerPosition = 0
    private var todayMenuListSize = 3
    private lateinit var job: Job
    private var active = false

    private var restaurantRecyclerViewAdapter: RestaurantRecyclerViewAdapter? = null

    private lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        // data binding 사용을 위해 선언 -> xml 의 viewModel 과 lifecycleOwner 를 현재 Fragment 값으로 반영
        binding.homeViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // CustomNestedScrollView 스크롤 함수 정의
        binding.nestedScrollView.onBottomBarStatusChange = { status ->
            (activity as? MainActivity)?.mainActivityListener?.changeBottomBarStatus(status)
        }

        loadingDialog = LoadingDialog(requireContext())

        initRecyclerView()
        observeData()
        initOnClickListener()

        checkTutorial()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // viewPager 자동 슬라이드 설정
        active = true
        scrollJobCreate()
    }

    override fun onPause() {
        super.onPause()
        // viewPager 자동 슬라이드 취소
        active = false
        job.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        restaurantRecyclerViewAdapter = null
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeData() {
        // 오늘의 메뉴 결과 응답 감지 시 viewpager 에 반영
        viewModel.todayMenuList.observe(requireActivity()) { todayMenuList ->
            initViewPager(todayMenuList)
        }

        // 식당 정보 결과 응답 감지 시 recyclerview 에 반영
        viewModel.restaurantList.observe(requireActivity()) { restaurantList ->
            if (restaurantList.isNotEmpty()) {
                binding.restaurantRecyclerViewEmptyView.visibility = View.GONE
                restaurantRecyclerViewAdapter?.restaurantList?.clear()
                restaurantRecyclerViewAdapter?.restaurantList?.addAll(restaurantList)
                restaurantRecyclerViewAdapter?.notifyDataSetChanged()
            }
        }

        viewModel.randomMenuResult.observe(requireActivity()) { menuInfo ->
            loadingDialog.dismiss()

            val intent = Intent(activity, MenuResultActivity::class.java)
            intent.putExtra(INTENT_NAME_RESULT, menuInfo)
            startActivity(intent)
        }
    }

    private fun initOnClickListener() {
        binding.randomCardView.setOnClickListener {
            loadingDialog.show()
            viewModel.requestRecommendRandomMenu()
        }

        binding.searchRestaurant.setOnClickListener {
            startMapWebViewActivity()
        }

        binding.rightArrowIcon.setOnClickListener {
            startMapWebViewActivity()
        }
    }

    private fun initViewPager(todayMenuList: ArrayList<MenuInfo>) {
        // viewPager 엥서 사용하는 adapter
        val todayMenuViewPagerAdapter = TodayMenuViewPagerAdapter(
            todayMenuList,
            object : TodayMenuViewPagerAdapter.TodayMenuClickListener {
                override fun todayMenuClick(menuInfo: MenuInfo) {
                    // 오늘의 추천 메뉴 클릭 시 메뉴 정보 보는 화면으로 이동
                    val intent = Intent(activity, MenuResultActivity::class.java)
                    intent.putExtra(INTENT_NAME_RESULT, menuInfo)
                    startActivity(intent)
                }
            })

        // 현재 banner 가 가리키고 있는 position 초기화
        bannerPosition =
            Int.MAX_VALUE / 2 - kotlin.math.ceil(todayMenuViewPagerAdapter.todayMenuList.size.toDouble() / 2)
                .toInt()

        binding.todayMenuViewPager.apply {
            // adpater 설정 및 Page 개수(1) 지정
            adapter = todayMenuViewPagerAdapter
            offscreenPageLimit = 1

            // Page 가 바뀔 경우 callback event
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    // Page 를 옮겼을 경우, 현재 banner position 저장 및 indicator 업데이트
                    bannerPosition = position
                    setCurrentIndicator(bannerPosition)
                }

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)

                    // Page 를 슬라이드하는 등의 상태 변화에 따른 event
                    when (state) {
                        // 멈춘 상태 -> 자동 슬라이드 비동기 작업이 취소된 상태일 경우 새로 만들기
                        ViewPager2.SCROLL_STATE_IDLE -> {
                            if (!job.isActive) scrollJobCreate()
                        }

                        // 드래그하는 상태 -> 자동 슬라이드 비동기 작업 취소
                        ViewPager2.SCROLL_STATE_DRAGGING -> job.cancel()

                        // 마지막까지 드래그했을 경우 (현재는 무한 슬라이드를 적용하였기 때문에 무의미)
                        ViewPager2.SCROLL_STATE_SETTLING -> {}
                    }
                }
            })
        }

        // ViewPager Indicator 초기화
        setupIndicators(todayMenuViewPagerAdapter.todayMenuList.size)
    }

    // ViewPager Indicator 초기 셋팅
    private fun setupIndicators(count: Int) {
        val indicators = arrayOfNulls<ImageView>(count)
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(16, 8, 16, 8)
        for (i in indicators.indices) {
            indicators[i] = ImageView(requireContext())
            indicators[i]!!.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
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
            if (i == position % todayMenuListSize) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bg_indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bg_indicator_inactive
                    )
                )
            }
        }
    }

    // ViewPager 자동 슬라이드 Coroutine 생성
    private fun scrollJobCreate() {
        // active 상태일 경우 반복하여 자동 슬라이드 실행 (onPause 를 거쳐 (active == true) 상태가 아닐 경우 멈춤)
        job = lifecycleScope.launch {
            while (active) {
                delay(3000)
                setCurrentIndicator(bannerPosition)
                binding.todayMenuViewPager.setCurrentItem(++bannerPosition, true)
            }
        }
    }

    private fun initRecyclerView() {
        // 식당 리스트 recyclerView 초기화
        restaurantRecyclerViewAdapter = RestaurantRecyclerViewAdapter()

        val noScrollLayoutManager =
            object : LinearLayoutManager(requireContext(), VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    // false 를 반환하여 세로 스크롤 이벤트 막기
                    return false
                }
            }

        binding.restaurantRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = noScrollLayoutManager
            adapter = restaurantRecyclerViewAdapter
        }
    }

    private fun startMapWebViewActivity() {
        // webview activity 전환
        val intent = Intent(requireContext(), WebViewMapActivity::class.java)
        val encodedQuery = URLEncoder.encode(LocationUtils.simpleAddress + " 맛집", "UTF-8")
        val url =
            "https://m.map.naver.com/search2/search.naver?query=${encodedQuery}&sm=hty&style=v5"
        intent.putExtra(Constants.INTENT_NAME_WEB_URL, url)
        startActivity(intent)
    }

    private fun checkTutorial() {
        val preferences = requireContext().getSharedPreferences(Constants.SHARED_PREFERENCES_TUTORIAL_NAME, Context.MODE_PRIVATE)
        val isTutorial = preferences.getBoolean(Constants.SHARED_PREFERENCES_TUTORIAL_KEY, true)

        if (isTutorial) {
            preferences.edit().putBoolean(Constants.SHARED_PREFERENCES_TUTORIAL_KEY, false).apply()
            startActivity(Intent(requireContext(), TutorialActivity::class.java))
        }
    }
}