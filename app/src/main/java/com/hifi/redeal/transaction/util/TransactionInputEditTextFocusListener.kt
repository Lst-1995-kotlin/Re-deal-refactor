package com.hifi.redeal.transaction.util

import android.view.View
import com.hifi.redeal.transaction.configuration.FocusBackgroundConfiguration

class TransactionInputEditTextFocusListener: View.OnFocusChangeListener {
    override fun onFocusChange(v: View, hasFocus: Boolean) {
        if (v.hasFocus()) {
            v.setBackgroundResource(FocusBackgroundConfiguration.FOCUSED_BACKGROUND.resource)
            return
        }
        v.setBackgroundResource(FocusBackgroundConfiguration.NOT_FOCUSED_BACKGROUND.resource)
    }
}