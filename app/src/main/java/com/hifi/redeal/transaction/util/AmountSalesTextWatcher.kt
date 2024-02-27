package com.hifi.redeal.transaction.util

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import com.hifi.redeal.transaction.configuration.TransactionAmountConfiguration.Companion.transactionAmountCheck
import com.hifi.redeal.transaction.util.TransactionNumberFormatUtil.removeNumberFormat
import com.hifi.redeal.transaction.util.TransactionNumberFormatUtil.replaceNumberFormat
import com.hifi.redeal.transaction.viewmodel.TransactionClientViewModel

class AmountSalesTextWatcher(
    private val transactionClientViewModel: TransactionClientViewModel,
    private val textInputEditText: TextInputEditText,
    private val itemAmountView: TextInputEditText,
    private val itemCountView: TextInputEditText,
    private val button: Button
) : TextWatcher {

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        button.visibility =
            if (p0.isNullOrEmpty() || transactionClientViewModel.selectedClient.value == null) View.GONE else View.VISIBLE
    }

    override fun afterTextChanged(p0: Editable?) {
        if (p0.isNullOrEmpty()) {
            textInputEditText.removeTextChangedListener(this)
            textInputEditText.text = null
            textInputEditText.addTextChangedListener(this)
        } else if ("$p0".all { it.isDigit() || it == ',' }) {
            var inputNumber = removeNumberFormat("$p0")
            while (!transactionAmountCheck(inputNumber)) {
                inputNumber /= 10L
            }
            val itemAmount = itemAmountView.text?.let { removeNumberFormat(it.toString()) } ?: 0L
            val itemCount = itemCountView.text?.let{ removeNumberFormat(it.toString()) } ?: 0L
            while (inputNumber > itemAmount * itemCount) {
                inputNumber /= 10L
            }
            val replaceNumber = replaceNumberFormat(inputNumber)

            textInputEditText.removeTextChangedListener(this)
            textInputEditText.setText(replaceNumber)
            textInputEditText.setSelection(replaceNumber.length)
            textInputEditText.addTextChangedListener(this)

        }
    }
}