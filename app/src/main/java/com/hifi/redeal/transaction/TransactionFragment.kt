package com.hifi.redeal.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hifi.redeal.databinding.FragmentTransactionBinding
import com.hifi.redeal.transaction.adapter.TransactionAdapter
import com.hifi.redeal.transaction.model.Transaction

class TransactionFragment : Fragment() {

    private lateinit var fragmentTransactionBinding: FragmentTransactionBinding

    private val transactions = mutableListOf<Transaction>()
    private val transactionAdapter = TransactionAdapter(transactions)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        setTransactionView(inflater)
        setViewModel()
        return fragmentTransactionBinding.root
    }

    private fun setTransactionView(inflater: LayoutInflater) {
        fragmentTransactionBinding = FragmentTransactionBinding.inflate(inflater)
        fragmentTransactionBinding.run {
            transactionRecyclerView.run {
                adapter = transactionAdapter
                layoutManager = LinearLayoutManager(context)
            }
        }
    }

    private fun setViewModel() {
        val transactionVM = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]
        val uid = Firebase.auth.uid!!

        transactionVM.run {
            transactionList.observe(viewLifecycleOwner) {
                it.sortedByDescending { it.date }.forEach { transactionData ->
                    transactionAdapter.addTransaction(Transaction(transactionData))
                }
            }
            getAllTransactionData(uid)
            getNextTransactionIdx(uid)
            getUserAllClient(uid)
        }
    }
}
