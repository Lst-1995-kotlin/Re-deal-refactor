package com.hifi.redeal.transaction.viewHolder.client

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.databinding.TransactionSelectClientItemBinding
import com.hifi.redeal.transaction.viewHolder.ViewHolderFactory
import com.hifi.redeal.transaction.viewmodel.TransactionClientViewModel

class TransactionClientHolderFactory(
    private val transactionClientViewModel: TransactionClientViewModel,
    private val dialogFragment: DialogFragment
) : ViewHolderFactory {
    override fun create(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TransactionSelectClientItemBinding.inflate(inflater, parent, false)
        return TransactionClientHolder(binding, transactionClientViewModel, dialogFragment)
    }

}