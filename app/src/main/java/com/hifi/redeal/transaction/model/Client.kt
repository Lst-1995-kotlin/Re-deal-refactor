package com.hifi.redeal.transaction.model

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

    fun getClientInfo() = "${clientSimpleData.clientName} ${clientSimpleData.clientManagerName}"

    fun getClientState() = clientSimpleData.clientState

    fun getClientBookmark() = clientSimpleData.isBookmark
}
