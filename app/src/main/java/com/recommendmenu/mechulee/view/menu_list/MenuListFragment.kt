package com.recommendmenu.mechulee.view.menu_list

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
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.databinding.FragmentMenuListBinding
import com.recommendmenu.mechulee.view.menu_list.adapter.MenuCategoryAdapter
import com.recommendmenu.mechulee.view.menu_list.menu_type.MenuTypeGridFragment
import com.recommendmenu.mechulee.view.menu_list.menu_type.MenuTypeListFragment

class MenuListFragment : Fragment() {

    companion object {
        private const val FRAGMENT_TAG_MENU_LIST = "MenuList"
        private const val FRAGMENT_TAG_MENU_GRID = "MenuGrid"
    }

    private var _binding: FragmentMenuListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MenuListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuListBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[MenuListViewModel::class.java]
        viewModel.menuList.observe(requireActivity()) { menuList ->
            // menuList 정보 변경 감지 시 각 Fragment 에 RecyclerView 내용 갱신
            val menuTypeListFragment = requireActivity().supportFragmentManager.findFragmentByTag(FRAGMENT_TAG_MENU_LIST) as? MenuTypeListFragment
            val menuTypeGridFragment = requireActivity().supportFragmentManager.findFragmentByTag(FRAGMENT_TAG_MENU_GRID) as? MenuTypeGridFragment

            menuTypeListFragment?.updateMenuList(menuList)
            menuTypeGridFragment?.updateMenuList(menuList)
        }

        initRecyclerView()
        initFragmentContainer(savedInstanceState)
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
    }

    // 처음 FragmentContainer 셋팅 -> '리스트' RecyclerView Fragment 가 보이게 설정
    private fun initFragmentContainer(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            requireActivity().supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.fragmentContainer, MenuTypeListFragment(viewModel.menuList.value ?: arrayListOf()), FRAGMENT_TAG_MENU_LIST)
            }
        }
    }

    private fun initButton() {
        // '리스트' 아이콘 클릭 시 -> Fragment 전환
        binding.menuListImageIcon.setOnClickListener {
            switchFragment(FRAGMENT_TAG_MENU_GRID, FRAGMENT_TAG_MENU_LIST) {
                MenuTypeListFragment(viewModel.menuList.value ?: arrayListOf())
            }
        }

        // '그리드' 아이콘 클릭 시 -> Fragment 전환
        binding.menuListGridIcon.setOnClickListener {
            switchFragment(FRAGMENT_TAG_MENU_LIST, FRAGMENT_TAG_MENU_GRID) {
                MenuTypeGridFragment(viewModel.menuList.value ?: arrayListOf())
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

    // Fragment 전환 기능 ('리스트', '그리드' 버튼 클릭 시마다 사용)
    private fun switchFragment(fromTag: String, toTag: String, newFragment: () -> Fragment) {
        val fromFragment: Fragment? = requireActivity().supportFragmentManager.findFragmentByTag(fromTag)
        val toFragment: Fragment? = requireActivity().supportFragmentManager.findFragmentByTag(toTag)

        requireActivity().supportFragmentManager.commit {
            fromFragment?.let { this.remove(it) }
            if (toFragment == null) {
                this.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                this.add(R.id.fragmentContainer, newFragment(), toTag)
            } else {
                this.replace(R.id.fragmentContainer, toFragment)
            }
        }
    }
}