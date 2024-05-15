package com.hifi.redeal.trade.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.trade.configuration.TradeType
import com.hifi.redeal.trade.data.model.TradeSelectData
import com.hifi.redeal.trade.ui.adapter.viewHolder.ViewHolderFactory
import com.hifi.redeal.trade.ui.adapter.viewHolder.tradeEdit.DepositSelectHolder
import com.hifi.redeal.trade.ui.adapter.viewHolder.tradeEdit.SalesSelectHolder

class TradeSelectAdapter(
    private val viewHolderFactories: Map<Int, ViewHolderFactory>,
    diffCallback: TradeSelectAdapterDiffCallback
) : ListAdapter<TradeSelectData, RecyclerView.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val factory = viewHolderFactories[viewType]
            ?: throw IllegalArgumentException("올바르지 못한 클라이언트 타입 입니다.")
        return factory.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DepositSelectHolder -> {
                holder.bind(currentList[position])
            }

            is SalesSelectHolder -> {
                holder.bind(currentList[position])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (currentList[position].type) {
            TradeType.DEPOSIT.type -> TradeType.DEPOSIT.type
            TradeType.SALES.type -> TradeType.SALES.type
            else -> TradeType.ERROR.type
        }
    }
}


