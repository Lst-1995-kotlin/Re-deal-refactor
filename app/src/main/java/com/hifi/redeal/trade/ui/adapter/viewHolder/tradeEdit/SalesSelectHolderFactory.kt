package com.hifi.redeal.trade.ui.adapter.viewHolder.tradeEdit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.databinding.RowTradeSelectSalesBinding
import com.hifi.redeal.trade.data.model.TradeSelectData
import com.hifi.redeal.trade.ui.adapter.viewHolder.ViewHolderFactory
import javax.inject.Inject

class SalesSelectHolderFactory  @Inject constructor(): ViewHolderFactory {

    private lateinit var onClickListener: (TradeSelectData) -> Unit

    fun setOnClickListener(onClickListener: (TradeSelectData) -> Unit) {
        this.onClickListener = onClickListener
    }

    override fun create(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowTradeSelectSalesBinding.inflate(inflater, parent, false)
        return SalesSelectHolder(binding, onClickListener)
    }
}