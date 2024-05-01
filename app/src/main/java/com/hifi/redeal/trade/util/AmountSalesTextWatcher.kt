package com.hifi.redeal.trade.util

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import com.hifi.redeal.trade.configuration.TradeAmountConfiguration.Companion.tradeAmountCheck
import com.hifi.redeal.trade.ui.viewmodel.TradeByClientViewModel
import com.hifi.redeal.util.numberFormatToLong
import com.hifi.redeal.util.toNumberFormat

class AmountSalesTextWatcher(
    private val tradeByClientViewModel: TradeByClientViewModel,
    private val textInputEditText: TextInputEditText,
    private val itemAmountView: TextInputEditText,
    private val itemCountView: TextInputEditText,
    private val button: Button
) : TextWatcher {

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        button.visibility =
            if (p0.isNullOrEmpty() || tradeByClientViewModel.clientId.value == null) View.GONE else View.VISIBLE
    }

    override fun afterTextChanged(p0: Editable?) {
        if (p0.isNullOrEmpty()) {
            textInputEditText.removeTextChangedListener(this)
            textInputEditText.text = null
            textInputEditText.addTextChangedListener(this)
        } else if ("$p0".all { it.isDigit() || it == ',' }) {
            var inputNumber = "$p0".numberFormatToLong()
            while (!tradeAmountCheck(inputNumber)) {
                inputNumber /= 10L
            }
            val itemAmount = itemAmountView.text?.let { "$it".numberFormatToLong() } ?: 0L
            val itemCount = itemCountView.text?.let { "$it".numberFormatToLong() } ?: 0L
            while (inputNumber > itemAmount * itemCount) {
                inputNumber /= 10L
            }
            val replaceNumber = inputNumber.toNumberFormat()

            textInputEditText.removeTextChangedListener(this)
            textInputEditText.setText(replaceNumber)
            textInputEditText.setSelection(replaceNumber.length)
            textInputEditText.addTextChangedListener(this)

        }
    }
}