package com.hifi.redeal.trade.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hifi.redeal.trade.data.model.TradeData
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@InstallIn(SingletonComponent::class)
@Module
class TradeAdapterDiffCallback @Inject constructor() : DiffUtil.ItemCallback<TradeData>() {

    override fun areItemsTheSame(oldItem: TradeData, newItem: TradeData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TradeData, newItem: TradeData): Boolean {
        return oldItem == newItem
    }

}