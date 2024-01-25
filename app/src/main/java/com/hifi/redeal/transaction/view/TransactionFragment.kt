package com.hifi.redeal.transaction.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hifi.redeal.MainActivity
import com.hifi.redeal.MainActivity.Companion.TRANSACTION_DEPOSIT_FRAGMENT
import com.hifi.redeal.MainActivity.Companion.TRANSACTION_SALES_FRAGMENT
import com.hifi.redeal.databinding.FragmentTransactionBinding
import com.hifi.redeal.transaction.adapter.TransactionAdapter
import com.hifi.redeal.transaction.adapter.TransactionAdapter.Companion.SALES_TRANSACTION
import com.hifi.redeal.transaction.util.TransactionNumberFormatUtil.replaceNumberFormat
import com.hifi.redeal.transaction.viewmodel.ClientViewModel
import com.hifi.redeal.transaction.viewmodel.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionFragment : Fragment() {
    private lateinit var fragmentTransactionBinding: FragmentTransactionBinding
    private val transactionViewModel: TransactionViewModel by activityViewModels()
    private val clientViewModel: ClientViewModel by activityViewModels()
    private lateinit var mainActivity: MainActivity
    private lateinit var transactionAdapter: TransactionAdapter

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

            transactionAdapter = TransactionAdapter(transactionViewModel, mainActivity)

            transactionRecyclerView.run {
                adapter = transactionAdapter
                layoutManager = LinearLayoutManager(context)
            }

            ImgBtnAddDeposit.setOnClickListener {
                mainActivity.replaceFragment(TRANSACTION_DEPOSIT_FRAGMENT, true, null)
            }

            ImgBtnAddTransaction.setOnClickListener {
                mainActivity.replaceFragment(TRANSACTION_SALES_FRAGMENT, true, null)
            }
        }
    }

    private fun setViewModel() {
        transactionViewModel.transactionList.observe(viewLifecycleOwner) { transactions ->
            val totalSalesCount =
                transactions.filter { it.getTransactionType() == SALES_TRANSACTION }.size
            val totalSalesAmount = transactions.sumOf { it.calculateSalesAmount() }
            val totalReceivables = transactions.sumOf { it.getReceivables() }
            fragmentTransactionBinding.run {
                textTotalSalesCount.text = replaceNumberFormat(totalSalesCount)
                textTotalSales.text = replaceNumberFormat(totalSalesAmount)
                textTotalReceivables.text = replaceNumberFormat(totalSalesAmount - totalReceivables)
            }
        }
        clientViewModel.selectedClient.observe(viewLifecycleOwner) { client ->
            client?.let { transactionViewModel.setSelectClientIndex(it.getClientIdx()) }
                ?: transactionViewModel.setSelectClientIndex(null)
        }

        clientViewModel.setSelectClient(arguments?.getLong("clientIdx"))
    }
}
