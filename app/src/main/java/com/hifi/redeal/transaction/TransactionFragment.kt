package com.hifi.redeal.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hifi.redeal.databinding.FragmentTransactionBinding
import com.hifi.redeal.transaction.adapter.TransactionAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionFragment : Fragment() {

    private lateinit var fragmentTransactionBinding: FragmentTransactionBinding
    private val transactionAdapter = TransactionAdapter()
    private val transactionViewModel: TransactionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        fragmentTransactionBinding = FragmentTransactionBinding.inflate(inflater)

        setTransactionView()
        setViewModel()
        return fragmentTransactionBinding.root
    }

    private fun setTransactionView() {
        fragmentTransactionBinding.run {
            transactionRecyclerView.run {
                adapter = transactionAdapter
                layoutManager = LinearLayoutManager(context)
            }
        }
    }

    private fun setViewModel() {
        transactionViewModel.run {
            transactionList.observe(viewLifecycleOwner) {
                transactionAdapter.transactionsClear()
                it.forEach { transaction ->
                    transactionAdapter.addTransaction(transaction)
                    transactionAdapter.sortTransaction(false)
                }
            }
            getAllTransactionData()
            getNextTransactionIdx()
            getUserAllClient()
        }
    }
}
