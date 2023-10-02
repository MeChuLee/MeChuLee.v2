package com.recommendmenu.mechulee.view.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.recommendmenu.mechulee.databinding.FragmentSettingsBinding
import com.recommendmenu.mechulee.utils.Constants

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(layoutInflater)

        // 각 라이센스 클릭 시 intent 타입 정보 저장 후 화면 전환
        binding.openSourceText.setOnClickListener {
            val intent = Intent(activity, LicenseActivity::class.java)
            intent.putExtra(Constants.INTENT_NAME_LICENSE, Constants.INTENT_VALUE_LICENSE_OPENSOURCE)
            startActivity(intent)
        }

        binding.imageText.setOnClickListener {
            val intent = Intent(activity, LicenseActivity::class.java)
            intent.putExtra(Constants.INTENT_NAME_LICENSE, Constants.INTENT_VALUE_LICENSE_IMAGE)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}