package com.hifi.redeal.trade.util

import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

class EditTextFocusDialogListener(
    private val dialogFragment: DialogFragment,
    private val childFragmentManager: FragmentManager
) : View.OnFocusChangeListener {
    override fun onFocusChange(v: View, hasFocus: Boolean) {
        if (v.hasFocus()) {
            dialogFragment.show(childFragmentManager, null)
        }
        v.clearFocus()
    }
}