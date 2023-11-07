package com.recommendmenu.mechulee.view.recommend_menu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.recommendmenu.mechulee.databinding.FragmentRecommendBinding
import com.recommendmenu.mechulee.view.recommend_menu.home.TutorialActivity

class RecommendFragment : Fragment() {

    private var _binding: FragmentRecommendBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecommendBinding.inflate(layoutInflater)

        initViewPager()

        binding.helpButton.setOnClickListener {
            startActivity(Intent(requireContext(), TutorialActivity::class.java))
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViewPager() {
        val viewPagerAdapter = ViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
        binding.viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(
            binding.tabs,
            binding.viewPager
        ) { tab, position ->
            when (position) {
                0 -> tab.text = "홈"
                1 -> tab.text = "AI 추천"
                2 -> tab.text = "재료 추천"
            }
        }.attach()
    }
}