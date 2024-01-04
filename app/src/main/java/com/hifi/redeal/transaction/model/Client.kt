package com.hifi.redeal.transaction.model

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.hifi.redeal.databinding.TransactionSelectClientItemBinding
import com.hifi.redeal.transaction.util.ClientConfiguration.Companion.getClientBookmarkResource
import com.hifi.redeal.transaction.util.ClientConfiguration.Companion.getClientStateResource

class Client(
    private val clientData: ClientData,
) {

    fun bind(transactionSelectClientItemBinding: TransactionSelectClientItemBinding) {
        transactionSelectClientItemBinding.run {
            getClientStateResource(clientData.clientState)?.let {
                selectTransactionClinetState.setBackgroundResource(it)
            }
            getClientBookmarkResource(clientData.isBookmark)?.let {
                selectTransactionClientBookmarkView.setBackgroundResource(it)
            }
            selectTransactionClientName.text = clientData.clientName
            selectTransactionClientManagerName.text = clientData.clientManagerName
        }
    }

    fun filter(value: String) = clientData.clientName.contains(value) ||
        clientData.clientManagerName.contains(value)

    fun setClientInfoView(textView: TextView) {
        textView.text = "${clientData.clientName} ${clientData.clientManagerName}"
    }

    fun setClientStateView(imageView: ImageView) {
        val clientStateResource = getClientStateResource(clientData.clientState)
        imageView.setImageResource(clientStateResource ?: 0)
        imageView.visibility = clientStateResource?.let { View.VISIBLE } ?: View.GONE
    }

    fun setClientBookmarkView(imageView: ImageView) {
        val clientBookmarkResource = getClientBookmarkResource(clientData.isBookmark)
        imageView.setImageResource(clientBookmarkResource ?: 0)
        imageView.visibility = clientBookmarkResource?.let { View.VISIBLE } ?: View.GONE
    }

    fun getClientIdx() = clientData.clientIdx
}
