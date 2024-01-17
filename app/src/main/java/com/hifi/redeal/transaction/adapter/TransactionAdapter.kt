package com.hifi.redeal.transaction.adapter

import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.R
import com.hifi.redeal.databinding.RowTransactionDepositBinding
import com.hifi.redeal.databinding.RowTransactionReleaseBinding
import com.hifi.redeal.transaction.model.Transaction
import com.hifi.redeal.transaction.viewmodel.TransactionViewModel

class TransactionAdapter(
    private val transactionViewModel: TransactionViewModel,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var transactions = listOf<Transaction>()

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
                rowTransactionDepositBinding.root.layoutParams =
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                    )
                return DepositHolder(rowTransactionDepositBinding)
            }

            WITHDRAWAL_TRANSACTION -> {
                val rowTransactionReleaseBinding =
                    RowTransactionReleaseBinding.inflate(inflater)

                rowTransactionReleaseBinding.root.layoutParams =
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                    )
                return ReleaseHolder(rowTransactionReleaseBinding)
            }

            else -> return throw IllegalArgumentException("올바르지 못한 거래 타입 입니다.")
        }
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            DEPOSIT_TRANSACTION -> {
                val item = holder as DepositHolder
                contextMenuSetting(item.itemView, position)
                item.bind(transactions[position])
            }

            WITHDRAWAL_TRANSACTION -> {
                val item = holder as ReleaseHolder
                contextMenuSetting(item.itemView, position)
                item.bind(transactions[position])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return transactions[position].getTransactionType()
    }

    fun sortTransaction(sortValue: Boolean) {
        if (sortValue) {
            transactions = transactions.sortedBy { it.getTransactionDate() }
            return notifyDataSetChanged()
        }
        transactions = transactions.sortedByDescending { it.getTransactionDate() }
        notifyDataSetChanged()
    }

    fun findTransactions(clientIdx: Long?) {
        clientIdx?.let {
            transactions = transactions.filter { it.getTransactionClientIdx() == clientIdx }
            notifyDataSetChanged()
        }
    }


    private fun contextMenuSetting(view: View, position: Int) {
        view.setOnCreateContextMenuListener { contextMenu, _, _ ->
            MenuInflater(view.context).inflate(R.menu.transaction_menu, contextMenu)
            contextMenu.findItem(R.id.transactionDeleteMenu).setOnMenuItemClickListener {
                transactionViewModel.deleteTransactionData(transactions[position])
                notifyDataSetChanged()
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
