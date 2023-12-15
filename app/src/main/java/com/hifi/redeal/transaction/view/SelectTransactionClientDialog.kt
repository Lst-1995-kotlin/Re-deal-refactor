package com.hifi.redeal.transaction.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.hifi.redeal.databinding.TransactionSelectClientBinding
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class SelectTransactionClientDialog @Inject constructor(
    @ActivityContext private val context: Context,
) {
    private val inflater = LayoutInflater.from(context)
    private val builder = AlertDialog.Builder(context)
    private var dialog: AlertDialog? = null

    fun selectTransactionClientDialogShow() {
        dialog = builder.create()
        dialog?.setView(getSelectClientView())
        dialog?.setOnDismissListener {
            dialog = null
        }
        dialog?.show()
    }

    private fun getSelectClientView(): View {
        val transactionSelectClientBinding = TransactionSelectClientBinding.inflate(inflater)
        return transactionSelectClientBinding.root
    }
}
