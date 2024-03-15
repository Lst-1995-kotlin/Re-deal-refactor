package com.hifi.redeal.trade.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.trade.ui.adapter.viewHolder.ViewHolderFactory
import com.hifi.redeal.trade.ui.adapter.viewHolder.trade.CountHolder
import com.hifi.redeal.trade.ui.adapter.viewHolder.trade.DepositHolder
import com.hifi.redeal.trade.ui.adapter.viewHolder.trade.SalesHolder
import com.hifi.redeal.trade.configuration.TradeType
import com.hifi.redeal.trade.data.model.TradeData

class TradeAdapter(
    private val viewHolderFactories: Map<Int, ViewHolderFactory>,
    diffCallback: TradeAdapterDiffCallback
) : ListAdapter<TradeData, RecyclerView.ViewHolder>(diffCallback) {

    fun updateCount() {
        notifyItemChanged(itemCount - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val factory = viewHolderFactories[viewType]
            ?: throw IllegalArgumentException("올바르지 못한 클라이언트 타입 입니다.")
        return factory.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DepositHolder -> {
                holder.bind(currentList[position])
            }

            is SalesHolder -> {
                holder.bind(currentList[position])
            }

            is CountHolder -> {
                holder.bind(currentList.size)
            }
        }
    }

    override fun getItemCount() = currentList.size + 1

    override fun getItemViewType(position: Int): Int {
        if (position == itemCount - 1) return TradeType.COUNT.type
        return when {
            currentList[position].type -> TradeType.DEPOSIT.type
            !currentList[position].type -> TradeType.SALES.type
            else -> TradeType.ERROR.type
        }
    }
}


