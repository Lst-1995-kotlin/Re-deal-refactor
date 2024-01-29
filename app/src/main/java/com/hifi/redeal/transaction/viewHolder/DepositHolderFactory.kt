package com.hifi.redeal.transaction.viewHolder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.MainActivity
import com.hifi.redeal.databinding.RowTransactionDepositBinding
import com.hifi.redeal.transaction.viewmodel.TransactionViewModel

class DepositHolderFactory(
    private val mainActivity: MainActivity,
    private val transactionViewModel: TransactionViewModel
) : ViewHolderFactory {
    override fun create(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowTransactionDepositBinding.inflate(inflater, parent, false)
        return DepositHolder(binding, mainActivity, transactionViewModel)
    }
}