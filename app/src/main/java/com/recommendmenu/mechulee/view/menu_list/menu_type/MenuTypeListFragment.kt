package com.recommendmenu.mechulee.view.menu_list.menu_type

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.recommendmenu.mechulee.databinding.FragmentMenuTypeListBinding
import com.recommendmenu.mechulee.model.data.MenuInfo
import com.recommendmenu.mechulee.view.menu_list.adapter.MenuListAdapter

class MenuTypeListFragment(private val menuList: ArrayList<MenuInfo>) : Fragment() {

    private var _binding: FragmentMenuTypeListBinding? = null
    private val binding get() = _binding!!

    private val menuListRecyclerViewAdapter = MenuListAdapter()

    @SuppressLint("NotifyDataSetChanged")
    fun updateMenuList(menuList: ArrayList<MenuInfo>) {
        menuListRecyclerViewAdapter.list = menuList
        menuListRecyclerViewAdapter.notifyDataSetChanged()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMenuTypeListBinding.inflate(layoutInflater)

        initRecyclerView()
        updateMenuList(menuList)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerView() {
        // 메뉴 리스트 RecyclerView
        binding.menuListRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = menuListRecyclerViewAdapter
        }
    }
}