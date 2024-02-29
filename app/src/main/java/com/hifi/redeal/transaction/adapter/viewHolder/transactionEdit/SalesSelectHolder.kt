package com.hifi.redeal.transaction.adapter.viewHolder.transactionEdit

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.R
import com.hifi.redeal.databinding.RowTransactionSelectSalesBinding
import com.hifi.redeal.transaction.model.Transaction
import com.hifi.redeal.transaction.viewmodel.TransactionViewModel

class SalesSelectHolder(
    private val rowTransactionSelectSalesBinding: RowTransactionSelectSalesBinding,
    private val transactionViewModel: TransactionViewModel
) : RecyclerView.ViewHolder(rowTransactionSelectSalesBinding.root) {
    fun bind(transaction: Transaction) {
        val valuesMap = transaction.getTransactionValueMap()
        rowTransactionSelectSalesBinding.run {
            transactionSelectDateTextView.text = valuesMap["date"]
            transactionSelectClientNameTextView.text = valuesMap["clientName"]
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
            setClickEvent(rowTransactionSelectSalesBinding, transaction)
        }
    }

    fun setSelectedImage(isSelected: Boolean) {
        if (isSelected) {
            rowTransactionSelectSalesBinding.transactionSelectSalesCheckBox.setImageResource(
                R.drawable.done_paint_24px
            )
            return
        }
        rowTransactionSelectSalesBinding.transactionSelectSalesCheckBox.setImageResource(
            R.drawable.rounded_rectangle_stroke_primary20
        )
    }

    private fun setClickEvent(
        rowTransactionSelectSalesBinding: RowTransactionSelectSalesBinding,
        transaction: Transaction
    ) {
        rowTransactionSelectSalesBinding.root.setOnClickListener {
            transactionViewModel.transactionSelectedChanged(transaction.getTransactionIdx())
        }
    }
}