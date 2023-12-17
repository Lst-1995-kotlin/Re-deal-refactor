package com.hifi.redeal.transaction.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.databinding.TransactionSelectClientItemBinding
import com.hifi.redeal.transaction.model.ClientSimpleData

class ClientAdapter : RecyclerView.Adapter<ClientAdapter.TransactionClientHolder>() {
    private val clients = emptyList<ClientSimpleData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionClientHolder {
        val inflater = LayoutInflater.from(parent.context)
        val transactionSelectClientItemBinding =
            TransactionSelectClientItemBinding.inflate(inflater)
        transactionSelectClientItemBinding.root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        )
        return TransactionClientHolder(transactionSelectClientItemBinding)
    }

    override fun getItemCount(): Int {
        return clients.size
    }

    override fun onBindViewHolder(holder: TransactionClientHolder, position: Int) {
        holder.bind(clients[position])
    }

    inner class TransactionClientHolder(
        private val transactionSelectClientItemBinding: TransactionSelectClientItemBinding,
    ) : RecyclerView.ViewHolder(transactionSelectClientItemBinding.root) {
        fun bind(clientSimpleData: ClientSimpleData) {
            transactionSelectClientItemBinding.run {
                selectTransactionClientName.text = clientSimpleData.clientName
            }
        }
    }
}
