package com.hifi.redeal.transaction.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.databinding.RowTransactionBinding
import com.hifi.redeal.databinding.RowTransactionDepositBinding
import com.hifi.redeal.transaction.model.Transaction

class TransactionAdapter(
    private val transactions: MutableList<Transaction>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            1 -> {
                val rowTransactionDepositBinding = RowTransactionDepositBinding.inflate(inflater)
                rowTransactionDepositBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                return DepositHolder(rowTransactionDepositBinding)
            }

            2 -> {
                val rowTransactionBinding = RowTransactionBinding.inflate(inflater)
                rowTransactionBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                return TransactionHolder(rowTransactionBinding)
            }

            else -> return throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            1 -> {
                val item = holder as DepositHolder
                item.bind(transactions[position])
            }

            2 -> {
                val item = holder as TransactionHolder
                item.bind(transactions[position])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return transactions[position].getTransactionType()
    }

    fun addTransaction(transaction: Transaction) {
        transactions.add(transaction)
        notifyDataSetChanged()
    }

    inner class DepositHolder(private val rowTransactionDepositBinding: RowTransactionDepositBinding) :
        RecyclerView.ViewHolder(rowTransactionDepositBinding.root) {
        fun bind(transaction: Transaction) {
            transaction.setViewText(
                rowTransactionDepositBinding.textTransactionDate,
                rowTransactionDepositBinding.transctionClientNameTextView,
                rowTransactionDepositBinding.depositPriceTextView
            )
        }
    }

    inner class TransactionHolder(private val rowTransactionBinding: RowTransactionBinding) :
        RecyclerView.ViewHolder(rowTransactionBinding.root) {
        fun bind(transaction: Transaction) {
            transaction.setViewText(
                rowTransactionBinding.textTransactionDate,
                rowTransactionBinding.transctionClientNameTextView,
                rowTransactionBinding.textProductName,
                rowTransactionBinding.textProductCount,
                rowTransactionBinding.textUnitPrice,
                rowTransactionBinding.textTotalAmount,
                rowTransactionBinding.textRecievedAmount,
                rowTransactionBinding.textRecievables
            )
        }
    }

}