package com.recommendmenu.mechulee.view.settings

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.naver.maps.map.app.OpenSourceLicenseActivity
import com.recommendmenu.mechulee.databinding.FragmentSettingsBinding
import com.recommendmenu.mechulee.view.recommend_menu.ai.ingredient_rate.IngredientActivity

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingClickEvent()
    }

    private fun settingClickEvent() {
        binding.openSourceText.setOnClickListener {
            val intent = Intent(activity, com.recommendmenu.mechulee.view.settings.OpenSourceLicenseActivity::class.java)
            startActivity(intent)
        }
        binding.imageText.setOnClickListener {
            val intent = Intent(activity, ImageLicenseActivity::class.java)
            startActivity(intent)
        }
        binding.versionText.setOnClickListener {
            val intent = Intent(activity, VersionNotationActivity::class.java)
            startActivity(intent)
        }
        binding.version.setOnClickListener {
            val intent = Intent(activity, VersionNotationActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}