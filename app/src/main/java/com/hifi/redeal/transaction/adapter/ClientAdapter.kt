package com.hifi.redeal.transaction.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.databinding.TransactionSelectClientItemBinding
import com.hifi.redeal.transaction.model.Client
import com.hifi.redeal.transaction.model.ClientSimpleData
import com.hifi.redeal.transaction.repository.TransactionRepository
import javax.inject.Inject

class ClientAdapter @Inject constructor(
    private val transactionRepository: TransactionRepository,
) :
    RecyclerView.Adapter<ClientAdapter.TransactionClientHolder>() {
    private val clients = mutableListOf<Client>()
    private var filterClients = listOf<Client>()
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
        transactionRepository.getUserAllClient {
            for (c1 in it.result) {
                addClient(
                    Client(
                        ClientSimpleData(
                            c1["clientIdx"] as Long,
                            c1["clientName"] as String,
                            c1["clientManagerName"] as String,
                            c1["clientState"] as Long,
                            c1["isBookmark"] as Boolean,
                        ),
                    ),
                )
            }
        }
    }

    private fun addClient(csd: Client) {
        clients.add(csd)
        filterClients = clients
        notifyDataSetChanged()
    }

    inner class TransactionClientHolder(
        private val transactionSelectClientItemBinding: TransactionSelectClientItemBinding,
    ) : RecyclerView.ViewHolder(transactionSelectClientItemBinding.root) {
        fun bind(client: Client) {
            client.bind(transactionSelectClientItemBinding)
        }
    }
}
