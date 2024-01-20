package com.hifi.redeal.transaction.model

import android.widget.TextView
import com.hifi.redeal.databinding.TransactionSelectClientItemBinding
import com.hifi.redeal.transaction.configuration.ClientConfiguration.Companion.setClientBookmarkResource
import com.hifi.redeal.transaction.configuration.ClientConfiguration.Companion.setClientStateResource


class Client(
    private val clientData: ClientData
) {

    override fun hashCode(): Int {
        return clientData.hashCode()
    }
    override fun equals(other: Any?): Boolean {
        val otherClient = other as Client
        return otherClient.clientData == this.clientData
    }
    fun bind(transactionSelectClientItemBinding: TransactionSelectClientItemBinding) {
        transactionSelectClientItemBinding.run {
            setClientStateResource(clientData.clientState, selectTransactionClinetState)
            setClientBookmarkResource(clientData.isBookmark, selectTransactionClientBookmarkView)
            selectTransactionClientName.text = clientData.clientName
            selectTransactionClientManagerName.text = clientData.clientManagerName
        }
    }

    fun filter(value: String) = clientData.clientName.contains(value) ||
            clientData.clientManagerName.contains(value)

    fun setClientInfoView(textView: TextView) {
        textView.text = "${clientData.clientName} ${clientData.clientManagerName}"
    }

    fun getClientIdx() = clientData.clientIdx
}
