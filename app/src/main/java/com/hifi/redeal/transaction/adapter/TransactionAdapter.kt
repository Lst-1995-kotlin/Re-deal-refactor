package com.hifi.redeal.transaction.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.databinding.RowTransactionDepositBinding
import com.hifi.redeal.databinding.RowTransactionWithdrawalBinding
import com.hifi.redeal.transaction.model.Transaction
import com.hifi.redeal.transaction.viewmodel.TransactionViewModel

class TransactionAdapter(
    private val transactionViewModel: TransactionViewModel,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var transactions = mutableListOf<Transaction>()

    init {
        transactionViewModel.transactionList.observeForever {
            transactions = it
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            DEPOSIT_TRANSACTION -> {
                val rowTransactionDepositBinding = RowTransactionDepositBinding.inflate(inflater)
                rowTransactionDepositBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                )
                return DepositHolder(rowTransactionDepositBinding)
            }

            WITHDRAWAL_TRANSACTION -> {
                val rowTransactionWithdrawalBinding =
                    RowTransactionWithdrawalBinding.inflate(inflater)
                rowTransactionWithdrawalBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                )
                return WithdrawalHolder(rowTransactionWithdrawalBinding)
            }

            else -> return throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            DEPOSIT_TRANSACTION -> {
                val item = holder as DepositHolder
                item.bind(transactions[position])
            }

            WITHDRAWAL_TRANSACTION -> {
                val item = holder as WithdrawalHolder
                item.bind(transactions[position])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return transactions[position].getTransactionType()
    }

    fun setTransactions(clientIdx: Long?) {
        clientIdx?.let {
            transactions =
                (transactions.filter { it.getTransactionClientIdx() == clientIdx }).toMutableList()
            notifyDataSetChanged()
        }
    }

    fun sortTransaction(sortValue: Boolean) {
        if (sortValue) {
            transactions.sortBy { it.getTransactionDate() }
            return notifyDataSetChanged()
        }
        transactions.sortByDescending { it.getTransactionDate() }
        notifyDataSetChanged()
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

    inner class WithdrawalHolder(
        private val rowTransactionWithdrawalBinding: RowTransactionWithdrawalBinding,
    ) :
        RecyclerView.ViewHolder(rowTransactionWithdrawalBinding.root) {
        fun bind(transaction: Transaction) {
            transaction.setTextViewValue(
                rowTransactionWithdrawalBinding.textTransactionDate,
                rowTransactionWithdrawalBinding.transctionClientNameTextView,
                rowTransactionWithdrawalBinding.textProductName,
                rowTransactionWithdrawalBinding.textProductCount,
                rowTransactionWithdrawalBinding.textUnitPrice,
                rowTransactionWithdrawalBinding.textTotalAmount,
                rowTransactionWithdrawalBinding.textRecievedAmount,
                rowTransactionWithdrawalBinding.textRecievables,
            )
        }
    }

    companion object {
        const val DEPOSIT_TRANSACTION = 1
        const val WITHDRAWAL_TRANSACTION = 2
    }
}
