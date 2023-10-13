package com.recommendmenu.mechulee.view.recommend_menu.ingredient

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.databinding.FragmentIngredientBinding
import com.recommendmenu.mechulee.utils.Constants.BOTTOM_BAR_STATUS_HIDE
import com.recommendmenu.mechulee.utils.Constants.BOTTOM_BAR_STATUS_SHOW
import com.recommendmenu.mechulee.view.MainActivity
import com.recommendmenu.mechulee.view.recommend_menu.ingredient.adapter.ClassificationAdapter
import com.recommendmenu.mechulee.view.recommend_menu.ingredient.adapter.IngredientOuterAdapter
import com.recommendmenu.mechulee.proto.checkedIngredientDataStore
import com.recommendmenu.mechulee.view.result.ingredient.IngredientResultActivity

class IngredientFragment : Fragment() {
    private var _binding: FragmentIngredientBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: IngredientViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager

    private var backPressedTime: Long = 0
    private lateinit var fadeIn: ObjectAnimator
    private lateinit var callback: OnBackPressedCallback

    // 재료 분류 어댑터
    private var classificationAdapter: ClassificationAdapter? =
        ClassificationAdapter(object : ClassificationAdapter.ClassificationListener {
            override fun changeCurrentClassification(classification: String) {
                // classification 버튼을 클릭하여 현재 classification 변경 시
                viewModel.selectClassification(classification)

                // 재료 RecyclerView 의 스크롤 이벤트 추가
                binding.recyclerMain.addOnScrollListener(scrollListener)
            }
        })

    // 재료 분류별 RecyclerView 어댑터
    private var ingredientOuterAdapter: IngredientOuterAdapter? =
        IngredientOuterAdapter(object : IngredientOuterAdapter.IngredientOuterListener {
            override fun clickIngredient(clickedIngredient: String) {
                viewModel.checkSelectedIngredient(clickedIngredient)
            }
        })

    // RecyclerView Scroll Listener
    private lateinit var scrollListener: OnScrollListener

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIngredientBinding.inflate(layoutInflater)
        val ingredientViewModelFactory =
            IngredientViewModelFactory(requireContext().checkedIngredientDataStore)
        viewModel =
            ViewModelProvider(this, ingredientViewModelFactory)[IngredientViewModel::class.java]

        // 재료 classification 보여주는 RecyclerView
        initClassificationRecycler()
        // 재료 보여주는 RecyclerView
        initIngredientsRecycler()

        viewModel.ready()

        // classification 선택으로 변경을 감지 시 선택한 classification의 재료를 RecyclerView에 반영
        viewModel.selectedMap.observe(requireActivity()) { nowMap ->
            ingredientOuterAdapter?.outerMap = nowMap.toMutableMap()
            ingredientOuterAdapter?.notifyDataSetChanged()
        }

        // 재료 선택 또는 해제 시에 변경을 감지 시에 outer Adapter에 바뀐 리스트 넘겨줌
        viewModel.selectedIngredientList.observe(requireActivity()) { selectedIngredients ->
            ingredientOuterAdapter?.outerSelectedIngredientList?.clear()
            ingredientOuterAdapter?.outerSelectedIngredientList?.addAll(selectedIngredients)
            ingredientOuterAdapter?.notifyDataSetChanged()

        }

        // 추천 받기 버튼 누르면 결과 화면으로 이동
        binding.selectButton.setOnClickListener {
            startActivity(Intent(requireContext(), IngredientResultActivity::class.java))
        }

        binding.circleSelectButton.setOnClickListener {
            startActivity(Intent(requireContext(), IngredientResultActivity::class.java))
        }

