package com.hifi.redeal.transaction.adapter.viewHolder.transaction

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hifi.redeal.R
import com.hifi.redeal.databinding.DialogTransactionEditBinding
import com.hifi.redeal.databinding.RowTransactionDepositBinding
import com.hifi.redeal.transaction.model.Transaction
import com.hifi.redeal.transaction.viewmodel.TransactionViewModel

class DepositHolder(
    private val rowTransactionDepositBinding: RowTransactionDepositBinding,
    private val transactionViewModel: TransactionViewModel
) : RecyclerView.ViewHolder(rowTransactionDepositBinding.root) {
    fun bind(transaction: Transaction, position: Int) {
        val valuesMap = transaction.getTransactionValueMap()
        rowTransactionDepositBinding.run {
            textTransactionDate.text = valuesMap["date"]
            transctionClientNameTextView.text = valuesMap["clientName"]
            depositPriceTextView.text = valuesMap["amountReceived"]
            setLongClickEvent(root, transaction, position)
        }
    }

    private fun setLongClickEvent(view: View, transaction: Transaction, position: Int) {
        view.setOnLongClickListener {
            val builder = AlertDialog.Builder(view.context, R.style.RoundedAlertDialog)
            val layoutInflater = LayoutInflater.from(view.context)
            val dialogTransactionEditBinding =
                DialogTransactionEditBinding.inflate(layoutInflater)
            builder.setView(dialogTransactionEditBinding.root)

            val dialog = builder.show()
            dialogTransactionEditBinding.run {
                transactionDeleteImageButton.setOnClickListener {
                    dialog.dismiss()
                    transactionViewModel.deleteTransactionIndex(transaction.getTransactionIdx())
                    if (position > 0) transactionViewModel.setMoveToPosition(position - 1)
                }
                transactionEditImageButton.setOnClickListener {
                    dialog.dismiss()
                    transactionViewModel.setModifyTransaction(transaction)
                    transactionViewModel.setMoveToPosition(position)
                }
            }
            true
        }
    }
}