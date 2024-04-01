package com.hifi.redeal.util

import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver

class KeyboardFocusClearListener(private val view: View) : ViewTreeObserver.OnGlobalLayoutListener {

    override fun onGlobalLayout() {
        val rect = Rect()
        view.getWindowVisibleDisplayFrame(rect)
        val screenHeight = view.rootView.height
        val keypadHeight = screenHeight - rect.bottom

        if (keypadHeight < screenHeight * 0.15) {
            view.clearFocus()
        }
    }
}
