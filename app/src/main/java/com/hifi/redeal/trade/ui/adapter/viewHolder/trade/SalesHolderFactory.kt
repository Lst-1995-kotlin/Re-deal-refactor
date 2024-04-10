package com.hifi.redeal.trade.ui.adapter.viewHolder.trade

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.databinding.RowTradeSalesBinding
import com.hifi.redeal.trade.data.model.TradeData
import com.hifi.redeal.trade.ui.adapter.viewHolder.ViewHolderFactory
import javax.inject.Inject


class SalesHolderFactory @Inject constructor() : ViewHolderFactory {

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
        val binding = RowTradeSalesBinding.inflate(inflater, parent, false)
        return SalesHolder(binding, onDeleteClickListener, onEditClickListener)
    }
}