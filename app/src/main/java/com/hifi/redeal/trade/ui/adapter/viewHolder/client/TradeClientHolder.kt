package com.hifi.redeal.trade.ui.adapter.viewHolder.client

import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.databinding.TransactionSelectClientItemBinding
import com.hifi.redeal.trade.configuration.ClientConfiguration.Companion.setClientBookmarkResource
import com.hifi.redeal.trade.configuration.ClientConfiguration.Companion.setClientStateResource
import com.hifi.redeal.trade.data.model.TradeClientData

class TradeClientHolder(
    private val transactionSelectClientItemBinding: TransactionSelectClientItemBinding,
    private val onClickListener: (TradeClientData) -> Unit
) : RecyclerView.ViewHolder(transactionSelectClientItemBinding.root) {
    fun bind(tradeClientData: TradeClientData) {
        transactionSelectClientItemBinding.run {
            setClientStateResource(
                tradeClientData.state,
                selectTransactionClinetState
            )
            setClientBookmarkResource(
                tradeClientData.bookmark,
                selectTransactionClientBookmarkView
            )
            selectTransactionClientName.text = tradeClientData.name
            selectTransactionClientManagerName.text = tradeClientData.managerName
        }
        transactionSelectClientItemBinding.root.setOnClickListener {
            onClickListener(tradeClientData)
        }
    }
}