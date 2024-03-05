package com.hifi.redeal.transaction.adapter.viewHolder.transaction

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.R
import com.hifi.redeal.databinding.DialogTransactionEditBinding
import com.hifi.redeal.databinding.RowTransactionDepositBinding
import com.hifi.redeal.transaction.model.TransactionBasic
import com.hifi.redeal.transaction.viewmodel.TransactionViewModel

class DepositHolder(
    private val rowTransactionDepositBinding: RowTransactionDepositBinding,
    private val transactionViewModel: TransactionViewModel
) : RecyclerView.ViewHolder(rowTransactionDepositBinding.root) {
    fun bind(transactionBasic: TransactionBasic, position: Int) {
        val valuesMap = transactionBasic.getTransactionValueMap()
        rowTransactionDepositBinding.run {
            textTransactionDate.text = valuesMap["date"]
            transctionClientNameTextView.text = valuesMap["clientName"]
            depositPriceTextView.text = valuesMap["amountReceived"]
            setLongClickEvent(root, transactionBasic, position)
        }
    }

    private fun setLongClickEvent(view: View, transactionBasic: TransactionBasic, position: Int) {
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
                    transactionViewModel.deleteTransactionIndex(transactionBasic.getTransactionIdx())
                    if (position > 0) transactionViewModel.setMoveToPosition(position - 1)
                }
                transactionEditImageButton.setOnClickListener {
                    dialog.dismiss()
                    transactionViewModel.setModifyTransaction(transactionBasic)
                    transactionViewModel.setMoveToPosition(position)
                }
            }
            true
        }
    }
}