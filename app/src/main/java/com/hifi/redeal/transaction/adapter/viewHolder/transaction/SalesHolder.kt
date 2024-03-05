package com.hifi.redeal.transaction.adapter.viewHolder.transaction

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.R
import com.hifi.redeal.databinding.DialogTransactionEditBinding
import com.hifi.redeal.databinding.RowTransactionSalesBinding
import com.hifi.redeal.transaction.model.TransactionBasic
import com.hifi.redeal.transaction.viewmodel.TransactionViewModel

class SalesHolder(
    private val rowTransactionReleaseBinding: RowTransactionSalesBinding,
    private val transactionViewModel: TransactionViewModel
) : RecyclerView.ViewHolder(rowTransactionReleaseBinding.root) {
    fun bind(transactionBasic: TransactionBasic, position: Int) {
        val valuesMap = transactionBasic.getTransactionValueMap()
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