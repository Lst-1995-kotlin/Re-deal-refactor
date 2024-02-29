package com.hifi.redeal.transaction.adapter.viewHolder.transactionEdit

import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.R
import com.hifi.redeal.databinding.RowTransactionSelectDepositBinding
import com.hifi.redeal.transaction.model.Transaction
import com.hifi.redeal.transaction.viewmodel.TransactionViewModel

class DepositSelectHolder(
    private val rowTransactionSelectDepositBinding: RowTransactionSelectDepositBinding,
    private val transactionViewModel: TransactionViewModel
) : RecyclerView.ViewHolder(rowTransactionSelectDepositBinding.root) {
    fun bind(transaction: Transaction) {
        val valuesMap = transaction.getTransactionValueMap()
        rowTransactionSelectDepositBinding.run {
            transactionSelectDateTextView.text = valuesMap["date"]
            transactionSelectClientNameTextView.text = valuesMap["clientName"]
            depositPriceTextView.text = valuesMap["amountReceived"]
            setClickEvent(rowTransactionSelectDepositBinding, transaction)
        }
    }

    fun setSelectedImage(isSelected: Boolean) {
        if (isSelected) {
            rowTransactionSelectDepositBinding.transactionSelectDepositCheckBox.setImageResource(
                R.drawable.done_paint_24px
            )
            return
        }
        rowTransactionSelectDepositBinding.transactionSelectDepositCheckBox.setImageResource(
            R.drawable.rounded_rectangle_stroke_primary20
        )
    }

    private fun setClickEvent(
        rowTransactionSelectDepositBinding: RowTransactionSelectDepositBinding,
        transaction: Transaction
    ) {
        rowTransactionSelectDepositBinding.root.setOnClickListener {
            transactionViewModel.transactionSelectedChanged(transaction.getTransactionIdx())
        }
    }
}