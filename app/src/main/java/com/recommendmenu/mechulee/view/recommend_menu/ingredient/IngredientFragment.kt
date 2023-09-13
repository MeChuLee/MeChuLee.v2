package com.recommendmenu.mechulee.view.recommend_menu.ingredient

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.databinding.FragmentIngredientBinding
import com.recommendmenu.mechulee.model.data.MenuInfo
import com.recommendmenu.mechulee.utils.Constants.BOTTOM_BAR_STATUS_HIDE
import com.recommendmenu.mechulee.utils.Constants.BOTTOM_BAR_STATUS_SHOW
import com.recommendmenu.mechulee.view.MainActivity
import com.recommendmenu.mechulee.view.recommend_menu.ingredient.adapter.ClassificationAdapter
import com.recommendmenu.mechulee.view.recommend_menu.ingredient.adapter.IngredientOuterAdapter
import com.recommendmenu.mechulee.proto.checkedIngredientDataStore

class IngredientFragment : Fragment() {
    private var _binding: FragmentIngredientBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: IngredientViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager

    private var classificationAdapter: ClassificationAdapter? =
        ClassificationAdapter(object : ClassificationAdapter.ClassificationListener {
            override fun changeCurrentClassification(classification: String) {
                // classification 버튼을 클릭하여 현재 classification 변경 시
                viewModel.selectClassification(classification)
            }
        })

    private var ingredientOuterAdapter: IngredientOuterAdapter? =
        IngredientOuterAdapter(object : IngredientOuterAdapter.IngredientOuterListener {
            override fun clickIngredient(clickedIngredient: String) {
                viewModel.checkSelectedIngredient(clickedIngredient)
            }
        })

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
        // 재료 보여주는 RecyclerView
        initIngredientsRecycler()

        // 추천 받기 버튼 누르면 결과 화면으로 이동
        binding.selectButton.setOnClickListener {
            // 밑에 2줄 중 원하는 MenuInfo로 선택해서 하드코딩으로 넣기
//            MenuInfo("된장찌개", "김치, 두부, 파, 양파, 고추", "한식"),
//            MenuInfo("바질 페스토 파스타", "김치, 두부, 파, 양파, 고추", "양식"),
            val resultMenu = MenuInfo("바질페스토파스타", "김치, 두부, 파, 양파, 고추", "양식")
            val intent = Intent(activity, AIRecommendResultActivity::class.java)
            intent.putExtra("object", resultMenu)
            startActivity(intent)
        }

        binding.circleSelectButton.setOnClickListener {
            val resultMenu = MenuInfo("바질페스토파스타", "김치, 두부, 파, 양파, 고추", "양식")
            val intent = Intent(activity, AIRecommendResultActivity::class.java)
            intent.putExtra("object", resultMenu)
            startActivity(intent)
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

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                // Handle touch event if needed
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
                // Handle request disallow intercept event if needed
            }
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
        val fadeIn = ObjectAnimator.ofFloat(binding.circleSelectButton, "alpha", 0f, 1f).apply {
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

        linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerMain.apply {
            adapter = ingredientOuterAdapter
            layoutManager = linearLayoutManager

            // Scroll 이벤트 추가 -> MainActivity Bottom Bar 를 컨트롤하기 위해 MainActivity 에 Listener 로 알림
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

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
            })
        }
    }

    override fun onResume() {
        super.onResume()

        // ViewPager 여러번 전환 시 MotionLayout 이 제대로 동작하지 않는 오류가 있어
        // MotionLayout Transition 에 대해 초기화 과정 수행 (끊겨보일 수 있음)
        binding.motionLayout.setTransition(R.id.motionLayoutTransition)
        binding.motionLayout.progress = 0f
    }

    // onPause시에 DataStore에 저장
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