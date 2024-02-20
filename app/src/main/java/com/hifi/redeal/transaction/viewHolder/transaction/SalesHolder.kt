package com.hifi.redeal.transaction.viewHolder.transaction

import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hifi.redeal.R
import com.hifi.redeal.databinding.DialogTransactionAddSelectBinding
import com.hifi.redeal.databinding.DialogTransactionEditBinding
import com.hifi.redeal.databinding.RowTransactionSalesBinding
import com.hifi.redeal.transaction.model.Transaction
import com.hifi.redeal.transaction.viewmodel.TransactionViewModel

class SalesHolder(
    private val rowTransactionReleaseBinding: RowTransactionSalesBinding,
    private val transactionViewModel: TransactionViewModel
) : RecyclerView.ViewHolder(rowTransactionReleaseBinding.root) {
    fun bind(transaction: Transaction, position: Int) {
        val valuesMap = transaction.getTransactionValueMap()
        rowTransactionReleaseBinding.run {
            textTransactionDate.text = valuesMap["date"]
            transctionClientNameTextView.text = valuesMap["clientName"]
            itemNameTextView.text = valuesMap["itemName"]
            itemSalesCountTextView.text = valuesMap["itemCount"]
            itemPriceTextView.text = valuesMap["itemPrice"]
            totalSalesAmountTextView.text = valuesMap["totalAmount"]
            recievedAmountTextView.text = valuesMap["amountReceived"]
            recievablesTextView.text = valuesMap["receivables"]
            if (valuesMap["receivables"] == "0") {
                recievablesTextView.visibility = View.GONE
                textTransaction23.visibility = View.GONE
                textTransaction24.visibility = View.GONE
            }
            setLongClickEvent(root, transaction, position)
        }
    }

    private fun setLongClickEvent(view: View, transaction: Transaction, position: Int) {
        view.setOnLongClickListener {
            val builder = MaterialAlertDialogBuilder(view.context)
            val layoutInflater = LayoutInflater.from(view.context)
            val dialogTransactionEditBinding =
                DialogTransactionEditBinding.inflate(layoutInflater)
            builder.setView(dialogTransactionEditBinding.root)
            val dialog = builder.create()
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
            dialog.show()
            true
        }
    }
}