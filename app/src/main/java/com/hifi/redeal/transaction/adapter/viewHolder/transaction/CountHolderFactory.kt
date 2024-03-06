package com.hifi.redeal.transaction.adapter.viewHolder.transaction

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.databinding.RowTransactionCountBinding
import com.hifi.redeal.transaction.adapter.viewHolder.ViewHolderFactory
import com.hifi.redeal.transaction.viewmodel.TradeViewModel

class CountHolderFactory(
    private val tradeViewModel: TradeViewModel,
    private val viewLifecycleOwner: LifecycleOwner
) : ViewHolderFactory {
    override fun create(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowTransactionCountBinding.inflate(inflater, parent, false)
        return CountHolder(binding, tradeViewModel, viewLifecycleOwner)
    }
}