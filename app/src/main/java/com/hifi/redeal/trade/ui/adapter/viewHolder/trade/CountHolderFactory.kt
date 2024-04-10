package com.hifi.redeal.trade.ui.adapter.viewHolder.trade

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.databinding.RowTradeCountBinding
import com.hifi.redeal.trade.ui.adapter.viewHolder.ViewHolderFactory
import javax.inject.Inject

class CountHolderFactory @Inject constructor(): ViewHolderFactory {

    override fun create(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowTradeCountBinding.inflate(inflater, parent, false)
        return CountHolder(binding)
    }
}