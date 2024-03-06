package com.hifi.redeal.transaction.adapter.viewHolder.trade

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.databinding.RowTransactionDepositBinding
import com.hifi.redeal.transaction.adapter.viewHolder.ViewHolderFactory
import com.hifi.redeal.transaction.model.TradeData
import javax.inject.Inject

class DepositHolderFactory @Inject constructor() : ViewHolderFactory {

    private lateinit var onDeleteClickListener: (TradeData) -> Unit
    private lateinit var onEditClickListener: (TradeData) -> Unit

    fun setOnDeleteClickListener(onDeleteClickListener: (TradeData) -> Unit) {
        this.onDeleteClickListener = onDeleteClickListener
    }

    fun setOnEditClickListener(onEditClickListener: (TradeData) -> Unit) {
        this.onEditClickListener = onEditClickListener
    }

    override fun create(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowTransactionDepositBinding.inflate(inflater, parent, false)
        return DepositHolder(binding, onDeleteClickListener, onEditClickListener)
    }
}