package com.hifi.redeal.transaction.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.databinding.TransactionSelectClientItemBinding
import com.hifi.redeal.transaction.model.Client
import com.hifi.redeal.transaction.model.ClientSimpleData
import com.hifi.redeal.transaction.repository.ClientRepository
import com.hifi.redeal.transaction.util.ClientConfiguration
import com.hifi.redeal.transaction.viewmodel.ClientViewModel

class ClientAdapter(
    private val clientViewModel: ClientViewModel,
) : RecyclerView.Adapter<ClientAdapter.TransactionClientHolder>() {

    private var clients = listOf<Client>()
    private var filterClients = listOf<Client>()

    init {
        clientViewModel.clients.observeForever {
            clients = it
            filterClients = clients
            notifyDataSetChanged()
        }
    }
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
        return filterClients.size
    }

    override fun onBindViewHolder(holder: TransactionClientHolder, position: Int) {
        holder.bind(filterClients[position])
    }

    fun clientFilterResult(value: String) {
        filterClients = clients.filter { it.filter(value) }
        notifyDataSetChanged()
    }

    fun getClient() {
        clientViewModel.clients.value ?: clientViewModel.getUserAllClient()
    }

    inner class TransactionClientHolder(
        private val transactionSelectClientItemBinding: TransactionSelectClientItemBinding,
    ) : RecyclerView.ViewHolder(transactionSelectClientItemBinding.root) {
        fun bind(client: Client) {
            client.bind(transactionSelectClientItemBinding)
            transactionSelectClientItemBinding.root.setOnClickListener {
                clientViewModel.setSelectClient(client)
            }
        }
    }
}
