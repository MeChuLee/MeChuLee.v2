package com.recommendmenu.mechulee.view.result.ingredient

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.databinding.ActivityIngredientResultBinding
import com.recommendmenu.mechulee.model.data.MenuInfo
import com.recommendmenu.mechulee.proto.checkedIngredientDataStore
import com.recommendmenu.mechulee.utils.Constants
import com.recommendmenu.mechulee.utils.NetworkUtils
import com.recommendmenu.mechulee.view.result.ingredient.adapter.IngredientMenuRecyclerViewAdapter
import com.recommendmenu.mechulee.view.result.menu.MenuResultActivity

class IngredientResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIngredientResultBinding
    private lateinit var viewModel: IngredientResultViewModel

    private lateinit var ingredientMenuRecyclerViewAdapter: IngredientMenuRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIngredientResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[IngredientResultViewModel::class.java]
        viewModel.create(checkedIngredientDataStore)

        viewModel.recommendResultList.observe(this) { recommendResultList ->
            initRecyclerView(recommendResultList)
        }

        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
    }

    private fun initRecyclerView(recommendResultList: ArrayList<Pair<MenuInfo, ArrayList<Int>>>) {
        if (recommendResultList.isNotEmpty()) {
            binding.ingredientMenuRecyclerViewEmptyView.visibility = View.GONE
        }

        ingredientMenuRecyclerViewAdapter = IngredientMenuRecyclerViewAdapter(object :
            IngredientMenuRecyclerViewAdapter.IngredientMenuClickListener {
            // 메뉴 클릭 시 메뉴 결과 화면으로 전환
            override fun menuClick(menu: MenuInfo) {
                val intent = Intent(this@IngredientResultActivity, MenuResultActivity::class.java)
                intent.putExtra(Constants.INTENT_NAME_RESULT, menu)
                startActivity(intent) // 액티비티로 전환
            }
        })

        ingredientMenuRecyclerViewAdapter.menuList = recommendResultList

        binding.ingredientMenuRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(
                this@IngredientResultActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = ingredientMenuRecyclerViewAdapter
        }
    }
}