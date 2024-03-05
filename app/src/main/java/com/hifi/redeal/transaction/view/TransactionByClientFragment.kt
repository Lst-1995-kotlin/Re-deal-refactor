package com.hifi.redeal.transaction.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hifi.redeal.databinding.FragmentTransactionByClientBinding
import com.hifi.redeal.transaction.adapter.TransactionAdapterDiffCallback
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TransactionByClientFragment : Fragment() {

    private lateinit var fragmentTransactionByClientBinding: FragmentTransactionByClientBinding
//    private lateinit var transactionAdapter: TransactionAdapter
//    private var clientIdx: Long? = null
//    private val transactionViewModel: TransactionViewModel by activityViewModels()
//    private val transactionClientViewModel: TransactionClientViewModel by activityViewModels()

    @Inject
    lateinit var transactionAdapterDiffCallback: TransactionAdapterDiffCallback


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentTransactionByClientBinding = FragmentTransactionByClientBinding.inflate(inflater)

//        setAdapter()
//        setBind()
//        setViewModel()

        return fragmentTransactionByClientBinding.root
    }


//    private fun setAdapter() {
//        val viewHolderFactories = HashMap<Int, ViewHolderFactory>()
//        viewHolderFactories[TransactionType.DEPOSIT.type] =
//            DepositHolderFactory(transactionViewModel)
//        viewHolderFactories[TransactionType.SALES.type] =
//            SalesHolderFactory(transactionViewModel)
//        viewHolderFactories[TransactionType.COUNT.type] =
//            CountHolderFactory(transactionViewModel, viewLifecycleOwner)
//
//        transactionAdapter =
//            TransactionAdapter(viewHolderFactories, transactionAdapterDiffCallback)
//    }
//
//    private fun setBind() {
//        fragmentTransactionByClientBinding.run {
//
//            transactionByClientRecyclerView.run {
//                adapter = transactionAdapter
//                layoutManager = LinearLayoutManager(context)
//            }
//
//            ImgBtnAddDepositByClient.setOnClickListener {
//                transactionClientViewModel.setSelectClientIndex(clientIdx)
//                findNavController().navigate(R.id.action_transactionByClientFragment_to_transactionDepositFragment)
//            }
//
//            ImgBtnAddTransactionByClient.setOnClickListener {
//                transactionClientViewModel.setSelectClientIndex(clientIdx)
//                findNavController().navigate(R.id.action_transactionByClientFragment_to_transactionSalesFragment)
//            }
//
//            toolbarTransactionByClientMain.setOnMenuItemClickListener {
//                findNavController().navigate(R.id.action_transactionByClientFragment_to_transactionsEditFragment)
//                true
//            }
//        }
//    }
//
//    private fun setViewModel() {
//        transactionViewModel.transactionBasicList.observe(viewLifecycleOwner) { transactions -> // 어댑터에 표시하는 거래내역들
//            transactionAdapter.submitList(transactions.sortedByDescending { it.getTransactionDate() }) {
//                transactionViewModel.postValueScrollPosition()
//            }
//            // 어댑터에 표시하는 거래내역들의 합계
//            val totalSalesCount =
//                transactions.count { it.getTransactionType() == TransactionType.SALES.type }
//            val totalSalesAmount = transactions.sumOf { it.calculateSalesAmount() }
//            val totalReceivables = transactions.sumOf { it.getReceivables() }
//
//            fragmentTransactionByClientBinding.run {
//                textTotalSalesCountByClient.text =
//                    TransactionNumberFormatUtil.replaceNumberFormat(totalSalesCount)
//                textTotalSalesByClient.text =
//                    TransactionNumberFormatUtil.replaceNumberFormat(totalSalesAmount)
//                textTotalReceivablesByClient.text =
//                    TransactionNumberFormatUtil.replaceNumberFormat(totalSalesAmount - totalReceivables)
//            }
//        }
//
//        transactionViewModel.transactionPosition.observe(viewLifecycleOwner) {// 수정 프래그먼트를 띄웠을 경우 해당 포지션으로 이동 시킴.
//            val layoutManager =
//                fragmentTransactionByClientBinding.transactionByClientRecyclerView.layoutManager as LinearLayoutManager
//            layoutManager.scrollToPosition(it)
//        }
//
//        transactionViewModel.modifyTransactionBasic.observe(viewLifecycleOwner) {// 수정하려는 거래내역이 선택되었을 때
//            it?.let {
//                transactionClientViewModel.setSelectClient(it.getClientInformation())
//                when (it.getTransactionType()) {
//                    TransactionType.SALES.type -> {
//                        findNavController().navigate(R.id.action_transactionByClientFragment_to_transactionSalesModifyFragment)
//                    }
//
//                    TransactionType.DEPOSIT.type -> {
//                        findNavController().navigate(R.id.action_transactionByClientFragment_to_transactionDepositModifyFragment)
//                    }
//                }
//            }
//        }
//        transactionViewModel.setSelectClientIndex(arguments?.getLong("clientIdx")) // 기존 선택한 클라이언트 정보를 초기화 시킨다.
//    }
}