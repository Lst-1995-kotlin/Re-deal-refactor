package com.hifi.redeal.transaction.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.hifi.redeal.databinding.DialogAddDepositBinding
import com.hifi.redeal.databinding.DialogAddTransactionBinding
import com.hifi.redeal.transaction.adapter.TransactionAdapter.Companion.DEPOSIT_TRANSACTION
import com.hifi.redeal.transaction.adapter.TransactionAdapter.Companion.WITHDRAWAL_TRANSACTION
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class TransactionAddDialog @Inject constructor(
    @ActivityContext private val context: Context,
) {
    private val inflater = LayoutInflater.from(context)
    private val builder = AlertDialog.Builder(context)
    private var dialog: AlertDialog? = null

    fun show(transactionKind: Int) {
        dialog = builder.create()
        when (transactionKind) {
            DEPOSIT_TRANSACTION -> {
                dialog?.setView(getDepositBindingView())
            }

            WITHDRAWAL_TRANSACTION -> {
                dialog?.setView(getAddTransactionBindingView())
            }
        }
        dialog?.setOnDismissListener {
            dialog?.setView(null)
            dialog = null
        }
        dialog?.show()
    }

    private fun getDepositBindingView(): View {
        val dialogAddDepositBinding = DialogAddDepositBinding.inflate(inflater)
        return dialogAddDepositBinding.root
    }

    private fun getAddTransactionBindingView(): View {
        val dialogAddTransactionBinding = DialogAddTransactionBinding.inflate(inflater)
        return dialogAddTransactionBinding.root
    }
}
