package com.hifi.redeal.transaction.adapter

import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.R
import com.hifi.redeal.databinding.RowTransactionDepositBinding
import com.hifi.redeal.databinding.RowTransactionReleaseBinding
import com.hifi.redeal.transaction.model.Transaction
import com.hifi.redeal.transaction.util.TransactionDiffCallback
import com.hifi.redeal.transaction.viewmodel.TransactionViewModel

class TransactionAdapter(
    private val transactionViewModel: TransactionViewModel,
    private val clientIdx: Long?
) : ListAdapter<Transaction, RecyclerView.ViewHolder>(TransactionDiffCallback()) {
    init {
        transactionViewModel.transactionList.observeForever { transactions ->
            clientIdx?.let {
                submitList(transactions.filter { it.getTransactionClientIdx() == clientIdx })
            } ?: submitList(transactions)
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

            WITHDRAWAL_TRANSACTION -> {
                val rowTransactionReleaseBinding =
                    RowTransactionReleaseBinding.inflate(inflater)

                rowTransactionReleaseBinding.root.layoutParams =
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                    )
                ReleaseHolder(rowTransactionReleaseBinding)
            }

            else -> throw IllegalArgumentException("올바르지 못한 거래 타입 입니다.")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val transaction = getItem(position)
        when (holder.itemViewType) {
            DEPOSIT_TRANSACTION -> {
                val item = holder as DepositHolder
                setContextMenu(item.itemView, position)
                item.bind(transaction)
            }

            WITHDRAWAL_TRANSACTION -> {
                val item = holder as ReleaseHolder
                setContextMenu(item.itemView, position)
                item.bind(transaction)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).getTransactionType()
    }

    private fun setContextMenu(view: View, position: Int) {
        view.setOnCreateContextMenuListener { contextMenu, _, _ ->
            MenuInflater(view.context).inflate(R.menu.transaction_menu, contextMenu)
            contextMenu.findItem(R.id.transactionDeleteMenu).setOnMenuItemClickListener {
                transactionViewModel.deleteTransactionData(getItem(position))
                true
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
        private val rowTransactionReleaseBinding: RowTransactionReleaseBinding,
    ) :
        RecyclerView.ViewHolder(rowTransactionReleaseBinding.root) {
        fun bind(transaction: Transaction) {
            transaction.setTextViewValue(
                rowTransactionReleaseBinding.textTransactionDate,
                rowTransactionReleaseBinding.transctionClientNameTextView,
                rowTransactionReleaseBinding.textProductName,
                rowTransactionReleaseBinding.textProductCount,
                rowTransactionReleaseBinding.textUnitPrice,
                rowTransactionReleaseBinding.textTotalAmount,
                rowTransactionReleaseBinding.textRecievedAmount,
                rowTransactionReleaseBinding.textRecievables,
            )
        }
    }

    companion object {
        const val DEPOSIT_TRANSACTION = 1
        const val WITHDRAWAL_TRANSACTION = 2
    }
}

