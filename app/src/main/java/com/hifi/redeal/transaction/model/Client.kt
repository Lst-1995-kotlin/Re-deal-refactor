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

    fun getClientValuesMap(): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        map["clientState"] = clientData.clientState
        map["isBookmark"] = clientData.isBookmark
        map["clientName"] = clientData.clientName
        map["clientManagerName"] = clientData.clientManagerName
        return map
    }

    fun filter(value: String) = clientData.clientName.contains(value) ||
            clientData.clientManagerName.contains(value)

    fun setClientInfoView(textView: TextView) {
        textView.text = "${clientData.clientName} ${clientData.clientManagerName}"
    }

    fun getClientIdx() = clientData.clientIdx

    fun getClientName() =clientData.clientName
}
