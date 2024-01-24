package com.hifi.redeal.transaction.util

import android.view.View
import androidx.fragment.app.FragmentManager
import com.hifi.redeal.transaction.view.SelectTransactionClientDialog

class TransactionSelectEditTextFocusListener(
    private val selectTransactionClientDialog: SelectTransactionClientDialog,
    private val childFragmentManager: FragmentManager
    ):  View.OnFocusChangeListener {
    override fun onFocusChange(v: View, hasFocus: Boolean) {
        if (v.hasFocus()) {
            selectTransactionClientDialog.show(childFragmentManager, null)
        }
        v.clearFocus()
    }
}