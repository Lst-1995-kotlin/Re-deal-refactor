package com.hifi.redeal.trade.configuration

import android.content.Context
import android.view.WindowManager
import androidx.fragment.app.DialogFragment

enum class DialogConfiguration(private val size: Float) {
    DIALOG_WIDTH(0.8F),
    DIALOG_HEIGHT(0.8F),
    ;

    companion object {
        fun Context.dialogResize(dialogFragment: DialogFragment) {
            val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val rect = windowManager.currentWindowMetrics.bounds
            val window = dialogFragment.dialog?.window
            val x = (rect.width() * DIALOG_WIDTH.size).toInt()
            val y = (rect.height() * DIALOG_HEIGHT.size).toInt()
            window?.setLayout(x, y)
        }
    }
}
