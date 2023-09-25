package com.recommendmenu.mechulee.utils

import com.recommendmenu.mechulee.model.data.MenuInfo
import java.util.Random

object RecommendUtils {
    // 랜덤으로 메뉴 추천
    fun recommendRandom(): MenuInfo {
        val random = Random()
        val randIdx = random.nextInt(NetworkUtils.totalMenuList.size)

        return NetworkUtils.totalMenuList[randIdx]
    }
}