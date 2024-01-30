package com.hifi.redeal.transaction.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.databinding.TransactionSelectClientItemBinding
import com.hifi.redeal.transaction.model.Client
import com.hifi.redeal.transaction.viewHolder.ViewHolderFactory
import com.hifi.redeal.transaction.viewHolder.client.TransactionClientHolder
import com.hifi.redeal.transaction.viewmodel.ClientViewModel

class ClientAdapter(
    private val viewHolderFactories: HashMap<String, ViewHolderFactory>,
    diffCallback: ClientAdapterDiffCallback
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val factory = viewHolderFactories["client"]
        return factory?.create(parent) ?: createDefaultViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val transactionClientHolder = holder as TransactionClientHolder
        transactionClientHolder.bind(differ.currentList[position])
    }

    fun setClients(clients: List<Client>) {
        differ.submitList(clients)
    }

    private fun createDefaultViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        Log.e("ClientAdapter", "올바르지 못한 클라이언트 타입 입니다")
        val view = View(parent.context)
        return object : RecyclerView.ViewHolder(view) {}
    }
}
