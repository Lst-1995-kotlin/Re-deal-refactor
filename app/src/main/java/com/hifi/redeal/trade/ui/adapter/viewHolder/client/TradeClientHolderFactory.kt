package com.hifi.redeal.trade.ui.adapter.viewHolder.client

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.databinding.TradeSelectClientItemBinding
import com.hifi.redeal.trade.data.model.TradeClientData
import com.hifi.redeal.trade.ui.adapter.viewHolder.ViewHolderFactory
import javax.inject.Inject

class TradeClientHolderFactory @Inject constructor() : ViewHolderFactory {

    private lateinit var onClickListener: (TradeClientData) -> Unit

    fun setOnClickListener(onClickListener: (TradeClientData) -> Unit) {
        this.onClickListener = onClickListener
    }

    override fun create(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TradeSelectClientItemBinding.inflate(inflater, parent, false)
        return TradeClientHolder(binding, onClickListener)
    }

}