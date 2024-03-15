package com.hifi.redeal.trade.ui.adapter.viewHolder.client

import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.databinding.TransactionSelectClientItemBinding
import com.hifi.redeal.trade.configuration.ClientConfiguration.Companion.setClientBookmarkResource
import com.hifi.redeal.trade.configuration.ClientConfiguration.Companion.setClientStateResource
import com.hifi.redeal.trade.data.model.ClientData

class TransactionClientHolder(
    private val transactionSelectClientItemBinding: TransactionSelectClientItemBinding,
    //private val transactionClientViewModel: TransactionClientViewModel,
    private val dialogFragment: DialogFragment
) : RecyclerView.ViewHolder(transactionSelectClientItemBinding.root) {
    fun bind(clientData: ClientData) {
        transactionSelectClientItemBinding.run {
            setClientStateResource(
                clientData.state,
                selectTransactionClinetState
            )
            setClientBookmarkResource(
                clientData.bookmark,
                selectTransactionClientBookmarkView
            )
            selectTransactionClientName.text = clientData.name
            selectTransactionClientManagerName.text = clientData.managerName
        }
        transactionSelectClientItemBinding.root.setOnClickListener {
            //transactionClientViewModel.setSelectClient(clientData.id)
            dialogFragment.dismiss()
        }
    }
}