package com.recommendmenu.mechulee.view.recommend_menu.ingredient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.selectingredients.ClassificationAdapter
import com.recommendmenu.mechulee.databinding.FragmentIngredientBinding
import com.recommendmenu.mechulee.view.menu_list.MenuListViewModel

class IngredientFragment : Fragment() {

    private var _binding: FragmentIngredientBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: IngredientViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIngredientBinding.inflate(layoutInflater)

        viewModel = ViewModelProvider(this)[IngredientViewModel::class.java]

        initClassificationRecycler()

        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 재료 카테고리 RecyclerView
    private fun initClassificationRecycler() {
        val classificationAdapter = ClassificationAdapter(object: ClassificationAdapter.ClassificationListener {
            override fun changeCurrentClassification(classification: String) {
                viewModel.selectClassification(classification)
            }
        })
        viewModel.classificationList.value?.forEach { classificationAdapter.datas.add(it) }
        linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.classificationRecyclerView.apply {
            adapter = classificationAdapter
            layoutManager = linearLayoutManager
        }
    }
}