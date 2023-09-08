package com.recommendmenu.mechulee.view.recommend_menu.ai

import android.content.Intent
import android.os.Bundle
import android.text.TextPaint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.magicgoop.tagsphere.OnTagLongPressedListener
import com.magicgoop.tagsphere.OnTagTapListener
import com.magicgoop.tagsphere.item.TagItem
import com.magicgoop.tagsphere.item.TextTagItem
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.databinding.FragmentAiBinding
import com.recommendmenu.mechulee.view.MainActivity
import com.recommendmenu.mechulee.view.recommend_menu.ai.ingredient_rate.IngredientActivity
import com.recommendmenu.mechulee.view.recommend_menu.ai.util.EmojiConstants
import kotlin.random.Random


class AIFragment : Fragment(), OnTagLongPressedListener, OnTagTapListener {

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

        binding.temperature.text
        binding.weatherText.text
        binding.locationText.text
        binding.weatherAnimation.setAnimation(R.raw.weather_cloud_rain_animation)
        // 함수로 변경해서 되는지 확인해보기

        // CustomNestedScrollView 스크롤 함수 정의 -> BottomNavigationBar 내려감
        binding.aiNestedScrollView.onBottomBarStatusChange = { status ->
            (activity as? MainActivity)?.mainActivityListener?.changeBottomBarStatus(status)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        aiButtonClickEvent()
        initTagView()
        // ViewModel에 있는 강수형태, 하늘상태, 기온에 따라 Weather카드의 text요소가 변경되는 함수
        initWeatherCardContents()
    }

    private fun initWeatherCardContents() {

    }

    private fun initTagView() {
        val samples = EmojiConstants.emojiCodePoints.size - 1
        (0..120).map {
            TextTagItem(
                text = String(
                    Character.toChars(EmojiConstants.emojiCodePoints[Random.nextInt(samples)])
                )
            )
        }.toList().let {
            binding.tagView.addTagList(it)
        }
        binding.tagView.setTextPaint(
            TextPaint().apply {
                isAntiAlias = true
                textSize = resources.getDimension(R.dimen.tag_text_size)
            }
        )
        binding.tagView.setOnLongPressedListener(this)
        binding.tagView.setOnTagTapListener(this)
    }

    private fun aiButtonClickEvent(){
        // 버튼 클릭 리스너 설정
        binding.tagView.setOnClickListener {
            // 액티비티로 전환하는 Intent 생성
            val intent = Intent(activity, IngredientActivity::class.java)
            startActivity(intent) // 액티비티로 전환
        }
    }

    override fun onLongPressed(tagItem: TagItem) {

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