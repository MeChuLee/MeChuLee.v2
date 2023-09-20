package com.recommendmenu.mechulee.view.like_menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.databinding.FragmentLikeBinding
import com.recommendmenu.mechulee.model.data.MenuInfo

class LikeFragment : Fragment() {
    private var _binding: FragmentLikeBinding? = null
    private val binding get() = _binding!!

    val tempList = ArrayList<MenuInfo>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLikeBinding.inflate(layoutInflater)

        tempList.add(MenuInfo("쿠키", "김치", "한식"))
        tempList.add(MenuInfo("샐러드", "김치", "한식"))
        tempList.add(MenuInfo("김치찌개", "김치, 김치, 김치, 김치", "한식"))
        tempList.add(MenuInfo("된장찌개", "김치", "한식"))
        tempList.add(MenuInfo("제육입니다", "김치", "한식"))
        tempList.add(MenuInfo("안녕하십니까", "김치", "한식"))
        tempList.add(MenuInfo("일곱글자뭐로해", "김치", "한식"))
        tempList.add(MenuInfo("여덟글자는이걸로", "김치", "한식"))
        tempList.add(MenuInfo("니가만든쿠키내가만든쿠키", "김치", "한식"))

        val likeAdapter = LikeAdapter()
        likeAdapter.datas = tempList
        binding.likeRecyclerView.apply {
            setHasFixedSize(true)
            adapter = likeAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}