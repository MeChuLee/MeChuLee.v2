package com.recommendmenu.mechulee.view.menu_list

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import androidx.recyclerview.widget.RecyclerView
import com.recommendmenu.mechulee.databinding.FragmentMenuListBinding
import com.recommendmenu.mechulee.model.data.MenuInfo
import com.recommendmenu.mechulee.utils.Constants.BOTTOM_BAR_STATUS_HIDE
import com.recommendmenu.mechulee.utils.Constants.BOTTOM_BAR_STATUS_SHOW
import com.recommendmenu.mechulee.utils.Constants.INTENT_NAME_RESULT
import com.recommendmenu.mechulee.view.MainActivity
import com.recommendmenu.mechulee.view.menu_list.adapter.MenuCategoryAdapter
import com.recommendmenu.mechulee.view.menu_list.adapter.MenuGridAdapter
import com.recommendmenu.mechulee.view.menu_list.adapter.MenuListAdapter
import com.recommendmenu.mechulee.view.result.menu.MenuResultActivity

class MenuListFragment : Fragment() {

    companion object {
        private const val GRID_LAYOUT_SPAN_COUNT = 3
    }

    private var _binding: FragmentMenuListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MenuListViewModel

    // 메뉴 리스트 RecyclerView 에서 사용할 두 가지 Adapter (List, Grid)
    private var menuListRecyclerViewAdapter: MenuListAdapter? = null
    private var menuGridRecyclerViewAdapter: MenuGridAdapter? = null
    private var menuCategoryRecyclerViewAdapter: MenuCategoryAdapter? = null

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuListBinding.inflate(layoutInflater)

        viewModel = ViewModelProvider(this)[MenuListViewModel::class.java]

        // viewModel 을 선언한 후, initRecyclerView() 를 순서대로 실행해야 동기적 선언 오류가 없음
        initRecyclerView()

        // recyclerView 초기화 후 viewModel 에서 retrofit 으로 메뉴 정보를 가져와야 하므로, crate 함수 따로 생성 후 실행
        viewModel.create()

        // 메뉴 리스트 observe -> 변경 감지 시 메뉴 정보 리스트 RecyclerView 에 반영
        viewModel.menuList.observe(requireActivity()) { menuList ->
            menuListRecyclerViewAdapter?.menuList = menuList.map { it.copy() }.toCollection(ArrayList())
            menuGridRecyclerViewAdapter?.menuList = menuList.map { it.copy() }.toCollection(ArrayList())

            menuListRecyclerViewAdapter?.notifyDataSetChanged()
            menuGridRecyclerViewAdapter?.notifyDataSetChanged()
        }

        // 메뉴 카테고리 리스트 observe -> 변경 감지 시 메뉴 카테고리 리스트 RecyclerView 에 반영
        viewModel.categoryList.observe(requireActivity()) { categoryList ->
            menuCategoryRecyclerViewAdapter?.list?.clear()
            menuCategoryRecyclerViewAdapter?.list?.addAll(categoryList)
            menuCategoryRecyclerViewAdapter?.notifyDataSetChanged()
        }

        initButton()
        initEditText()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        menuListRecyclerViewAdapter = null
        menuGridRecyclerViewAdapter = null
        menuCategoryRecyclerViewAdapter = null
    }

    private fun initRecyclerView() {
        // 메뉴 카테고리 RecyclerView 초기화
        menuCategoryRecyclerViewAdapter = MenuCategoryAdapter(object: MenuCategoryAdapter.MenuCategoryListener {
            // Listener 내부 함수 정의
            override fun changeCurrentCategory(category: String) {
                // 카테고리 버튼을 클릭하여 현재 카테고리 변경 시
                viewModel.searchMenuList(category, binding.menuSearchEditText.text.toString())
            }
        })

        binding.menuCategoryRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = menuCategoryRecyclerViewAdapter
        }

        // 메뉴 리스트 RecyclerView 초기화
        menuListRecyclerViewAdapter = MenuListAdapter(object: MenuListAdapter.MenuListClickListener {
            override fun menuListClick(menuInfo: MenuInfo) {
                startMenuResultActivity(menuInfo)
            }
        })
        menuGridRecyclerViewAdapter = MenuGridAdapter(object: MenuGridAdapter.MenuGridClickListener {
            override fun menuGridClick(menuInfo: MenuInfo) {
                startMenuResultActivity(menuInfo)
            }
        })

        binding.menuListRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = menuListRecyclerViewAdapter

            // Scroll 이벤트 추가 -> MainActivity Bottom Bar 를 컨트롤하기 위해 MainActivity 에 Listener 로 알림
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (dy > 0) {
                        // 아래로 스크롤 시 Bottom Bar 사라짐
                        (activity as? MainActivity)?.mainActivityListener?.changeBottomBarStatus(BOTTOM_BAR_STATUS_HIDE)
                    } else {
                        // 위로 스크롤 시 Bottom Bar 나타남
                        (activity as? MainActivity)?.mainActivityListener?.changeBottomBarStatus(BOTTOM_BAR_STATUS_SHOW)
                    }
                }
            })
        }
    }

    private fun initButton() {
        // '리스트' 아이콘 클릭 시 -> 메뉴 리스트 RecyclerView Adapter 전환 (List 형식)
        binding.menuListImageIcon.setOnClickListener {
            binding.menuListRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = menuListRecyclerViewAdapter
            }
        }

        // '그리드' 아이콘 클릭 시 -> 메뉴 리스트 RecyclerView Adapter 전환 (Grid 형식)
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
                viewModel.searchMenuList(null, binding.menuSearchEditText.text.toString())
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

    private fun startMenuResultActivity(menuInfo: MenuInfo) {
        val intent = Intent(activity, MenuResultActivity::class.java)
        intent.putExtra(INTENT_NAME_RESULT, menuInfo)
        startActivity(intent)
    }
}