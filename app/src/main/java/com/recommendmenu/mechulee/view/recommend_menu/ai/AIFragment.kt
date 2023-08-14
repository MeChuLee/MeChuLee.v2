package com.recommendmenu.mechulee.view.recommend_menu.ai

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.recommendmenu.mechulee.databinding.FragmentAiBinding
import com.recommendmenu.mechulee.view.ingredient_rate.IngredientActivity

class AIFragment : Fragment() {

    private var _binding: FragmentAiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAiBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 버튼 클릭 리스너 설정
        binding.AIbutton.setOnClickListener {
            // 액티비티로 전환하는 Intent 생성
            val intent = Intent(activity, IngredientActivity::class.java)
            startActivity(intent) // 액티비티로 전환
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}