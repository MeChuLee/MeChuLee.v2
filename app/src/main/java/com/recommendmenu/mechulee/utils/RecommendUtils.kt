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

    // 비슷한 메뉴를 추천받는 메소드
    // 카테고리가 같은 메뉴가 5개 이하면 그냥 그대로 그 갯수만큼 출력
    // 많으면 랜덤으로 5개의 index를 뽑아 그 위치에 해당하는 메뉴 출력
    fun recommendOtherMenu(nowMenu: MenuInfo): ArrayList<MenuInfo> {
        val tempTotalList = NetworkUtils.totalMenuList.toCollection(ArrayList())
        tempTotalList.remove(nowMenu)
        val nowCategory = nowMenu.category
        val sameCategoryList = ArrayList<MenuInfo>()
        tempTotalList.forEach {
            if (it.category == nowCategory) {
                sameCategoryList.add(it)
            }
        }
        val numList = ArrayList<Int>()
        if (sameCategoryList.size <= 5) {
            numList.addAll(0 until sameCategoryList.size)
        } else {
            while (numList.size < 5) {
                val intRandom = (0 until sameCategoryList.size).random()
                if (intRandom !in numList) {
                    numList.add(intRandom)
                }
            }
        }
        val tempOtherMenuList = ArrayList<MenuInfo>()
        for (i in numList) {
            tempOtherMenuList.add(sameCategoryList[i])
        }
        return tempOtherMenuList
    }
}