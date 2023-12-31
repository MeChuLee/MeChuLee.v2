package com.recommendmenu.mechulee.view.recommend_menu.ai

import android.content.Intent
import android.os.Bundle
import android.text.TextPaint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.magicgoop.tagsphere.OnTagTapListener
import com.magicgoop.tagsphere.item.TagItem
import com.magicgoop.tagsphere.item.TextTagItem
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.databinding.FragmentAiBinding
import com.recommendmenu.mechulee.utils.EmojiConstantUtils
import com.recommendmenu.mechulee.view.MainActivity
import com.recommendmenu.mechulee.view.recommend_menu.ai.ingredient_rate.IngredientActivity

class AIFragment : Fragment(), OnTagTapListener {

    private var _binding: FragmentAiBinding? = null
    private val binding get() = _binding!!
    private lateinit var aiViewModel: AIViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAiBinding.inflate(layoutInflater)

        aiViewModel = ViewModelProvider(this)[AIViewModel::class.java]

        aiViewModel.weatherInfo.observe(requireActivity()) {
            binding.weatherText.text = it.sky
            val rainType = it.rainType

            if (rainType == "비" || rainType == "비/눈" || rainType == "빗방울") {
                binding.weatherAnimation.setAnimation(R.raw.weather_cloud_rain_animation)
            } else if (rainType == "눈" || rainType == "빗방울눈날림" || rainType == "눈날림") {
                binding.weatherAnimation.setAnimation(R.raw.weather_snow_animation)
            } else if (it.sky == "맑음") {
                binding.weatherAnimation.setAnimation(R.raw.weather_sun_animation)
            } else if (it.sky == "구름많음") {
                binding.weatherAnimation.setAnimation(R.raw.weather_cloudcloud_animation)
            } else if (it.sky == "흐림") {
                binding.weatherAnimation.setAnimation(R.raw.weather_cloud_animation)
            }

            binding.weatherAnimation.playAnimation() // 플레이 해줘야함
            binding.temperature.text = it.temp
        }

        aiViewModel.location.observe(requireActivity()) {
            binding.locationText.text = it
        }

        // CustomNestedScrollView 스크롤 함수 정의 -> BottomNavigationBar 내려감
        binding.aiNestedScrollView.onBottomBarStatusChange = { status ->
            (activity as? MainActivity)?.mainActivityListener?.changeBottomBarStatus(status)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        
        aiClickEvent()
        initTagView()
    }

    private fun initTagView() {
        val tagList = ArrayList<TextTagItem>()

        for (i in 0 until EmojiConstantUtils.emojiCodePoints.size) {
            val emoji = String(Character.toChars(EmojiConstantUtils.emojiCodePoints[i]))
            val textTagItem = TextTagItem(text = emoji)
            tagList.add(textTagItem)
        }

        binding.tagView.addTagList(tagList)

        binding.tagView.setTextPaint(
            TextPaint().apply {
                isAntiAlias = true
                textSize = resources.getDimension(R.dimen.tag_text_size)
            }
        )
        binding.tagView.setOnTagTapListener(this)
    }

    private fun aiClickEvent() {
        // 버튼 클릭 리스너 설정
        binding.tagView.setOnClickListener {
            // 액티비티로 전환하는 Intent 생성
            val intent = Intent(activity, IngredientActivity::class.java)
            startActivity(intent) // 액티비티로 전환
        }

        binding.rightArrowIcon.setOnClickListener {
            // 액티비티로 전환하는 Intent 생성
            val intent = Intent(activity, IngredientActivity::class.java)
            startActivity(intent) // 액티비티로 전환
        }

        binding.ratingText.setOnClickListener {
            // 액티비티로 전환하는 Intent 생성
            val intent = Intent(activity, IngredientActivity::class.java)
            startActivity(intent) // 액티비티로 전환
        }
    }

    override fun onTap(tagItem: TagItem) {
        val intent = Intent(activity, IngredientActivity::class.java)
        startActivity(intent) // 액티비티로 전환
    }

    override fun onResume() {
        super.onResume()
        binding.tagView.startAutoRotation(-1f, 1f)
    }

    override fun onPause() {
        super.onPause()
        binding.tagView.stopAutoRotation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
