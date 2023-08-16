package com.recommendmenu.mechulee.view.recommend_menu.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.recommendmenu.mechulee.databinding.FragmentHomeBinding
import com.recommendmenu.mechulee.view.MainActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)

        binding.nestedScrollView.onBottomBarStatusChange = { status ->
            (activity as? MainActivity)?.mainActivityListener?.changeBottomBarStatus(status)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}