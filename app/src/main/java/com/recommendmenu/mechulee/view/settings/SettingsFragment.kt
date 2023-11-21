package com.recommendmenu.mechulee.view.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.recommendmenu.mechulee.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(layoutInflater)

        // 오픈소스 라이센스 화면으로 전환
        binding.openSourceText.setOnClickListener {
            val intent = Intent(activity, OpensourceLicenseActivity::class.java)
            startActivity(intent)
        }

        // 이미지 라이센스 화면으로 전환
        binding.imageText.setOnClickListener {
            val intent = Intent(activity, ImageLicenseActivity::class.java)
            startActivity(intent)
        }

        // 리뷰 남기기 화면 이동
        binding.reviewText.setOnClickListener {
            val uriName = Uri.parse("market://details?id=com.recommendmenu.mechulee&hl=ko-KR")
            val intent = Intent(Intent.ACTION_VIEW, uriName)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}