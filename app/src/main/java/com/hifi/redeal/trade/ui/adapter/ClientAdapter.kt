package com.hifi.redeal.trade.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.trade.data.model.TradeClientData
import com.hifi.redeal.trade.ui.adapter.viewHolder.ViewHolderFactory
import com.hifi.redeal.trade.ui.adapter.viewHolder.client.TradeClientHolder


class ClientAdapter(
    private val viewHolderFactories: Map<String, ViewHolderFactory>,
    diffCallback: ClientAdapterDiffCallback
) : ListAdapter<TradeClientData, RecyclerView.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val factory = viewHolderFactories["client"]
            ?: throw IllegalArgumentException("올바르지 못한 클라이언트 타입 입니다.")
        return factory.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val tradeClientHolder = holder as TradeClientHolder
        tradeClientHolder.bind(currentList[position])
    }
}
