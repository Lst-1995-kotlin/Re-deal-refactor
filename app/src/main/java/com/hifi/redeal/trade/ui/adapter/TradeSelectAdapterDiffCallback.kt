package com.hifi.redeal.trade.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hifi.redeal.trade.data.model.TradeSelectData
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@InstallIn(SingletonComponent::class)
@Module
class TradeSelectAdapterDiffCallback @Inject constructor() :
    DiffUtil.ItemCallback<TradeSelectData>() {

    override fun areItemsTheSame(oldItem: TradeSelectData, newItem: TradeSelectData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TradeSelectData, newItem: TradeSelectData): Boolean {
        return oldItem == newItem
    }

}