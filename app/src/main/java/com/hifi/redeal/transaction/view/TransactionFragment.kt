package com.hifi.redeal.transaction.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hifi.redeal.MainActivity
import com.hifi.redeal.MainActivity.Companion.TRANSACTION_DEPOSIT_FRAGMENT
import com.hifi.redeal.MainActivity.Companion.TRANSACTION_SALES_FRAGMENT
import com.hifi.redeal.databinding.FragmentTransactionBinding
import com.hifi.redeal.transaction.adapter.TransactionAdapter
import com.hifi.redeal.transaction.adapter.TransactionAdapterDiffCallback
import com.hifi.redeal.transaction.configuration.TransactionType
import com.hifi.redeal.transaction.util.TransactionNumberFormatUtil.replaceNumberFormat
import com.hifi.redeal.transaction.viewHolder.DepositHolderFactory
import com.hifi.redeal.transaction.viewHolder.SalesHolderFactory
import com.hifi.redeal.transaction.viewHolder.ViewHolderFactory
import com.hifi.redeal.transaction.viewmodel.ClientViewModel
import com.hifi.redeal.transaction.viewmodel.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TransactionFragment : Fragment() {
    private lateinit var fragmentTransactionBinding: FragmentTransactionBinding
    private val transactionViewModel: TransactionViewModel by activityViewModels()
    private val clientViewModel: ClientViewModel by activityViewModels()
    private lateinit var mainActivity: MainActivity
    private lateinit var transactionAdapter: TransactionAdapter

    @Inject
    lateinit var transactionAdapterDiffCallback: TransactionAdapterDiffCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        fragmentTransactionBinding = FragmentTransactionBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        val viewHolderFactories = HashMap<Int, ViewHolderFactory>()
        viewHolderFactories[TransactionType.DEPOSIT.type] =
            DepositHolderFactory(mainActivity, transactionViewModel)
        viewHolderFactories[TransactionType.SALES.type] =
            SalesHolderFactory(mainActivity, transactionViewModel)

        transactionAdapter =
            TransactionAdapter(viewHolderFactories, transactionAdapterDiffCallback)

        setBind()
        setViewModel()

        return fragmentTransactionBinding.root
    }

    private fun setBind() {
        fragmentTransactionBinding.run {

            transactionRecyclerView.run {
                adapter = transactionAdapter
                layoutManager = LinearLayoutManager(context)
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)

                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                            val firstVisibleItemPosition =
                                layoutManager.findFirstVisibleItemPosition()
                            if (firstVisibleItemPosition == 0) {
                                transactionViewModel.getAllTransactionData()
                            }
                        }
                    }
                })
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
            transactionAdapter.setTransactions(transactions)
            transactionViewModel.postValueScrollPosition()
            val totalSalesCount =
                transactions.count { it.getTransactionType() == TransactionType.SALES.type }
            val totalSalesAmount = transactions.sumOf { it.calculateSalesAmount() }
            val totalReceivables = transactions.sumOf { it.getReceivables() }

            fragmentTransactionBinding.run {
                textTotalSalesCount.text = replaceNumberFormat(totalSalesCount)
                textTotalSales.text = replaceNumberFormat(totalSalesAmount)
                textTotalReceivables.text = replaceNumberFormat(totalSalesAmount - totalReceivables)
            }
        }

        transactionViewModel.transactionPosition.observe(viewLifecycleOwner) {
            val layoutManager =
                fragmentTransactionBinding.transactionRecyclerView.layoutManager as LinearLayoutManager
            layoutManager.scrollToPosition(it)
        }

        clientViewModel.selectedClient.observe(viewLifecycleOwner) { client ->
            client?.let { transactionViewModel.setSelectClientIndex(it.getClientIdx()) }
                ?: transactionViewModel.setSelectClientIndex(null)
        }
        clientViewModel.setSelectClient(arguments?.getLong("clientIdx"))
    }
}
