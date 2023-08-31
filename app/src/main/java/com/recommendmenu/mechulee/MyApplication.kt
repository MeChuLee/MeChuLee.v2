package com.recommendmenu.mechulee

import android.app.Application
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy

/**
 * Logger 라이브러리를 사용하기 위해 생성한 Application Class
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)
            .methodCount(2)
            .methodOffset(0)
            .tag("TEST_LOGGER")
            .build()

        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
    }
}