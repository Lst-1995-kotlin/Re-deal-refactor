package com.hifi.redeal.transaction.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hifi.redeal.R
import com.hifi.redeal.databinding.FragmentTradeBinding
import com.hifi.redeal.transaction.adapter.TradeAdapter
import com.hifi.redeal.transaction.adapter.TradeAdapterDiffCallback
import com.hifi.redeal.transaction.adapter.viewHolder.ViewHolderFactory
import com.hifi.redeal.transaction.adapter.viewHolder.trade.CountHolderFactory
import com.hifi.redeal.transaction.adapter.viewHolder.trade.DepositHolderFactory
import com.hifi.redeal.transaction.adapter.viewHolder.trade.SalesHolderFactory
import com.hifi.redeal.transaction.configuration.TransactionType
import com.hifi.redeal.transaction.view.dialog.TradeAddSelectDialog
import com.hifi.redeal.transaction.viewmodel.TradeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TradeFragment : Fragment() {
    private lateinit var fragmentTradeBinding: FragmentTradeBinding
    private val tradeViewModel: TradeViewModel by viewModels()
    private lateinit var tradeAdapter: TradeAdapter
    private lateinit var tradeAddSelectDialog: TradeAddSelectDialog

    @Inject
    lateinit var tradeAdapterDiffCallback: TradeAdapterDiffCallback

    @Inject
    lateinit var countHolderFactory: CountHolderFactory

    @Inject
    lateinit var depositHolderFactory: DepositHolderFactory

    @Inject
    lateinit var salesHolderFactory: SalesHolderFactory


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        fragmentTradeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_trade, container, false)
        tradeAddSelectDialog = TradeAddSelectDialog(findNavController(), inflater)
        fragmentTradeBinding.lifecycleOwner = viewLifecycleOwner
        fragmentTradeBinding.viewModel = tradeViewModel
        setAdapter()
        setBind()
        setViewModel()

        return fragmentTradeBinding.root
    }

    private fun setAdapter() {
        val viewHolderFactories = HashMap<Int, ViewHolderFactory>()

        depositHolderFactory.setOnDeleteClickListener { tradeViewModel.deleteTrade(it) }
        depositHolderFactory.setOnEditClickListener {
            findNavController().navigate(
                R.id.action_tradeFragment_to_transactionDepositModifyFragment,
                Bundle().apply {
                    putInt("tradeId", it.id)
                }
            )
        }

        salesHolderFactory.setOnDeleteClickListener { tradeViewModel.deleteTrade(it) }
        salesHolderFactory.setOnEditClickListener {
            findNavController().navigate(
                R.id.action_tradeFragment_to_transactionSalesModifyFragment,
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
        fragmentTradeBinding.transactionRecyclerView.run {
            adapter = tradeAdapter
            layoutManager = LinearLayoutManager(context)
        }

        fragmentTradeBinding.transactionAddButton.setOnClickListener {
            tradeAddSelectDialog.dialogShow()
        }

        fragmentTradeBinding.toolbarTransactionMain.setOnMenuItemClickListener {
            findNavController().navigate(R.id.action_tradeFragment_to_transactionsEditFragment)
            true
        }
    }

    private fun setViewModel() {
        tradeViewModel.trades.observe(viewLifecycleOwner) { trades -> // 어댑터에 표시하는 거래내역들
            tradeAdapter.submitList(trades) {
                tradeAdapter.updateCount()
            }
        }
    }
}