        return binding.root
    }

    // 재료 classification RecyclerView 초기화
    @SuppressLint("NotifyDataSetChanged")
    private fun initClassificationRecycler() {
        // 재료 분류 쪽 스크롤 시 fragment가 넘어가지지 않도록 방지
        binding.classificationRecyclerView.addOnItemTouchListener(object :
            RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                rv.parent.requestDisallowInterceptTouchEvent(true)
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })

        viewModel.classificationList.observe(requireActivity()) {
            classificationAdapter?.datas?.clear()
            classificationAdapter?.datas?.addAll(it)
            classificationAdapter?.notifyDataSetChanged()
        }

        binding.classificationRecyclerView.apply {
            setHasFixedSize(true)
            adapter = classificationAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    // 선택할 재료를 보여주는 RecyclerView
    private fun initIngredientsRecycler() {
        // 버튼을 서서히 사라지게 하는 애니메이션
        val fadeOut = ObjectAnimator.ofFloat(binding.circleSelectButton, "alpha", 1f, 0f).apply {
            duration = 1000 // 애니메이션 지속 시간 설정 (1초)
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    binding.circleSelectButton.isEnabled = false
                }

                override fun onAnimationEnd(animation: Animator) {}

                override fun onAnimationCancel(animation: Animator) {}

                override fun onAnimationRepeat(animation: Animator) {}
            })
        }

        // 버튼을 다시 나타나게 하는 애니메이션
        fadeIn = ObjectAnimator.ofFloat(binding.circleSelectButton, "alpha", 0f, 1f).apply {
            duration = 1000 // 애니메이션 지속 시간 설정 (1초)
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {
                    binding.circleSelectButton.isEnabled = true // 애니메이션이 끝나면 버튼 클릭 가능
                }

                override fun onAnimationCancel(animation: Animator) {}

                override fun onAnimationRepeat(animation: Animator) {}
            })
        }

        // Scroll 이벤트 추가
        // 1. MainActivity Bottom Bar 를 컨트롤하기 위해 MainActivity 에 Listener 로 알림
        // 2. 스크롤 시 상단 에 '추천 받기' 버튼 생성 및 제거
        // 3. 스크롤 시 우측 상단에 엄지 모양 '추천' 버튼 생성 및 제거
        scrollListener = object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // 스크롤을 위로 올렸을 경우, 첫 번째 항목이 완전히 보이는지 확인 (맨 위까지 스크롤),
                // 버벅거림 방지를 위해 transition 상태가 확인 후,
                // 현재 애니메이션이 진행되고 있지 않다면 motion transition 수행
                if (dy < 0
                    && binding.motionLayout.currentState == R.id.end
                    && (binding.motionLayout.progress >= 1f
                            || binding.motionLayout.progress <= 0f)
                ) {
                    fadeIn.start()

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
                    fadeOut.start()

                    binding.motionLayout.transitionToEnd()

                    // 아래로 스크롤 시 Bottom Bar 사라짐
                    (activity as? MainActivity)?.mainActivityListener?.changeBottomBarStatus(
                        BOTTOM_BAR_STATUS_HIDE
                    )
                }
            }
        }

        linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerMain.apply {
            adapter = ingredientOuterAdapter
            layoutManager = linearLayoutManager
            addOnScrollListener(scrollListener)
        }
    }

    // 아래로 스크롤 시 생성되는 큰 추천 버튼,
    // 사라지는 작은 추천 버튼 및 bottom navigator를 원래 상태로 복구
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    requireActivity().finish()
                } else {
                    fadeIn.start()
                    binding.motionLayout.transitionToStart()
                    (activity as? MainActivity)?.mainActivityListener?.changeBottomBarStatus(
                        BOTTOM_BAR_STATUS_SHOW
                    )
                    Toast.makeText(requireContext(), "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
                }
                backPressedTime = System.currentTimeMillis()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    override fun onResume() {
        super.onResume()

        // ViewPager 여러번 전환 시 MotionLayout 이 제대로 동작하지 않는 오류가 있어
        // MotionLayout Transition 에 대해 초기화 과정 수행 (끊겨보일 수 있음)
        binding.motionLayout.setTransition(R.id.motionLayoutTransition)
        binding.motionLayout.progress = 0f
        binding.circleSelectButton.alpha = 1f
        binding.circleSelectButton.isEnabled = true
    }

    override fun onPause() {
        super.onPause()
        viewModel.storeSelectedIngredient()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        classificationAdapter = null
        ingredientOuterAdapter = null
    }
}