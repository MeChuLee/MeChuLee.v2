package com.recommendmenu.mechulee.utils

import com.recommendmenu.mechulee.model.data.LicenseInfo

object Constants {
    const val BOTTOM_BAR_STATUS_SHOW = 0
    const val BOTTOM_BAR_STATUS_HIDE = 1

    const val URL_TYPE_MENU = 0
    const val URL_TYPE_INGREDIENT = 1

    const val INTENT_NAME_WEB_URL = "webUrl"
    const val INTENT_NAME_RESULT = "object"

    val OPENSOURCE_LICENSE_LIST = arrayListOf(
        LicenseInfo("lottie", ""),
        LicenseInfo("circleimageview", ""),
        LicenseInfo("expandablebottombar", ""),
        LicenseInfo("magic-goop:tag-sphere", "")
    )

    val MENU_LICENSE_LIST = arrayListOf(
        LicenseInfo("북엇국", "https://www.flickr.com/photos/96349462@N00/564365392") to LicenseInfo("CC BY-SA 2.0", "https://creativecommons.org/licenses/by-sa/2.0/deed.en"),
        LicenseInfo("배추롤", "https://commons.wikimedia.org/wiki/File:Chinese_cabbage_rolls_with_chicken_mince.jpg?uselang=ko") to LicenseInfo("CC BY-SA 4.0", "https://creativecommons.org/licenses/by-sa/4.0"),
        LicenseInfo("콩국수", "https://www.flickr.com/photos/koreanet/27198870013/") to LicenseInfo("CC BY-SA 2.0", "https://creativecommons.org/licenses/by-sa/2.0"),
        LicenseInfo("참치김치찌개", "https://www.flickr.com/photos/kfoodaddict/10165987846/") to LicenseInfo("CC BY 2.0", "https://creativecommons.org/licenses/by/2.0"),
        LicenseInfo("부대찌개", "https://www.flickr.com/photos/bryansjs/44835481475/") to LicenseInfo("CC BY-SA 2.0", "https://creativecommons.org/licenses/by-sa/2.0"),
        LicenseInfo("돼지고기 장조림", "https://www.flickr.com/photos/127584241@N02/15584459417/") to LicenseInfo("CC BY 2.0", "https://creativecommons.org/licenses/by/2.0"),
        LicenseInfo("전복죽", "https://www.flickr.com/photos/bryansjs/45749113321/") to LicenseInfo("CC BY-SA 2.0", "https://creativecommons.org/licenses/by-sa/2.0"),
        LicenseInfo("야채죽", "https://commons.wikimedia.org/wiki/File:Korean_rice_porridge_Vegetable_juk.jpg") to LicenseInfo("CC BY-SA 4.0", "https://creativecommons.org/licenses/by-sa/4.0"),
        LicenseInfo("잔치국수", "https://www.flickr.com/photos/kfoodaddict/7603859406/") to LicenseInfo("CC BY 2.0", "https://creativecommons.org/licenses/by/2.0"),
        LicenseInfo("카레덮밥", "https://commons.wikimedia.org/wiki/File:Beef_curry_rice_003.jpg") to LicenseInfo("CC0", "https://creativecommons.org/publicdomain/zero/1.0/deed.en"),
        LicenseInfo("카레우동", "https://commons.wikimedia.org/wiki/File:Curry_udon_by_hirotomo.jpg?uselang=ko") to LicenseInfo("CC BY-SA 2.0", "https://creativecommons.org/licenses/by-sa/2.0"),
        LicenseInfo("야채볶음밥", "https://commons.wikimedia.org/wiki/File:Fried_rice_in_home.jpg") to LicenseInfo("CC BY-SA 4.0", "https://creativecommons.org/licenses/by-sa/4.0"),
        LicenseInfo("짜장면", "https://www.flickr.com/photos/stuart_spivack/370040691/") to LicenseInfo("CC BY-SA 2.0", "https://creativecommons.org/licenses/by-sa/2.0"),
        LicenseInfo("짬뽕", "https://commons.wikimedia.org/wiki/File:Jjamppong-sujebi_2.jpg?uselang=ko") to LicenseInfo("CC BY-SA 4.0", "https://creativecommons.org/licenses/by-sa/4.0"),
        LicenseInfo("멘보샤", "https://commons.wikimedia.org/wiki/File:Shrimp_toast_from_Tiger's_Cafe_Tuen_Mun.jpg") to LicenseInfo("CC BY-SA 4.0", "https://creativecommons.org/licenses/by-sa/4.0"),
        LicenseInfo("삼겹김치볶음밥", "https://pixabay.com/es/photos/arroz-frito-con-kimchi-arroz-frito-241051/") to LicenseInfo("CC0", "https://creativecommons.org/publicdomain/zero/1.0/deed.en"),
        LicenseInfo("치킨볶음밥", "https://commons.wikimedia.org/wiki/File:Siam_Fried_Rice_with_Chicken_by_Papaya,_Hove.jpg") to LicenseInfo("CC0", "https://creativecommons.org/publicdomain/zero/1.0/deed.en"),
        LicenseInfo("닭갈비", "https://www.flickr.com/photos/nagy/17495384/") to LicenseInfo("CC BY-SA 2.0", "https://creativecommons.org/licenses/by-sa/2.0"),
        LicenseInfo("소고기 주먹밥", "https://www.flickr.com/photos/cc_photoshare/10958054356/") to LicenseInfo("CC BY 2.0", "https://creativecommons.org/licenses/by/2.0"),
        LicenseInfo("탕수욱", "https://commons.wikimedia.org/wiki/File:Tangsuyuk.jpg") to LicenseInfo("CC0", "https://creativecommons.org/publicdomain/zero/1.0/deed.en"),
        LicenseInfo("바지락 칼국수", "https://commons.wikimedia.org/wiki/File:Bajirak-kalguksu_20230126_001.jpg") to LicenseInfo("CC BY-SA 4.0", "https://creativecommons.org/licenses/by-sa/4.0"),
        LicenseInfo("장칼국수", "https://commons.wikimedia.org/wiki/File:Jangkalguksu_20220501_001.jpg") to LicenseInfo("CC BY-SA 4.0", "https://creativecommons.org/licenses/by-sa/4.0"),
        LicenseInfo("콩나물 불고기", "http://bjbjbjbj.tistory.com/9") to LicenseInfo("CC BY 4.0", "https://creativecommons.org/licenses/by/4.0"),
        LicenseInfo("소불고기", "https://commons.wikimedia.org/wiki/File:Bassak-bulgogi.jpg?uselang=ko") to LicenseInfo("CC BY 4.0", "https://creativecommons.org/licenses/by/4.0"),
        LicenseInfo("차돌숙주볶음", "https://commons.wikimedia.org/wiki/File:Fried_Pork_Bean_Sprout.jpg") to LicenseInfo("CC BY-SA 4.0", "https://creativecommons.org/licenses/by-sa/4.0"),
        LicenseInfo("장어구이", "http://ciadfhoz.tistory.com/42") to LicenseInfo("CC BY 4.0", "https://creativecommons.org/licenses/by/4.0"),
        LicenseInfo("삼겹김치찜", "https://commons.wikimedia.org/wiki/File:Mugeun-ji-jjim_1.jpg?uselang=ko") to LicenseInfo("CC BY-SA 4.0", "https://creativecommons.org/licenses/by-sa/4.0"),
        LicenseInfo("뼈해장국", "https://pixabay.com/ko/photos/해장국-국밥-한식-뚝배기-3666599/") to LicenseInfo("CC0", "https://creativecommons.org/publicdomain/zero/1.0/deed.en"),
        LicenseInfo("우거지 감자탕", "https://www.flickr.com/photos/traveloriented/11251558963/") to LicenseInfo("CC BY-SA 2.0", "https://creativecommons.org/licenses/by-sa/2.0"),
        LicenseInfo("갈비탕", "https://www.flickr.com/photos/koreanet/16356184153/") to LicenseInfo("CC BY-SA 2.0", "https://creativecommons.org/licenses/by-sa/2.0"),
        LicenseInfo("순두부찌개", "https://commons.wikimedia.org/wiki/File:Sundubu-jjigae_2.jpg?uselang=ko") to LicenseInfo("CC BY 4.0", "https://creativecommons.org/licenses/by/4.0"),
        LicenseInfo("돼지갈비", "https://blog.naver.com/deljesu/221111946686") to LicenseInfo("CC BY 2.0 KR", "https://creativecommons.org/licenses/by/2.0/kr/deed.en"),
        LicenseInfo("소갈비", "https://www.flickr.com/photos/68710028@N06/31289696908/") to LicenseInfo("CC BY-SA 2.0", "https://creativecommons.org/licenses/by-sa/2.0"),
        LicenseInfo("LA 갈비", "https://commons.wikimedia.org/wiki/File:Korean_BBQ-LA_Galbi-01.jpg?uselang=ko") to LicenseInfo("CC BY 2.0", "https://creativecommons.org/licenses/by/2.0"),
        LicenseInfo("프렌치토스트", "https://www.flickr.com/photos/ruthanddave/10294904906/") to LicenseInfo("CC BY 2.0", "https://creativecommons.org/licenses/by/2.0"),
        LicenseInfo("피자토스트", "https://commons.wikimedia.org/wiki/File:Pizza_toast,_at_Gusto.jpg") to LicenseInfo("CC BY-SA 3.0", "https://creativecommons.org/licenses/by-sa/3.0"),
        LicenseInfo("간장치킨", "https://www.flickr.com/photos/97247234@N00/3547603486/") to LicenseInfo("CC BY 2.0", "https://creativecommons.org/licenses/by/2.0"),
        LicenseInfo("오므라이스", "https://www.flickr.com/photos/kanesue/50448824236/") to LicenseInfo("CC BY 2.0", "https://creativecommons.org/licenses/by/2.0"),
        LicenseInfo("돼지곱창볶음", "https://commons.wikimedia.org/wiki/File:양념곱창.JPG") to LicenseInfo("CC BY-SA 3.0", "https://creativecommons.org/licenses/by-sa/3.0"),
        LicenseInfo("갈치조림", "http://semizzang.tistory.com/4") to LicenseInfo("CC BY 4.0", "https://creativecommons.org/licenses/by/4.0"),
        LicenseInfo("", "") to LicenseInfo("", "")
    )

