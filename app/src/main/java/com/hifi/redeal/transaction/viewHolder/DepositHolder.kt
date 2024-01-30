package com.hifi.redeal.transaction.viewHolder

import android.view.MenuInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.MainActivity
import com.hifi.redeal.R
import com.hifi.redeal.databinding.RowTransactionDepositBinding
import com.hifi.redeal.transaction.model.Transaction
import com.hifi.redeal.transaction.viewmodel.TransactionViewModel

class DepositHolder(
    private val rowTransactionDepositBinding: RowTransactionDepositBinding,
    private val mainActivity: MainActivity,
    private val transactionViewModel: TransactionViewModel
) : RecyclerView.ViewHolder(rowTransactionDepositBinding.root) {
    fun bind(transaction: Transaction, position: Int) {
        val valuesMap = transaction.getTransactionValueMap()
        rowTransactionDepositBinding.run {
            textTransactionDate.text = valuesMap["date"]
            transctionClientNameTextView.text = valuesMap["clientName"]
            depositPriceTextView.text = valuesMap["amountReceived"]
            setContextMenu(root, transaction, position)
        }
    }

    private fun setContextMenu(view: View, transaction: Transaction, position: Int) {
        view.setOnCreateContextMenuListener { contextMenu, _, _ ->
            MenuInflater(view.context).inflate(R.menu.transaction_menu, contextMenu)
            contextMenu.findItem(R.id.transactionDeleteMenu).setOnMenuItemClickListener {
                transactionViewModel.deleteTransactionData(transaction.getTransactionIdx())
                if (position > 0) {
                    transactionViewModel.setMoveToPosition(position - 1)
                    return@setOnMenuItemClickListener true
                }
                transactionViewModel.setMoveToPosition(position)
                true
            }
            contextMenu.findItem(R.id.transactionEditMenu).setOnMenuItemClickListener {
                transactionViewModel.setModifyTransaction(transaction)
                mainActivity.replaceFragment(
                    MainActivity.TRANSACTION_DEPOSIT_MODIFY_FRAGMENT,
                    true,
                    null
                )
                transactionViewModel.setMoveToPosition(position)
                true
            }
        }
    }
}