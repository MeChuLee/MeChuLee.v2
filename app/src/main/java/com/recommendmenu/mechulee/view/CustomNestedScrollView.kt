package com.recommendmenu.mechulee.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.widget.NestedScrollView
import com.recommendmenu.mechulee.utils.Constants.BOTTOM_BAR_STATUS_HIDE
import com.recommendmenu.mechulee.utils.Constants.BOTTOM_BAR_STATUS_SHOW

class CustomNestedScrollView(context: Context, attrs: AttributeSet) :
    NestedScrollView(context, attrs) {

    // Bottom Bar 를 관리하는 함수
    var onBottomBarStatusChange: ((Int) -> Unit)? = null

    init {
        setOnTouchListener(object : OnTouchListener {
            private var prevY = 0f

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                val currentY: Float

                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        prevY = event.y
                    }

                    MotionEvent.ACTION_MOVE -> {
                        currentY = event.y

                        if (prevY < currentY) {
                            // 위로 스크롤 시 Bottom Bar 나타남
                            onBottomBarStatusChange?.invoke(BOTTOM_BAR_STATUS_SHOW)
                        } else if (prevY > currentY) {
                            // 아래로 스크롤 시 Bottom Bar 사라짐
                            onBottomBarStatusChange?.invoke(BOTTOM_BAR_STATUS_HIDE)
                        }

                        prevY = currentY
                    }

                    MotionEvent.ACTION_UP -> {
                        v?.performClick()
                    }
                }

                return false
            }
        })
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}
