package com.hifi.redeal.transaction.adapter

import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.MainActivity
import com.hifi.redeal.R
import com.hifi.redeal.databinding.RowTransactionDepositBinding
import com.hifi.redeal.databinding.RowTransactionSalesBinding
import com.hifi.redeal.transaction.model.Transaction
import com.hifi.redeal.transaction.viewmodel.TransactionViewModel

class TransactionAdapter(
    private val transactionViewModel: TransactionViewModel,
    private val mainActivity: MainActivity
) : ListAdapter<Transaction, RecyclerView.ViewHolder>(TransactionAdapterDiffCallback()) {

    init {
        transactionViewModel.transactionList.observeForever { transactions ->
            submitList(transactions.sortedByDescending { it.getTransactionDate() })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            DEPOSIT_TRANSACTION -> {
                val rowTransactionDepositBinding =
                    RowTransactionDepositBinding.inflate(inflater)
                rowTransactionDepositBinding.root.layoutParams =
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                    )
                DepositHolder(rowTransactionDepositBinding)
            }

            SALES_TRANSACTION -> {
                val rowTransactionSalesBinding =
                    RowTransactionSalesBinding.inflate(inflater)

                rowTransactionSalesBinding.root.layoutParams =
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                    )
                ReleaseHolder(rowTransactionSalesBinding)
            }

            else -> throw IllegalArgumentException("올바르지 못한 거래 타입 입니다.")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val transaction = getItem(position)
        when (holder.itemViewType) {
            DEPOSIT_TRANSACTION -> {
                val item = holder as DepositHolder
                setContextMenu(item.itemView, transaction)
                item.bind(transaction)
            }

            SALES_TRANSACTION -> {
                val item = holder as ReleaseHolder
                setContextMenu(item.itemView, transaction)
                item.bind(transaction)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return currentList[position].getTransactionType()
    }

    private fun setContextMenu(view: View, transaction: Transaction) {
        view.setOnCreateContextMenuListener { contextMenu, _, _ ->
            MenuInflater(view.context).inflate(R.menu.transaction_menu, contextMenu)
            contextMenu.findItem(R.id.transactionDeleteMenu).setOnMenuItemClickListener {
                transactionViewModel.deleteTransactionData(transaction.getTransactionIdx())
                true
            }
            contextMenu.findItem(R.id.transactionEditMenu).setOnMenuItemClickListener {
                transactionViewModel.setModifyTransaction(transaction)
                if (transaction.getTransactionType() == DEPOSIT_TRANSACTION){
                    mainActivity.replaceFragment(MainActivity.TRANSACTION_DEPOSIT_MODIFY_FRAGMENT, true, null)
                    return@setOnMenuItemClickListener true
                }
                return@setOnMenuItemClickListener true
            }
        }
    }

    inner class DepositHolder(
        private val rowTransactionDepositBinding: RowTransactionDepositBinding,
    ) :
        RecyclerView.ViewHolder(rowTransactionDepositBinding.root) {
        fun bind(transaction: Transaction) {
            transaction.setTextViewValue(
                rowTransactionDepositBinding.textTransactionDate,
                rowTransactionDepositBinding.transctionClientNameTextView,
                rowTransactionDepositBinding.depositPriceTextView,
            )
        }
    }

    inner class ReleaseHolder(
        private val rowTransactionReleaseBinding: RowTransactionSalesBinding,
    ) :
        RecyclerView.ViewHolder(rowTransactionReleaseBinding.root) {
        fun bind(transaction: Transaction) {
            transaction.setTextViewValue(
                rowTransactionReleaseBinding.textTransactionDate,
                rowTransactionReleaseBinding.transctionClientNameTextView,
                rowTransactionReleaseBinding.itemNameTextView,
                rowTransactionReleaseBinding.itemSalesCountTextView,
                rowTransactionReleaseBinding.itemPriceTextView,
                rowTransactionReleaseBinding.totalSalesAmountTextView,
                rowTransactionReleaseBinding.recievedAmountTextView,
                rowTransactionReleaseBinding.recievablesTextView,
            )
        }
    }

    companion object {
        const val DEPOSIT_TRANSACTION = 1
        const val SALES_TRANSACTION = 2
    }
}

