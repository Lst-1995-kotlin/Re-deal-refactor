package com.hifi.redeal.transaction.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.snapshots.Snapshot.Companion.observe
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hifi.redeal.R
import com.hifi.redeal.databinding.FragmentTradeByClientBinding
import com.hifi.redeal.transaction.adapter.TradeAdapter
import com.hifi.redeal.transaction.adapter.TradeAdapterDiffCallback
import com.hifi.redeal.transaction.adapter.viewHolder.ViewHolderFactory
import com.hifi.redeal.transaction.adapter.viewHolder.trade.CountHolderFactory
import com.hifi.redeal.transaction.adapter.viewHolder.trade.DepositHolderFactory
import com.hifi.redeal.transaction.adapter.viewHolder.trade.SalesHolderFactory
import com.hifi.redeal.transaction.configuration.TransactionType
import com.hifi.redeal.transaction.util.TransactionNumberFormatUtil.replaceNumberFormat
import com.hifi.redeal.transaction.viewmodel.TradeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TradeByClientFragment : Fragment() {

    private lateinit var fragmentTradeByClientBinding: FragmentTradeByClientBinding
    private val tradeViewModel: TradeViewModel by viewModels()
    private lateinit var tradeAdapter: TradeAdapter

    @Inject
    lateinit var tradeAdapterDiffCallback: TradeAdapterDiffCallback

    @Inject
    lateinit var countHolderFactory: CountHolderFactory

    @Inject
    lateinit var depositHolderFactory: DepositHolderFactory

    @Inject
    lateinit var salesHolderFactory: SalesHolderFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentTradeByClientBinding = FragmentTradeByClientBinding.inflate(inflater)
        setAdapter()
        setBind()
        setViewModel()
        return fragmentTradeByClientBinding.root
    }

    private fun setAdapter() {
        val viewHolderFactories = HashMap<Int, ViewHolderFactory>()

        depositHolderFactory.setOnDeleteClickListener { tradeViewModel.deleteTrade(it) }
        depositHolderFactory.setOnEditClickListener {
            findNavController().navigate(
                R.id.action_transactionByClientFragment_to_transactionDepositModifyFragment,
                Bundle().apply {
                    putInt("tradeId", it.id)
                }
            )
        }

        salesHolderFactory.setOnDeleteClickListener { tradeViewModel.deleteTrade(it) }
        salesHolderFactory.setOnEditClickListener {
            findNavController().navigate(
                R.id.action_transactionByClientFragment_to_transactionSalesModifyFragment,
                Bundle().apply {
                    putInt("tradeId", it.id)
                }
            )
        }

        viewHolderFactories[TransactionType.DEPOSIT.type] = depositHolderFactory
        viewHolderFactories[TransactionType.SALES.type] = salesHolderFactory
        viewHolderFactories[TransactionType.COUNT.type] = countHolderFactory

        tradeAdapter = TradeAdapter(viewHolderFactories, tradeAdapterDiffCallback)
    }

    private fun setBind() {
        fragmentTradeByClientBinding.run {

            transactionByClientRecyclerView.run {
                adapter = tradeAdapter
                layoutManager = LinearLayoutManager(context)
            }

            ImgBtnAddDepositByClient.setOnClickListener {
                findNavController().navigate(R.id.action_transactionByClientFragment_to_transactionDepositFragment)
            }

            ImgBtnAddTransactionByClient.setOnClickListener {
                findNavController().navigate(R.id.action_transactionByClientFragment_to_transactionSalesFragment)
            }

            toolbarTransactionByClientMain.setOnMenuItemClickListener {
                findNavController().navigate(R.id.action_transactionByClientFragment_to_transactionsEditFragment)
                true
            }
        }
    }

    private fun setViewModel() {
        tradeViewModel.trades.observe(viewLifecycleOwner) { trades -> // 어댑터에 표시하는 거래내역들
            tradeAdapter.submitList(trades) {
                tradeAdapter.updateCount()
            }
            // 어댑터에 표시하는 거래내역들의 합계
            val totalSalesCount = trades.count { !it.type } // 매출 건 수
            val totalSalesAmount = trades.sumOf { it.itemCount * it.itemPrice } // 총 판매 금액
            val totalReceivables =
                totalSalesAmount - trades.sumOf { it.receivedAmount }// 발생 미수금

            fragmentTradeByClientBinding.run {
                textTotalSalesCountByClient.text = replaceNumberFormat(totalSalesCount)
                textTotalSalesByClient.text = replaceNumberFormat(totalSalesAmount)
                textTotalReceivablesByClient.text =
                    replaceNumberFormat(totalSalesAmount - totalReceivables)
            }
        }
        tradeViewModel.setClientId(arguments?.getInt("clientId")!!)
    }
}