    val INGREDIENT_LICENSE_LIST = arrayListOf(
        LicenseInfo("상추 아이콘 제작자: Freepik - Flaticon", "https://www.flaticon.com/kr/free-icon/lettuce_5346093?term=상추&page=1&position=13&origin=search&related_id=5346093"),
        LicenseInfo("고기 아이콘 제작자: DinosoftLabs - Flaticon", "https://www.flaticon.com/kr/free-icon/beef_3143643?term=소고기&page=1&position=1&origin=search&related_id=3143643"),
        LicenseInfo("버섯 아이콘 제작자: popo2021 - Flaticon", "https://www.flaticon.com/kr/free-icon/mushroom_8886168?term=버섯&page=1&position=4&origin=search&related_id=8886168"),
        LicenseInfo("양고기 아이콘 제작자: Freepik - Flaticon", "https://www.flaticon.com/kr/free-icon/lamb_4366625?term=양고기&page=1&position=2&origin=search&related_id=4366625"),
        LicenseInfo("케첩 아이콘 제작자: Freepik - Flaticon", "https://www.flaticon.com/kr/free-icon/ketchup_877824?term=케첩&page=1&position=3&origin=search&related_id=877824"),
        LicenseInfo("소시지 아이콘 제작자: Freepik - Flaticon", "https://www.flaticon.com/kr/free-icon/sausage_1857881?term=소시지&page=1&position=2&origin=search&related_id=1857881"),
        LicenseInfo("젤리 아이콘 제작자: Freepik - Flaticon", "https://www.flaticon.com/kr/free-icon/jelly_184579?term=젤리&page=1&position=15&origin=search&related_id=184579"),
        LicenseInfo("고기 아이콘 제작자: Good Ware - Flaticon", "https://www.flaticon.com/kr/free-icon/bacon_5017711?term=고기&page=1&position=16&origin=search&related_id=5017711"),
        LicenseInfo("우유 아이콘 제작자: Freepik - Flaticon", "https://www.flaticon.com/kr/free-icon/milk_372973?term=우유&page=1&position=3&origin=search&related_id=372973"),
        LicenseInfo("양배추 아이콘 제작자: dDara - Flaticon", "https://www.flaticon.com/kr/free-icon/cabbage_3823331?term=양배추&page=1&position=6&origin=search&related_id=3823331"),
        LicenseInfo("과일 아이콘 제작자: Freepik - Flaticon", "https://www.flaticon.com/kr/free-icon/cucumber_381083?term=오이&page=1&position=2&origin=search&related_id=381083"),
        LicenseInfo("떡 아이콘 제작자: Freepik - Flaticon", "https://www.flaticon.com/kr/free-icon/mochi_3362017?term=떡&page=1&position=1&origin=search&related_id=3362017"),
        LicenseInfo("팥 아이콘 제작자: justicon - Flaticon", "https://www.flaticon.com/kr/free-icon/red-beans_2079285?term=팥&page=1&position=2&origin=search&related_id=2079285"),
        LicenseInfo("새우 아이콘 제작자: Freepik - Flaticon", "https://www.flaticon.com/kr/free-icon/shrimp_2346897?term=새우&page=1&position=1&origin=search&related_id=2346897"),
        LicenseInfo("오리 아이콘 제작자: Freepik - Flaticon", "https://www.flaticon.com/kr/free-icon/peking-duck_3846159?term=오리&page=1&position=66&origin=search&related_id=3846159"),
        LicenseInfo("쉘 링크 아이콘 제작자: Freepik - Flaticon", "https://www.flaticon.com/kr/free-icon/sea-snail_616473?term=달팽이&page=1&position=10&origin=search&related_id=616473"),
        LicenseInfo("김치 아이콘 제작자: Flat Icons - Flaticon", "https://www.flaticon.com/kr/free-icon/kimchi_4727309?term=김치&page=1&position=2&origin=search&related_id=4727309"),
        LicenseInfo("음식과 식당 아이콘 제작자: Freepik - Flaticon", "https://www.flaticon.com/kr/free-icon/spinach_8411072?term=시금치&page=1&position=62&origin=search&related_id=8411072"),
        LicenseInfo("카레 아이콘 제작자: Freepik - Flaticon", "https://www.flaticon.com/kr/free-icon/curry_510096?term=카레&page=1&position=6&origin=search&related_id=510096"),
        LicenseInfo("오징어 아이콘 제작자: Freepik - Flaticon", "https://www.flaticon.com/kr/free-icon/squid_616495?term=오징어&page=1&position=10&origin=search&related_id=616495"),
        LicenseInfo("햄 아이콘 제작자: Freepik - Flaticon", "https://www.flaticon.com/kr/free-icon/ham_1699238?term=햄&page=1&position=1&origin=search&related_id=1699238"),
        LicenseInfo("물고기 아이콘 제작자: Freepik - Flaticon", "https://www.flaticon.com/kr/free-icon/fish_7584210?term=생선&page=1&position=3&origin=search&related_id=7584210"),
        LicenseInfo("물고기 아이콘 제작자: surang - Flaticon", "https://www.flaticon.com/kr/free-icon/fish_6192106?term=생선&page=1&position=5&origin=search&related_id=6192106"),
        LicenseInfo("과일 아이콘 제작자: Freepik - Flaticon", "https://www.flaticon.com/kr/free-icon/apple_415733?term=사과&page=1&position=1&origin=search&related_id=415733"),
        LicenseInfo("고기 아이콘 제작자: Freepik - Flaticon", "https://www.flaticon.com/kr/free-icon/chicken_8119002?term=닭&page=1&position=9&origin=search&related_id=8119002"),
        LicenseInfo("토마토 아이콘 제작자: Rudiyana - Flaticon", "https://www.flaticon.com/kr/free-icon/tomato_2662082?term=토마토&page=1&position=4&origin=search&related_id=2662082"),
        LicenseInfo("라자냐 아이콘 제작자: Freepik - Flaticon", "https://www.flaticon.com/kr/free-icon/lasagna_8512358?term=라자냐&page=1&position=5&origin=search&related_id=8512358"),
        LicenseInfo("고구마 아이콘 제작자: Mihimihi - Flaticon", "https://www.flaticon.com/kr/free-icon/sweet-potato_8907579?term=고구마&page=1&position=2&origin=search&related_id=8907579"),
        LicenseInfo("쌀 아이콘 제작자: Freepik - Flaticon", "https://www.flaticon.com/kr/free-icon/rice_7219927?term=쌀&page=1&position=32&origin=search&related_id=7219927"),
        LicenseInfo("중국말 아이콘 제작자: ultimatearm - Flaticon", "https://www.flaticon.com/kr/free-icon/buns_2548242?term=만두&page=1&position=2&origin=search&related_id=2548242"),
        LicenseInfo("멸치 아이콘 제작자: Freepik - Flaticon", "https://www.flaticon.com/kr/free-icon/anchovy_2056046?term=멸치&page=1&position=4&origin=search&related_id=2056046"),
        LicenseInfo("Anise icons created by Smashicons - Flaticon", "https://www.flaticon.com/free-icon/anise_3006137?term=star+anise&page=1&position=2&origin=search&related_id=3006137"),
        LicenseInfo("두부 아이콘 제작자: Freepik - Flaticon", "https://www.flaticon.com/kr/free-icon/fried-tofu-curd-balls_890112?related_id=890061&origin=search"),
        LicenseInfo("호박 아이콘 제작자: popo2021 - Flaticon", "https://www.flaticon.com/kr/free-icon/pumpkin_8886190?related_id=8886190"),
        LicenseInfo("돼지 아이콘 제작자: iconixarPro - Flaticon", "https://www.flaticon.com/kr/free-icon/pork_10851752?term=돼지고기&page=1&position=7&origin=search&related_id=10851752"),
        LicenseInfo("야채 아이콘 제작자: max.icons - Flaticon", "https://www.flaticon.com/kr/free-icon/vegetable_3944142?related_id=3944152&origin=search"),
        LicenseInfo("Seaweed icons created by Kise1ki - Flaticon", "https://www.flaticon.com/free-icon/seaweed_9925449?term=seaweed&related_id=9925449"),
        LicenseInfo("소스 아이콘 제작자: Ina Mella - Flaticon", "https://www.flaticon.com/kr/free-icon/sauce_7327731?term=소스&page=2&position=1&origin=search&related_id=7327731"),
        LicenseInfo("소고기 아이콘 제작자: flatart_icons - Flaticon", "https://www.flaticon.com/kr/free-icon/beef_2888560?term=소고기&page=1&position=16&origin=search&related_id=2888560"),
        LicenseInfo("파프리카 아이콘 제작자: Sophia tkx - Flaticon", "https://www.flaticon.com/kr/free-icon/paprika_7830662?term=파프리카&page=1&position=12&origin=search&related_id=7830662"),
        LicenseInfo("메밀 아이콘 제작자: Freepik - Flaticon", "https://www.flaticon.com/kr/free-icon/buckwheat_6534002?term=메밀&page=1&position=3&origin=search&related_id=6534002"),
        LicenseInfo("Octopus icons created by Amethyst prime - Flaticon", "https://www.flaticon.com/free-icon/octopus_7591558?term=octopus&related_id=7591558"),
        LicenseInfo("해초 아이콘 제작자: dDara - Flaticon", "https://www.flaticon.com/kr/free-icon/seaweed_10806517?related_id=10806588&origin=search"),
        LicenseInfo("고등어 아이콘 제작자: surang - Flaticon", "https://www.flaticon.com/kr/free-icon/mackerel_2884977?term=고등어&page=1&position=2&origin=search&related_id=2884977"),
        LicenseInfo("우동 아이콘 제작자: Freepik - Flaticon", "https://www.flaticon.com/kr/free-icon/udon_3978738?term=우동&page=1&position=2&origin=search&related_id=3978738"),
        LicenseInfo("갈비 살 아이콘 제작자: kerismaker - Flaticon", "https://www.flaticon.com/kr/free-icon/ribs_5371522?term=갈비&page=1&position=5&origin=search&related_id=5371522"),
        LicenseInfo("인삼 아이콘 제작자: surang - Flaticon", "https://www.flaticon.com/kr/free-icon/ginseng_5789145?term=인삼&page=1&position=9&origin=search&related_id=5789145"),
        LicenseInfo("한국어 아이콘 제작자: surang - Flaticon", "https://www.flaticon.com/kr/free-icon/spice_5789155?term=고추&related_id=5789155")
    )
}