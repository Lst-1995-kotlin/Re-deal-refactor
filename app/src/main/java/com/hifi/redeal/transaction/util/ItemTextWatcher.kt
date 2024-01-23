package com.hifi.redeal.transaction.util

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import com.hifi.redeal.transaction.configuration.TransactionAmountConfiguration
import com.hifi.redeal.transaction.configuration.TransactionAmountConfiguration.Companion.transactionAmountCheck
import com.hifi.redeal.transaction.util.TransactionNumberFormatUtil.removeNumberFormat
import com.hifi.redeal.transaction.util.TransactionNumberFormatUtil.replaceNumberFormat
import com.hifi.redeal.transaction.viewmodel.ClientViewModel

class ItemTextWatcher(
    private val clientViewModel: ClientViewModel,
    private val nowEditText: TextInputEditText,
    private val notEditText: TextInputEditText,
    private val amountEditText: TextInputEditText,
    private val button: Button
) : TextWatcher {

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        button.visibility =
            if (p0.isNullOrEmpty() || clientViewModel.selectedClient.value == null) View.GONE else View.VISIBLE
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

            if (inputNumber == 0L) {
                nowEditText.text = null
                return
            }

            val replaceNumber = replaceNumberFormat(inputNumber)
            nowEditText.removeTextChangedListener(this)
            nowEditText.setText(replaceNumber)
            nowEditText.setSelection(replaceNumber.length)
            nowEditText.addTextChangedListener(this)
        }
    }
}