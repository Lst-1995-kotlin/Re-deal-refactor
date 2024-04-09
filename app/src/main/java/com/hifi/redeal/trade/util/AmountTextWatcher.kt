package com.hifi.redeal.trade.util

import android.text.Editable
import android.text.TextWatcher
import javax.inject.Inject

class AmountTextWatcher @Inject constructor() : TextWatcher {

    private var onTextChangeListener: (CharSequence?) -> Unit = { _ -> }
    private var afterTextChangListener: (CharSequence?) -> Unit = { _ -> }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        onTextChangeListener(p0)
    }

    override fun afterTextChanged(p0: Editable?) {
        afterTextChangListener(p0)
    }

    fun setOnTextChangeListener(listener: (CharSequence?) -> Unit) {
        onTextChangeListener = listener
    }

    fun setAfterTextChangListener(listener: (CharSequence?) -> Unit) {
        afterTextChangListener = listener
    }
}