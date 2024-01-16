package com.hifi.redeal.transaction.util

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import com.hifi.redeal.transaction.viewmodel.ClientViewModel
import java.text.NumberFormat
import java.util.Locale

class CustomTextWatcher(
    private val viewModel: ClientViewModel,
    private val textInputEditText: TextInputEditText,
    private val button: Button
) : TextWatcher {

    private val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (p0.isNullOrEmpty() || viewModel.selectedClient.value == null) {
            button.visibility = View.GONE
            return
        }
        button.visibility = View.VISIBLE
    }

    override fun afterTextChanged(p0: Editable?) {
        if (p0.isNullOrEmpty()) {
            textInputEditText.removeTextChangedListener(this)
            textInputEditText.setText("")
            textInputEditText.addTextChangedListener(this)
        } else if ("$p0".all { it.isDigit() || it == ',' }) {
            val number = numberFormat.format("$p0".replace(",", "").toLong())
            textInputEditText.removeTextChangedListener(this)
            textInputEditText.setText(number)
            textInputEditText.setSelection(number.length)
            textInputEditText.addTextChangedListener(this)
        }
    }
}