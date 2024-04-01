package com.hifi.redeal.trade.util

import android.view.View
import com.hifi.redeal.trade.configuration.FocusBackgroundConfiguration
import javax.inject.Inject

class TradeInputEditTextFocusListener: View.OnFocusChangeListener {
    override fun onFocusChange(v: View, hasFocus: Boolean) {
        if (v.hasFocus()) {
            v.setBackgroundResource(FocusBackgroundConfiguration.FOCUSED_BACKGROUND.resource)
            return
        }
        v.setBackgroundResource(FocusBackgroundConfiguration.NOT_FOCUSED_BACKGROUND.resource)
    }
}