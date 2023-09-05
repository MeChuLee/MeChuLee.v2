package com.recommendmenu.mechulee.view.recommend_menu.ingredient

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.map
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orhanobut.logger.Logger
import com.recommendmenu.mechulee.IngredientData
import com.recommendmenu.mechulee.databinding.FragmentIngredientBinding
import com.recommendmenu.mechulee.model.data.MenuInfo
import com.recommendmenu.mechulee.utils.constant.Constants.BOTTOM_BAR_STATUS_HIDE
import com.recommendmenu.mechulee.utils.constant.Constants.BOTTOM_BAR_STATUS_SHOW
import com.recommendmenu.mechulee.view.MainActivity
import com.recommendmenu.mechulee.view.recommend_menu.ingredient.adapter.ClassificationAdapter
import com.recommendmenu.mechulee.view.recommend_menu.ingredient.adapter.IngredientOuterAdapter
import com.recommendmenu.mechulee.proto.checkedIngredientDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class CheckedIngredientFactory(private val dataStore: DataStore<IngredientData>) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IngredientViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return IngredientViewModel(dataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class IngredientFragment : Fragment() {
    private var _binding: FragmentIngredientBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: IngredientViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val ingredientOuterAdapter = IngredientOuterAdapter()

    private var tempList = ArrayList<String>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIngredientBinding.inflate(layoutInflater)
        val checkedIngredientFactory =
            CheckedIngredientFactory(requireContext().checkedIngredientDataStore)
        viewModel =
            ViewModelProvider(this, checkedIngredientFactory)[IngredientViewModel::class.java]

        // 재료 classification 보여주는 RecyclerView
        initClassificationRecycler()

        // classification 선택으로 변경을 감지 시 선택한 classification의 재료를 RecyclerView에 반영
        viewModel.selectClassificationMap.observe(requireActivity()) { nowMap ->
            ingredientOuterAdapter.itemList = nowMap
            ingredientOuterAdapter.notifyDataSetChanged()
        }

        // 재료 보여주는 RecyclerView
        initIngredientsRecycler()

        binding.selectButton.setOnClickListener {

            // 추천받기 버튼 클릭 시 선택한 값들 모두 출력
            tempList.clear()
            Logger.d("확인용 ${ingredientOuterAdapter.clickedData}")

            ingredientOuterAdapter.clickedData.forEach {
                tempList.add(it)
            }

            lifecycleScope.launch {
                viewModel.addCheckedIngredientInfo(tempList)
            }

//            viewModel.deleteAllCheckedIngredientInfo()

//            MenuInfo("된장찌개", "김치, 두부, 파, 양파, 고추", "한식"),
//            MenuInfo("바질 페스토 파스타", "김치, 두부, 파, 양파, 고추", "양식"),
            val resultMenu = MenuInfo("바질 페스토 파스타", "김치, 두부, 파, 양파, 고추", "양식")
            val intent = Intent(activity, IngredientRecommendResultActivity::class.java)
            intent.putExtra("object", resultMenu)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        lifecycleScope.launch {
            tempList.clear()
            ingredientOuterAdapter.clickedData.forEach {
                tempList.add(it)
            }

            viewModel.addCheckedIngredientInfo(tempList)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initClassificationRecycler() {
        // 재료 classification RecyclerView 초기화
        val classificationAdapter =
            ClassificationAdapter(object : ClassificationAdapter.ClassificationListener {
                override fun changeCurrentClassification(classification: String) {
                    // classification 버튼을 클릭하여 현재 classification 변경 시
                    viewModel.selectClassification(classification)
                }
            })

        // ViewModel에 선언되어 있는 classificationList를 추가
        viewModel.classificationList.value?.forEach { classificationAdapter.datas.add(it) }

        binding.classificationRecyclerView.apply {
            setHasFixedSize(true)
            adapter = classificationAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    // 선택할 재료를 보여주는 RecyclerView
    private fun initIngredientsRecycler() {
        linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerMain.apply {
            adapter = ingredientOuterAdapter
            layoutManager = linearLayoutManager

            // Scroll 이벤트 추가 -> MainActivity Bottom Bar 를 컨트롤하기 위해 MainActivity 에 Listener 로 알림
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (dy > 0) {
                        // 아래로 스크롤 시 Bottom Bar 사라짐
                        (activity as? MainActivity)?.mainActivityListener?.changeBottomBarStatus(
                            BOTTOM_BAR_STATUS_HIDE
                        )
                    } else {
                        // 위로 스크롤 시 Bottom Bar 나타남
                        (activity as? MainActivity)?.mainActivityListener?.changeBottomBarStatus(
                            BOTTOM_BAR_STATUS_SHOW
                        )
                    }
                }
            })
        }
    }
}