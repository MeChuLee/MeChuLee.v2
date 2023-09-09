package com.recommendmenu.mechulee.view.recommend_menu.ingredient

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orhanobut.logger.Logger
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.databinding.FragmentIngredientBinding
import com.recommendmenu.mechulee.utils.constant.Constants.BOTTOM_BAR_STATUS_HIDE
import com.recommendmenu.mechulee.utils.constant.Constants.BOTTOM_BAR_STATUS_SHOW
import com.recommendmenu.mechulee.view.MainActivity
import com.recommendmenu.mechulee.view.recommend_menu.ingredient.adapter.ClassificationAdapter
import com.recommendmenu.mechulee.view.recommend_menu.ingredient.adapter.IngredientOuterAdapter

class IngredientFragment : Fragment() {

    private var _binding: FragmentIngredientBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: IngredientViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val ingredientOuterAdapter = IngredientOuterAdapter()

    private var isButtonAbove = true
    private var isButtonExpanded = false

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIngredientBinding.inflate(layoutInflater)

        viewModel = ViewModelProvider(this)[IngredientViewModel::class.java]

        // 재료 classification 보여주는 RecyclerView
        initClassificationRecycler()

        // classification 선택으로 변경을 감지 시 선택한 classification의 재료를 RecyclerView에 반영
        viewModel.selectClassificationMap.observe(requireActivity()) {
            ingredientOuterAdapter.itemList = it

            ingredientOuterAdapter.notifyDataSetChanged()
        }

        // 아래로 Scroll down 시에는 버튼을 크게
        // Scroll up 시에는 버튼을 작게 버튼의 크기를 변경하는 부분
//        binding.recyclerMain.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//
//        })

//        viewModel.updatedClickedIngredients.observe(viewLifecycleOwner) { updatedIngredients ->
//            println(updatedIngredients)
        // 수정된 clicked 값들을 이용하여 화면을 업데이트하는 작업 수행
        // updatedIngredients 리스트에는 수정된 clicked 값들이 들어있습니다.
//        }

        // 재료 보여주는 RecyclerView
        initIngredientsRecycler()

        binding.selectButton.setOnClickListener {
            val intent = Intent(activity, IngredientRecommendResultActivity::class.java)
            startActivity(intent)
        }

        return binding.root
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

                    if (binding.motionLayout.currentState == R.id.start) {
                        Logger.e("START 상태")
                    } else if (binding.motionLayout.currentState == R.id.end) {
                        Logger.e("END 상태")
                    } else {
                        Logger.e("모르는 상태 ${binding.motionLayout.currentState}")
                    }

                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager

                    // 스크롤을 위로 올렸을 경우, 첫 번째 항목이 완전히 보이는지 확인 (맨 위까지 스크롤),
                    // 버벅거림 방지를 위해 transition 상태가 확인 후,
                    // 현재 애니메이션이 진행되고 있지 않다면 motion transition 수행
                    if (dy < 0
                        && layoutManager.findFirstCompletelyVisibleItemPosition() == 0
                        && binding.motionLayout.currentState == R.id.end
                        && (binding.motionLayout.progress >= 1f
                                || binding.motionLayout.progress <= 0f)
                    ) {
                        binding.motionLayout.transitionToStart()

                        // 위로 스크롤 시 Bottom Bar 나타남
                        (activity as? MainActivity)?.mainActivityListener?.changeBottomBarStatus(
                            BOTTOM_BAR_STATUS_SHOW
                        )
                    }

                    // 스크롤을 아래로 내렸을 경우, 버벅거림 방지를 위해 transition 상태가 확인 후,
                    // 현재 애니메이션이 진행되고 있지 않다면 motion transition 수행
                    if (dy > 0
                        && binding.motionLayout.currentState == R.id.start
                        && (binding.motionLayout.progress >= 1f
                                || binding.motionLayout.progress <= 0f)
                    ) {
                        binding.motionLayout.transitionToEnd()

                        // 아래로 스크롤 시 Bottom Bar 사라짐
                        (activity as? MainActivity)?.mainActivityListener?.changeBottomBarStatus(
                            BOTTOM_BAR_STATUS_HIDE
                        )
                    }
                }
            })
        }
    }

    private fun expandButton() {
        // 버튼을 크게 만드는 메소드
        if (!isButtonExpanded) {
            val layoutParams = binding.selectButton.layoutParams
            layoutParams.height = resources.getDimensionPixelSize(R.dimen.expanded_button_height)
            layoutParams.width = resources.getDimensionPixelSize(R.dimen.expanded_button_width)
            binding.selectButton.layoutParams = layoutParams
            isButtonExpanded = true
            isButtonAbove = false

        }
    }

    private fun shrinkButton() {
        // 버튼을 원래의 크기로 돌리는 메소드
        if (isButtonExpanded) {
            val layoutParams = binding.selectButton.layoutParams
            layoutParams.height = resources.getDimensionPixelSize(R.dimen.original_button_height)
            layoutParams.width = resources.getDimensionPixelSize(R.dimen.original_button_width)
            binding.selectButton.layoutParams = layoutParams
            isButtonExpanded = false
            isButtonAbove = true
        }
    }

    // button의 margin bottom을 조정해 위치 조정하려는 의도
//    private fun updateButtonMargin() {
//        val layoutParams = binding.selectButton.layoutParams as ConstraintLayout.LayoutParams
//        if (isButtonAbove) {
//            layoutParams.bottomMargin =
//                resources.getDimensionPixelSize(R.dimen.initial_button_margin_bottom)
//        } else {
//            layoutParams.bottomMargin =
//                resources.getDimensionPixelSize(R.dimen.scrolled_button_margin_bottom)
//        }
//        binding.selectButton.layoutParams = layoutParams
//    }
}