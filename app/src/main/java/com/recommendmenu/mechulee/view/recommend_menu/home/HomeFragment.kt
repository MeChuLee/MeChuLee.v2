package com.recommendmenu.mechulee.view.recommend_menu.home

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.databinding.FragmentHomeBinding
import com.recommendmenu.mechulee.view.MainActivity
import com.recommendmenu.mechulee.view.recommend_menu.home.adapter.TodayMenuViewPagerAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), OnMapReadyCallback {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var bannerPosition = 0
    private var todayMenuListSize = 3
    private lateinit var job: Job
    private var active = false

    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)

        // CustomNestedScrollView 스크롤 함수 정의
        binding.nestedScrollView.onBottomBarStatusChange = { status ->
            (activity as? MainActivity)?.mainActivityListener?.changeBottomBarStatus(status)
        }

        initViewPager()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // viewPager 자동 슬라이드 설정
        active = true
        scrollJobCreate()

        // 위치 권한 체크
        val fineLocationGranted = ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseLocationGranted = ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fineLocationGranted || coarseLocationGranted) {
            initMap()
        } else {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
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
    }

    private fun initViewPager() {
        // viewPager 엥서 사용하는 adapter
        val todayMenuViewPagerAdapter = TodayMenuViewPagerAdapter().apply {
            todayMenuList = ArrayList()
            todayMenuList.add("one")
            todayMenuList.add("two")
            todayMenuList.add("three")
        }

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

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.Follow
    }

    // 위치 권한 요청
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
                initMap()
            }

            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
                initMap()
            }

            else -> {
                // No location access granted
                Toast.makeText(requireContext(), "위치 권한을 허용해주세요.", Toast.LENGTH_SHORT).show()
                val intent: Intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(
                    Uri.parse("package:${requireActivity().packageName}")
                )
                startActivity(intent)
            }
        }
    }

    // 지도 초기화 (현재 위치 등록)
    private fun initMap() {
        // gps 켜져 있는 지 확인
        val locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        ) {
            locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

            val fm = requireActivity().supportFragmentManager
            val mapFragment = fm.findFragmentById(R.id.mapFragment) as MapFragment?
                ?: MapFragment.newInstance().also {
                    fm.beginTransaction().add(R.id.mapFragment, it).commit()
                }

            mapFragment.getMapAsync(this)
        } else {
            // 사용자가 위치 서비스를 키도록 AlertDialog 를 사용하여 유도
            AlertDialog.Builder(requireContext()).apply {
                setTitle("위치 서비스 활성화")
                setMessage("앱에서 위치 기능을 사용하려면 위치 서비스를 활성화해야 합니다. 위치 설정으로 이동하시겠습니까?")
                setPositiveButton("이동") { _, _ ->
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                }
                setNegativeButton("취소") { _, _ ->
                    requireActivity().finish()
                }
                create()
            }.show()
        }
    }
}