package com.hifi.redeal.transaction.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.MainActivity
import com.hifi.redeal.databinding.RowTransactionDepositBinding
import com.hifi.redeal.databinding.RowTransactionSalesBinding
import com.hifi.redeal.transaction.viewHolder.DepositHolder
import com.hifi.redeal.transaction.viewHolder.SalesHolder
import com.hifi.redeal.transaction.viewmodel.TransactionViewModel

class TransactionAdapter(
    private val transactionViewModel: TransactionViewModel,
    private val recyclerView: RecyclerView,
    private val mainActivity: MainActivity,
    lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val diffCallback = TransactionAdapterDiffCallback()
    private val differ = AsyncListDiffer(this, diffCallback)

    init {
        transactionViewModel.transactionList.observe(lifecycleOwner) { transactions ->
            differ.submitList(transactions.sortedByDescending { it.getTransactionDate() }) {
                recyclerView.layoutManager?.scrollToPosition(0)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return when (viewType) {
            DEPOSIT_TRANSACTION -> {
                val rowTransactionDepositBinding =
                    RowTransactionDepositBinding.inflate(inflater)
                rowTransactionDepositBinding.root.layoutParams = layoutParams
                DepositHolder(rowTransactionDepositBinding, mainActivity, transactionViewModel)
            }

            SALES_TRANSACTION -> {
                val rowTransactionSalesBinding =
                    RowTransactionSalesBinding.inflate(inflater)
                rowTransactionSalesBinding.root.layoutParams = layoutParams
                SalesHolder(rowTransactionSalesBinding, mainActivity, transactionViewModel)
            }

            else -> {
                Log.e("TransactionAdapter", "올바르지 못한 거래 타입 입니다: $viewType")
                val view = View(parent.context)
                object : RecyclerView.ViewHolder(view) {}
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val transaction = differ.currentList[position]
        when (holder.itemViewType) {
            DEPOSIT_TRANSACTION -> {
                val item = holder as DepositHolder
                item.bind(transaction)
            }

            SALES_TRANSACTION -> {
                val item = holder as SalesHolder
                item.bind(transaction)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return differ.currentList[position].getTransactionType()
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    companion object {
        const val ERROR_TRANSACTION = 0
        const val DEPOSIT_TRANSACTION = 1
        const val SALES_TRANSACTION = 2
    }
}

