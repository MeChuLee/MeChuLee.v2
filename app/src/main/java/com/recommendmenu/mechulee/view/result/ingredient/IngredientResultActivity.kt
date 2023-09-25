package com.recommendmenu.mechulee.view.result.ingredient

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.databinding.ActivityIngredientResultBinding
import com.recommendmenu.mechulee.utils.NetworkUtils
import com.recommendmenu.mechulee.view.result.ingredient.adapter.IngredientMenuRecyclerViewAdapter

class IngredientResultActivity: AppCompatActivity() {

    private lateinit var binding: ActivityIngredientResultBinding

    private lateinit var ingredientMenuRecyclerViewAdapter: IngredientMenuRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIngredientResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()

        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
    }

    private fun initRecyclerView() {
        ingredientMenuRecyclerViewAdapter = IngredientMenuRecyclerViewAdapter()
        ingredientMenuRecyclerViewAdapter.menuList = ArrayList()
        ingredientMenuRecyclerViewAdapter.menuList.apply {
            add(NetworkUtils.totalMenuList[0])
            add(NetworkUtils.totalMenuList[1])
            add(NetworkUtils.totalMenuList[2])
            add(NetworkUtils.totalMenuList[3])
            add(NetworkUtils.totalMenuList[4])
        }

        binding.ingredientMenuRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@IngredientResultActivity, LinearLayoutManager.VERTICAL, false)
            adapter = ingredientMenuRecyclerViewAdapter
        }
    }
}