//package com.hifi.redeal.transaction.adapter
//
//import android.util.Log
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.ListAdapter
//import androidx.recyclerview.widget.RecyclerView
//import com.hifi.redeal.transaction.adapter.viewHolder.ViewHolderFactory
//import com.hifi.redeal.transaction.adapter.viewHolder.client.TransactionClientHolder
//
//class ClientAdapter(
//    private val viewHolderFactories: HashMap<String, ViewHolderFactory>,
//    diffCallback: ClientAdapterDiffCallback
//) : ListAdapter<Client, RecyclerView.ViewHolder>(diffCallback) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        val factory = viewHolderFactories["client"]
//        return factory?.create(parent) ?: createDefaultViewHolder(parent)
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        val transactionClientHolder = holder as TransactionClientHolder
//        transactionClientHolder.bind(currentList[position])
//    }
//
//    fun setClients(clients: List<Client>) {
//        submitList(clients)
//    }
//
//    private fun createDefaultViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
//        Log.e("ClientAdapter", "올바르지 못한 클라이언트 타입 입니다")
//        val view = View(parent.context)
//        return object : RecyclerView.ViewHolder(view) {}
//    }
//}
