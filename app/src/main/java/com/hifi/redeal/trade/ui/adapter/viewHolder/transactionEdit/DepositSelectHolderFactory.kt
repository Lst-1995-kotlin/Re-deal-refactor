package com.hifi.redeal.trade.ui.adapter.viewHolder.transactionEdit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.databinding.RowTradeSelectDepositBinding
import com.hifi.redeal.trade.ui.adapter.viewHolder.ViewHolderFactory
import com.hifi.redeal.trade.domain.viewmodel.TradeViewModel

class DepositSelectHolderFactory(
    private val tradeViewModel: TradeViewModel
) : ViewHolderFactory {
    override fun create(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowTradeSelectDepositBinding.inflate(inflater, parent, false)
        return DepositSelectHolder(binding, tradeViewModel)
    }
}