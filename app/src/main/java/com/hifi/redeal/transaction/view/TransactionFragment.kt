package com.hifi.redeal.transaction.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hifi.redeal.MainActivity
import com.hifi.redeal.MainActivity.Companion.TRANSACTION_DEPOSIT_FRAGMENT
import com.hifi.redeal.databinding.FragmentTransactionBinding
import com.hifi.redeal.transaction.adapter.TransactionAdapter
import com.hifi.redeal.transaction.viewmodel.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TransactionFragment : Fragment() {

    private lateinit var fragmentTransactionBinding: FragmentTransactionBinding
    private val transactionViewModel: TransactionViewModel by viewModels()
    private lateinit var mainActivity: MainActivity

    @Inject
    lateinit var transactionAdapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        fragmentTransactionBinding = FragmentTransactionBinding.inflate(inflater)
        mainActivity = activity as MainActivity

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
                mainActivity.replaceFragment(TRANSACTION_DEPOSIT_FRAGMENT, true, null)
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
        }
    }
}