package com.hifi.redeal.transaction.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.databinding.TransactionSelectClientItemBinding
import com.hifi.redeal.transaction.model.Client
import com.hifi.redeal.transaction.util.ClientDiffCallback
import com.hifi.redeal.transaction.viewmodel.ClientViewModel

class ClientAdapter(
    private val clientViewModel: ClientViewModel,
) : ListAdapter<Client, ClientAdapter.TransactionClientHolder>(ClientDiffCallback()) {

    init {
        clientViewModel.clients.observeForever {
            submitList(it)
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

    override fun onBindViewHolder(holder: TransactionClientHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun clientFind(value: String) {
        val filterList =
            clientViewModel.clients.value?.filter { it.filter(value) }
        submitList(filterList)
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
