package com.hifi.redeal.transaction.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hifi.redeal.R
import com.hifi.redeal.databinding.FragmentTradeBinding
import com.hifi.redeal.transaction.adapter.TradeAdapter
import com.hifi.redeal.transaction.adapter.TradeAdapterDiffCallback
import com.hifi.redeal.transaction.adapter.viewHolder.ViewHolderFactory
import com.hifi.redeal.transaction.adapter.viewHolder.transaction.CountHolderFactory
import com.hifi.redeal.transaction.adapter.viewHolder.transaction.DepositHolderFactory
import com.hifi.redeal.transaction.adapter.viewHolder.transaction.SalesHolderFactory
import com.hifi.redeal.transaction.configuration.TransactionType
import com.hifi.redeal.transaction.util.TransactionNumberFormatUtil.replaceNumberFormat
import com.hifi.redeal.transaction.view.dialog.TransactionAddSelectDialog
import com.hifi.redeal.transaction.viewmodel.TradeViewModel
import com.hifi.redeal.transaction.viewmodel.TransactionClientViewModel
import com.hifi.redeal.transaction.viewmodel.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TradeFragment : Fragment() {
    private lateinit var fragmentTradeBinding: FragmentTradeBinding
    private val transactionViewModel: TransactionViewModel by activityViewModels()
    private val tradeViewModel: TradeViewModel by viewModels()
    private val transactionClientViewModel: TransactionClientViewModel by activityViewModels()
    private lateinit var tradeAdapter: TradeAdapter
    private lateinit var transactionAddSelectDialog: TransactionAddSelectDialog

    @Inject
    lateinit var tradeAdapterDiffCallback: TradeAdapterDiffCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        fragmentTradeBinding = FragmentTradeBinding.inflate(inflater)
        transactionAddSelectDialog =
            TransactionAddSelectDialog(findNavController(), inflater)

        setAdapter()
        setBind()
        setViewModel()

        return fragmentTradeBinding.root
    }

    private fun setAdapter() {
        val viewHolderFactories = HashMap<Int, ViewHolderFactory>()
        viewHolderFactories[TransactionType.DEPOSIT.type] = DepositHolderFactory(tradeViewModel)
        viewHolderFactories[TransactionType.SALES.type] = SalesHolderFactory(tradeViewModel)
        viewHolderFactories[TransactionType.COUNT.type] = CountHolderFactory(tradeViewModel, viewLifecycleOwner)

        tradeAdapter =
            TradeAdapter(viewHolderFactories, tradeAdapterDiffCallback)
    }

    private fun setBind() {
        fragmentTradeBinding.transactionRecyclerView.run {
            adapter = tradeAdapter
            layoutManager = LinearLayoutManager(context)
        }

        fragmentTradeBinding.transactionAddButton.setOnClickListener {
            transactionAddSelectDialog.dialogShow()
        }

        fragmentTradeBinding.toolbarTransactionMain.setOnMenuItemClickListener {
            findNavController().navigate(R.id.action_tradeFragment_to_transactionsEditFragment)
            true
        }
    }

    private fun setViewModel() {

        tradeViewModel.trades.observe(viewLifecycleOwner) {
            fragmentTradeBinding.textTotalSales.text = "${it.size}"
        }

        tradeViewModel.trades.observe(viewLifecycleOwner) { trades -> // 어댑터에 표시하는 거래내역들
            tradeAdapter.submitList(trades)

            // 어댑터에 표시하는 거래내역들의 합계
            val totalSalesCount = trades.count { !it.type } // 매출 건 수
            val totalSalesAmount = trades.sumOf { it.itemCount * it.itemPrice } // 총 판매 금액
            val totalReceivables = totalSalesAmount - trades.sumOf { it.receivedAmount }// 발생 미수금

            fragmentTradeBinding.run {
                textTotalSalesCount.text = replaceNumberFormat(totalSalesCount)
                textTotalSales.text = replaceNumberFormat(totalSalesAmount)
                textTotalReceivables.text = replaceNumberFormat(totalReceivables)
            }
        }

        transactionViewModel.transactionPosition.observe(viewLifecycleOwner) {// 수정 프래그먼트를 띄웠을 경우 해당 포지션으로 이동 시킴.
            val layoutManager =
                fragmentTradeBinding.transactionRecyclerView.layoutManager as LinearLayoutManager
            layoutManager.scrollToPosition(it)
        }

        transactionViewModel.modifyTransactionBasic.observe(viewLifecycleOwner) {// 수정하려는 거래내역이 선택되었을 때
            it?.let {
                transactionClientViewModel.setSelectClient(it.getClientInformation())
                when (it.getTransactionType()) {
                    TransactionType.SALES.type -> {
                        findNavController().navigate(R.id.action_tradeFragment_to_transactionSalesModifyFragment)
                    }

                    TransactionType.DEPOSIT.type -> {
                        findNavController().navigate(R.id.action_tradeFragment_to_transactionDepositModifyFragment)
                    }
                }
            } ?: transactionClientViewModel.setSelectClientIndex(null)
        }

        transactionViewModel.setSelectClientIndex(null) // 기존 선택한 클라이언트 정보를 초기화 시킨다.
    }
}
