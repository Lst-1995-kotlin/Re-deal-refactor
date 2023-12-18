package com.hifi.redeal.transaction.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hifi.redeal.databinding.FragmentTransactionBinding
import com.hifi.redeal.transaction.adapter.TransactionAdapter
import com.hifi.redeal.transaction.viewmodel.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionFragment : Fragment() {

    private lateinit var fragmentTransactionBinding: FragmentTransactionBinding
    private val transactionViewModel: TransactionViewModel by viewModels()
    private val transactionAdapter = TransactionAdapter()

    var depositDialog = TransactionDepositFragment()

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

            ImgBtnAddDeposit.setOnClickListener {
                depositDialog.show(childFragmentManager, "temp")
            }

            ImgBtnAddTransaction.setOnClickListener {
                // transactionDepositDialog.show(WITHDRAWAL_TRANSACTION)
            }
        }
    }

    private fun setViewModel() {
        transactionViewModel.run {
            transactionList.observe(viewLifecycleOwner) {
                transactionAdapter.transactionsClear()
                it.forEach { transaction ->
                    transactionAdapter.adapterAddTransaction(transaction)
                    transactionAdapter.sortTransaction(false)
                }
            }
            getAllTransactionData(arguments?.getLong("clientIdx"))
            getUserAllClient()
        }
    }
}
