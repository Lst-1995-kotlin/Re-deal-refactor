package com.hifi.redeal.trade.util

import android.view.View
import androidx.fragment.app.FragmentManager
import com.hifi.redeal.trade.view_refactor_before.dialog.SelectTransactionClientDialog

class TransactionSelectEditTextFocusListener(
    private val selectTransactionClientDialog: SelectTransactionClientDialog,
    private val childFragmentManager: FragmentManager
) : View.OnFocusChangeListener {
    override fun onFocusChange(v: View, hasFocus: Boolean) {
        if (v.hasFocus()) {
            selectTransactionClientDialog.show(childFragmentManager, null)
        }
        v.clearFocus()
    }
}