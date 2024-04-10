package com.hifi.redeal.trade.ui.adapter.viewHolder.client

import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.databinding.TradeSelectClientItemBinding
import com.hifi.redeal.trade.configuration.ClientConfiguration.Companion.setClientBookmarkResource
import com.hifi.redeal.trade.configuration.ClientConfiguration.Companion.setClientStateResource
import com.hifi.redeal.trade.data.model.TradeClientData

class TradeClientHolder(
    private val tradeSelectClientItemBinding: TradeSelectClientItemBinding,
    private val onClickListener: (TradeClientData) -> Unit
) : RecyclerView.ViewHolder(tradeSelectClientItemBinding.root) {
    fun bind(tradeClientData: TradeClientData) {
        tradeSelectClientItemBinding.run {
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
        tradeSelectClientItemBinding.root.setOnClickListener {
            onClickListener(tradeClientData)
        }
    }
}