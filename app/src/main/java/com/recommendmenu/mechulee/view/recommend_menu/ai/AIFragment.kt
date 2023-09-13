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
import com.orhanobut.logger.Logger
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.databinding.FragmentAiBinding
import com.recommendmenu.mechulee.utils.EmojiConstantUtils
import com.recommendmenu.mechulee.utils.LocationUtils
import com.recommendmenu.mechulee.view.MainActivity
import com.recommendmenu.mechulee.view.recommend_menu.ai.ingredient_rate.IngredientActivity

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

        // 함수로 변경해서 되는지 확인해보기
        LocationUtils.getLocationGPS(requireActivity(), onResultLocation = { latitude, longitude ->
            aiViewModel.setLocationXY(latitude, longitude)
        })

        // observe대상을 dataClass객체에서 각각의 String값으로 한 이유
        // -> 객체로 하면 반복문 돌다가 요소 하나만나면 바로 observe함수를 비동기로 실행해버려서
        // 객체에 완벽하게 값이 들어오지 않은 채로 UI에 반영이 됨.
        // observe하고 있는 대상은 UI에 직접적으로 반영이 되는 대상으로 하는게 좋을 것 같음
        // 변경을 대상 전체를 하는게 맞는 것 같다.

        aiViewModel.weatherInfo.observe(requireActivity()){
            binding.weatherText.text = it.sky
            val rainType = it.rainType

            if(rainType == "비" || rainType == "비/눈" || rainType == "빗방울"){
                binding.weatherAnimation.setAnimation(R.raw.weather_cloud_rain_animation)
                // continue 없나?
            } else if(rainType == "눈" || rainType == "빗방울눈날림" || rainType == "눈날림"){
                binding.weatherAnimation.setAnimation(R.raw.weather_snow_animation)
            } else if(it.sky == "맑음") {
                binding.weatherAnimation.setAnimation(R.raw.weather_sun_animation)
            } else if(it.sky == "구름많음"){
                binding.weatherAnimation.setAnimation(R.raw.weather_cloudcloud_animation)
            } else if(it.sky == "흐림") {
                binding.weatherAnimation.setAnimation(R.raw.weather_cloud_animation)
            }

            binding.weatherAnimation.playAnimation() // 플레이 해줘야함
            binding.temperature.text = it.temp
        }

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

        val tagList = mutableListOf<TextTagItem>()

        Logger.d(EmojiConstantUtils.emojiCodePoints.size)
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