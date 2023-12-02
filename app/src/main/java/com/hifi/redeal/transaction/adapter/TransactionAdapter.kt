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

    inner class DepositHolder(rowTransactionDepositBinding: RowTransactionDepositBinding) :
        RecyclerView.ViewHolder(rowTransactionDepositBinding.root) {
        val textTransactionDate = rowTransactionDepositBinding.textTransactionDate
        val transctionClientNameTextView = rowTransactionDepositBinding.transctionClientNameTextView
        val depositPriceTextVie = rowTransactionDepositBinding.depositPriceTextView

    }

    inner class TransactionHolder(rowTransactionBinding: RowTransactionBinding) :
        RecyclerView.ViewHolder(rowTransactionBinding.root) {
        val date = rowTransactionBinding.textTransactionDate
        val clientName = rowTransactionBinding.transctionClientNameTextView
        val productName = rowTransactionBinding.textProductName
        val productCount = rowTransactionBinding.textProductCount
        val unitPrice = rowTransactionBinding.textUnitPrice
        val totalAmount = rowTransactionBinding.textTotalAmount
        val recievedAmount = rowTransactionBinding.textRecievedAmount
        val recievables = rowTransactionBinding.textRecievables

    }

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
                transactions[position].setViewText(
                    item.textTransactionDate,
                    item.transctionClientNameTextView,
                    item.depositPriceTextVie
                )
            }

            2 -> {
                val item = holder as TransactionHolder
                transactions[position].setViewText(
                    item.date,
                    item.clientName,
                    item.productName,
                    item.productCount,
                    item.unitPrice,
                    item.totalAmount,
                    item.recievedAmount,
                    item.recievables
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return transactions[position].getTransactionType()
    }

    fun addTransaction() {
//        transactions.add(Transaction(CustomTransactionData(
//
//        )))
        notifyDataSetChanged()
    }

}