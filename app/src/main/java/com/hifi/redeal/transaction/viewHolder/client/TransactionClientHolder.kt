package com.hifi.redeal.transaction.viewHolder.client

import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.databinding.TransactionSelectClientItemBinding
import com.hifi.redeal.transaction.configuration.ClientConfiguration.Companion.setClientBookmarkResource
import com.hifi.redeal.transaction.configuration.ClientConfiguration.Companion.setClientStateResource
import com.hifi.redeal.transaction.model.Client
import com.hifi.redeal.transaction.viewmodel.TransactionClientViewModel

class TransactionClientHolder(
    private val transactionSelectClientItemBinding: TransactionSelectClientItemBinding,
    private val transactionClientViewModel: TransactionClientViewModel,
    private val dialogFragment: DialogFragment
) : RecyclerView.ViewHolder(transactionSelectClientItemBinding.root) {
    fun bind(client: Client) {
        transactionSelectClientItemBinding.run {
            val clientValuesMap = client.getClientValuesMap()
            setClientStateResource(
                clientValuesMap["clientState"] as Long,
                selectTransactionClinetState
            )
            setClientBookmarkResource(
                clientValuesMap["isBookmark"] as Boolean,
                selectTransactionClientBookmarkView
            )
            selectTransactionClientName.text = clientValuesMap["clientName"] as String
            selectTransactionClientManagerName.text = clientValuesMap["clientManagerName"] as String
        }
        transactionSelectClientItemBinding.root.setOnClickListener {
            transactionClientViewModel.setSelectClient(client)
            dialogFragment.dismiss()
        }
    }
}