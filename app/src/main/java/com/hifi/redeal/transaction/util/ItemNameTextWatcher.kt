package com.hifi.redeal.transaction.util

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import com.hifi.redeal.transaction.viewmodel.ClientViewModel

class ItemNameTextWatcher(
    private val clientViewModel: ClientViewModel,
    private val button: Button
) : TextWatcher {

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (p0.isNullOrEmpty() || clientViewModel.selectedClient.value == null) {
            button.visibility = View.GONE
            return
        }
        button.visibility = View.VISIBLE
    }

    override fun afterTextChanged(p0: Editable?) {}
}