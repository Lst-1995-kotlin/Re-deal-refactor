package com.hifi.redeal.transaction.model

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.hifi.redeal.databinding.TransactionSelectClientItemBinding
import com.hifi.redeal.transaction.util.ClientConfiguration.Companion.getClientBookmarkResource
import com.hifi.redeal.transaction.util.ClientConfiguration.Companion.getClientStateResource

class Client(
    private val clientSimpleData: ClientSimpleData,
) {

    fun bind(transactionSelectClientItemBinding: TransactionSelectClientItemBinding) {
        transactionSelectClientItemBinding.run {
            getClientStateResource(clientSimpleData.clientState)?.let {
                selectTransactionClinetState.setBackgroundResource(it)
            }
            getClientBookmarkResource(clientSimpleData.isBookmark)?.let {
                selectTransactionClientBookmarkView.setBackgroundResource(it)
            }
            selectTransactionClientName.text = clientSimpleData.clientName
            selectTransactionClientManagerName.text = clientSimpleData.clientManagerName
        }
    }

    fun filter(value: String) = clientSimpleData.clientName.contains(value) ||
        clientSimpleData.clientManagerName.contains(value)

    fun setClientInfoView(textView: TextView) {
        textView.text = "${clientSimpleData.clientName} ${clientSimpleData.clientManagerName}"
    }

    fun setClientStateView(imageView: ImageView) {
        val clientStateResource = getClientStateResource(clientSimpleData.clientState)
        imageView.setImageResource(clientStateResource ?: 0)
        imageView.visibility = clientStateResource?.let { View.VISIBLE } ?: View.GONE
    }

    fun setClientBookmarkView(imageView: ImageView) {
        val clientBookmarkResource = getClientBookmarkResource(clientSimpleData.isBookmark)
        imageView.setImageResource(clientBookmarkResource ?: 0)
        imageView.visibility = clientBookmarkResource?.let { View.VISIBLE } ?: View.GONE
    }

    fun getClientIdx() = clientSimpleData.clientIdx
}
