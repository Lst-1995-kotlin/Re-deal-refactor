package com.hifi.redeal.transaction.model

import com.hifi.redeal.databinding.TransactionSelectClientItemBinding
import com.hifi.redeal.transaction.util.ClientConfiguration.getClientBookmarkResource
import com.hifi.redeal.transaction.util.ClientConfiguration.getClientStateResource
import javax.inject.Inject

class Client @Inject constructor(
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

    fun filter(value: String) =
        (clientSimpleData.clientName.contains(value) || clientSimpleData.clientManagerName.contains(value))
}
