package com.recommendmenu.mechulee.view.result.menu

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.databinding.ActivityMenuResultBinding
import com.recommendmenu.mechulee.model.data.MenuInfo
import com.recommendmenu.mechulee.proto.likedMenuDataStore
import com.recommendmenu.mechulee.utils.Constants.INTENT_NAME_RESULT
import com.recommendmenu.mechulee.view.result.menu.adapter.MenuResultIngredientAdapter
import com.recommendmenu.mechulee.view.result.menu.adapter.MenuResultOtherMenuAdapter

class MenuResultActivity : AppCompatActivity() {
    private var _binding: ActivityMenuResultBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MenuResultViewModel
    private lateinit var result: String
    private val storedDataList = ArrayList<String>()
    private var isLiked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding =
            DataBindingUtil.setContentView(this, R.layout.activity_menu_result)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        val resultViewModelFactory = MenuResultViewModelFactory(likedMenuDataStore)
        viewModel = ViewModelProvider(this, resultViewModelFactory)[MenuResultViewModel::class.java]

        val resultMenu: MenuInfo? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(INTENT_NAME_RESULT, MenuInfo::class.java)
        } else {
            intent.getSerializableExtra(INTENT_NAME_RESULT) as MenuInfo?
        }

        binding.heartIcon.setOnClickListener {
            isLiked = !isLiked
            viewModel.checkLikeMenu(result)
            animateHeart(isLiked)
        }

        if (resultMenu != null) {
            // nowResult에 저장
            viewModel.showResult(resultMenu)
            viewModel.nowResult.let {
                result = it.name
                binding.resultMenuName.text = result
                viewModel.ready()
            }

            // DataStore에 저장된 메뉴인지 확인
            viewModel.likedMenuList.observe(this) {
                storedDataList.clear()
                storedDataList.addAll(it)

                storedDataList.let { localDataStore ->
                    if (result in localDataStore) {
                        isLiked = true
                    }
                    animateHeart(isLiked)
                }
            }

            // 재료 리스트 연결
            viewModel.ingredientList.let {
                val menuResultIngredientAdapter = MenuResultIngredientAdapter()
                it.forEach { myIngredient ->
                    menuResultIngredientAdapter.ingredientList.add(myIngredient)
                }
                binding.ingredientList.apply {
                    adapter = menuResultIngredientAdapter
                    layoutManager = GridLayoutManager(context, 5)
                    setHasFixedSize(true)
                }
            }

            // 비슷한 메뉴 연결
            viewModel.otherList.observe(this) {
                val menuResultOtherMenuAdapter = MenuResultOtherMenuAdapter()
                it.forEach { otherMenu ->
                    menuResultOtherMenuAdapter.datas.add(otherMenu)
                }
                binding.otherMenuRecycler.apply {
                    setHasFixedSize(true)
                    adapter = menuResultOtherMenuAdapter
                    layoutManager =
                        LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
                }
            }
        }
    }

    // 하트 애니메이션
    private fun animateHeart(isLiked: Boolean) {
        if (isLiked) {
            binding.lottieAnimation.setAnimation(R.raw.heart_click)
            binding.lottieAnimation.playAnimation()
            binding.lottieAnimation.visibility = View.VISIBLE
        } else {
            binding.lottieAnimation.visibility = View.INVISIBLE
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.storeLikeMenu()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}