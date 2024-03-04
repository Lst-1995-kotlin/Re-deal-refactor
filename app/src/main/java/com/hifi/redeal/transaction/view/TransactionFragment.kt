package com.hifi.redeal.transaction.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hifi.redeal.R
import com.hifi.redeal.databinding.FragmentTransactionBinding
import com.hifi.redeal.transaction.adapter.TransactionAdapter
import com.hifi.redeal.transaction.adapter.TransactionAdapterDiffCallback
import com.hifi.redeal.transaction.adapter.viewHolder.ViewHolderFactory
import com.hifi.redeal.transaction.adapter.viewHolder.transaction.CountHolderFactory
import com.hifi.redeal.transaction.adapter.viewHolder.transaction.DepositHolderFactory
import com.hifi.redeal.transaction.adapter.viewHolder.transaction.SalesHolderFactory
import com.hifi.redeal.transaction.configuration.TransactionType
import com.hifi.redeal.transaction.util.TransactionNumberFormatUtil.replaceNumberFormat
import com.hifi.redeal.transaction.view.dialog.TransactionAddSelectDialog
import com.hifi.redeal.transaction.viewmodel.TransactionClientViewModel
import com.hifi.redeal.transaction.viewmodel.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TransactionFragment : Fragment() {
    private lateinit var fragmentTransactionBinding: FragmentTransactionBinding
    private val transactionViewModel: TransactionViewModel by activityViewModels()
    private val transactionClientViewModel: TransactionClientViewModel by activityViewModels()
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var transactionAddSelectDialog: TransactionAddSelectDialog

    @Inject
    lateinit var transactionAdapterDiffCallback: TransactionAdapterDiffCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        fragmentTransactionBinding = FragmentTransactionBinding.inflate(inflater)
        transactionAddSelectDialog =
            TransactionAddSelectDialog(findNavController(), requireContext(), inflater)

        setAdapter()
        setBind()
        setViewModel()

        return fragmentTransactionBinding.root
    }

    private fun setAdapter() {
        val viewHolderFactories = HashMap<Int, ViewHolderFactory>()
        viewHolderFactories[TransactionType.DEPOSIT.type] =
            DepositHolderFactory(transactionViewModel)
        viewHolderFactories[TransactionType.SALES.type] =
            SalesHolderFactory(transactionViewModel)
        viewHolderFactories[TransactionType.COUNT.type] =
            CountHolderFactory(transactionViewModel, viewLifecycleOwner)

        transactionAdapter =
            TransactionAdapter(viewHolderFactories, transactionAdapterDiffCallback)
    }

    private fun setBind() {
        fragmentTransactionBinding.transactionRecyclerView.run {
            adapter = transactionAdapter
            layoutManager = LinearLayoutManager(context)
        }

        fragmentTransactionBinding.transactionAddButton.setOnClickListener {
            transactionAddSelectDialog.dialogShow()
        }

        fragmentTransactionBinding.toolbarTransactionMain.setOnMenuItemClickListener {
            findNavController().navigate(R.id.action_transactionFragment_to_transactionsEditFragment)
            true
        }
    }

    private fun setViewModel() {
        transactionViewModel.transactionList.observe(viewLifecycleOwner) { transactions -> // 어댑터에 표시하는 거래내역들
            transactionAdapter.submitList(transactions.sortedByDescending { it.getTransactionDate() }) {
                transactionViewModel.postValueScrollPosition()
            }
            // 어댑터에 표시하는 거래내역들의 합계
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

        transactionViewModel.transactionPosition.observe(viewLifecycleOwner) {// 수정 프래그먼트를 띄웠을 경우 해당 포지션으로 이동 시킴.
            val layoutManager =
                fragmentTransactionBinding.transactionRecyclerView.layoutManager as LinearLayoutManager
            layoutManager.scrollToPosition(it)
        }

        transactionViewModel.modifyTransaction.observe(viewLifecycleOwner) {// 수정하려는 거래내역이 선택되었을 때
            it?.let {
                transactionClientViewModel.setSelectClient(it.getClientInformation())
                when (it.getTransactionType()) {
                    TransactionType.SALES.type -> {
                        findNavController().navigate(R.id.action_transactionFragment_to_transactionSalesModifyFragment)
                    }

                    TransactionType.DEPOSIT.type -> {
                        findNavController().navigate(R.id.action_transactionFragment_to_transactionDepositModifyFragment)
                    }
                }
            } ?: transactionClientViewModel.setSelectClientIndex(null)
        }

        transactionViewModel.tempList.observe(viewLifecycleOwner) {
            fragmentTransactionBinding.textTotalSales.text = "테스트 ${it.size}"
        }
        transactionViewModel.setSelectClientIndex(null) // 기존 선택한 클라이언트 정보를 초기화 시킨다.
    }
}
