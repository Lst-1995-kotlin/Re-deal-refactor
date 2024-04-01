package com.hifi.redeal.trade.util

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import com.hifi.redeal.trade.domain.viewmodel.TradeByClientViewModel

class ItemNameTextWatcher(
    private val tradeByClientViewModel: TradeByClientViewModel,
    private val button: Button
) : TextWatcher {

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (p0.isNullOrEmpty() || tradeByClientViewModel.clientId.value == null) {
            button.visibility = View.GONE
            return
        }
        button.visibility = View.VISIBLE
    }

    override fun afterTextChanged(p0: Editable?) {}
}