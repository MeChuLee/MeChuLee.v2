package com.recommendmenu.mechulee.view.menu_list

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.recommendmenu.mechulee.databinding.FragmentMenuListBinding
import com.recommendmenu.mechulee.view.menu_list.adapter.MenuCategoryAdapter
import com.recommendmenu.mechulee.view.menu_list.adapter.MenuGridAdapter
import com.recommendmenu.mechulee.view.menu_list.adapter.MenuListAdapter

class MenuListFragment : Fragment() {

    companion object {
        private const val MENU_TYPE_LIST = 0
        private const val MENU_TYPE_GRID = 1
        private const val GRID_LAYOUT_SPAN_COUNT = 3
    }

    private var _binding: FragmentMenuListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MenuListViewModel

    private lateinit var menuListRecyclerViewAdapter: MenuListAdapter
    private lateinit var menuGridRecyclerViewAdapter: MenuGridAdapter

    private var currentMenuType = MENU_TYPE_LIST

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuListBinding.inflate(layoutInflater)

        viewModel = ViewModelProvider(this)[MenuListViewModel::class.java]
        initRecyclerView()

        viewModel.menuList.observe(requireActivity()) { menuList ->
            menuListRecyclerViewAdapter.list = menuList
            menuGridRecyclerViewAdapter.list = menuList

            when (currentMenuType) {
                MENU_TYPE_LIST -> menuListRecyclerViewAdapter.notifyDataSetChanged()
                MENU_TYPE_GRID -> menuGridRecyclerViewAdapter.notifyDataSetChanged()
            }
        }

        initButton()
        initEditText()

        return binding.root
    }

    private fun initRecyclerView() {
        // 메뉴 카테고리 RecyclerView 초기화
        val menuCategoryRecyclerViewAdapter = MenuCategoryAdapter()

        viewModel.categoryList.value?.forEach { menuCategoryRecyclerViewAdapter.list.add(it) }

        binding.menuCategoryRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = menuCategoryRecyclerViewAdapter
        }

        menuListRecyclerViewAdapter = MenuListAdapter()
        menuGridRecyclerViewAdapter = MenuGridAdapter()

        binding.menuListRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = menuListRecyclerViewAdapter
        }
    }

    private fun initButton() {
        // '리스트' 아이콘 클릭 시 -> Fragment 전환
        binding.menuListImageIcon.setOnClickListener {
            binding.menuListRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = menuListRecyclerViewAdapter
            }
        }

        // '그리드' 아이콘 클릭 시 -> Fragment 전환
        binding.menuListGridIcon.setOnClickListener {
            binding.menuListRecyclerView.apply {
                layoutManager = GridLayoutManager(requireContext(), GRID_LAYOUT_SPAN_COUNT)
                adapter = menuGridRecyclerViewAdapter
            }
        }
    }

    private fun initEditText() {
        // 검색창 입력 시마다 검색 기능 수행
        binding.menuSearchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.searchMenuList(binding.menuSearchEditText.text.toString())
            }
        })

        // 검색창 키보드 '검색' 아이콘 클릭 시 키보드 내리기
        binding.menuSearchEditText.setOnEditorActionListener { _, actionId, _ ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.menuSearchEditText.windowToken, 0)
                handled = true
            }
            handled
        }
    }
}