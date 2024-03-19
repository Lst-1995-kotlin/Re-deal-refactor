package com.hifi.redeal.util

import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver

class KeyboardFocusClearListener(private val textView: View) : ViewTreeObserver.OnGlobalLayoutListener {

    override fun onGlobalLayout() {
        val rect = Rect()
        textView.getWindowVisibleDisplayFrame(rect)
        val screenHeight = textView.rootView.height
        val keypadHeight = screenHeight - rect.bottom

        if (keypadHeight < screenHeight * 0.15) {
            textView.clearFocus()
        }
    }
}
