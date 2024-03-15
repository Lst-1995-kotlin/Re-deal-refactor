package com.hifi.redeal.trade.util

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import com.hifi.redeal.trade.configuration.TradeAmountConfiguration.Companion.transactionAmountCheck
import com.hifi.redeal.trade.util.TransactionNumberFormatUtil.removeNumberFormat
import com.hifi.redeal.trade.util.TransactionNumberFormatUtil.replaceNumberFormat
import com.hifi.redeal.trade.domain.viewmodel.TradeByClientViewModel

class ItemTextWatcher(
    private val tradeByClientViewModel: TradeByClientViewModel,
    private val nowEditText: TextInputEditText,
    private val notEditText: TextInputEditText,
    private val amountEditText: TextInputEditText,
    private val button: Button
) : TextWatcher {

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        button.visibility =
            if (p0.isNullOrEmpty() || tradeByClientViewModel.clientId.value == null) View.GONE
            else View.VISIBLE
    }

    override fun afterTextChanged(p0: Editable?) {
        if (p0.isNullOrEmpty()) {
            nowEditText.removeTextChangedListener(this)
            nowEditText.text = null
            amountEditText.text = null
            nowEditText.addTextChangedListener(this)
        } else if ("$p0".all { it.isDigit() || it == ',' }) {
            var inputNumber = removeNumberFormat("$p0")
            while (!transactionAmountCheck(inputNumber)) {
                inputNumber /= 10L
            }

            if (notEditText.text.isNullOrEmpty()) {
                amountEditText.text = null
            } else {
                while (!transactionAmountCheck(inputNumber * removeNumberFormat("${notEditText.text}"))) {
                    inputNumber /= 10L
                }
                amountEditText.setText("${inputNumber * removeNumberFormat("${notEditText.text}")}")
            }

            val replaceNumber = replaceNumberFormat(inputNumber)
            nowEditText.removeTextChangedListener(this)
            nowEditText.setText(replaceNumber)
            nowEditText.setSelection(replaceNumber.length)
            nowEditText.addTextChangedListener(this)
        }
    }
}