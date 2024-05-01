package com.hifi.redeal.trade.ui.fragment

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
import com.hifi.redeal.trade.ui.adapter.TradeAdapter
import com.hifi.redeal.trade.ui.adapter.TradeAdapterDiffCallback
import com.hifi.redeal.trade.ui.adapter.viewHolder.ViewHolderFactory
import com.hifi.redeal.trade.ui.adapter.viewHolder.trade.CountHolderFactory
import com.hifi.redeal.trade.ui.adapter.viewHolder.trade.DepositHolderFactory
import com.hifi.redeal.trade.ui.adapter.viewHolder.trade.SalesHolderFactory
import com.hifi.redeal.trade.configuration.TradeType
import com.hifi.redeal.trade.ui.dialog.TradeAddSelectDialog
import com.hifi.redeal.trade.ui.viewmodel.TradeViewModel
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
        fragmentTradeBinding.lifecycleOwner = viewLifecycleOwner
        fragmentTradeBinding.viewModel = tradeViewModel

        setDialog(inflater)
        setAdapter()
        setBind()
        setViewModel()

        return fragmentTradeBinding.root
    }

    private fun setDialog(inflater: LayoutInflater) {
        tradeAddSelectDialog = TradeAddSelectDialog(inflater,
            { findNavController().navigate(R.id.action_tradeFragment_to_tradeDepositFragment) },
            { findNavController().navigate(R.id.action_tradeFragment_to_tradeSalesFragment) }
        )
    }

    private fun setAdapter() {
        val viewHolderFactories = HashMap<Int, ViewHolderFactory>()

        depositHolderFactory.setOnDeleteClickListener { tradeViewModel.deleteTrade(it) }
        depositHolderFactory.setOnEditClickListener {
            findNavController().navigate(
                R.id.action_tradeFragment_to_tradeDepositModifyFragment,
                Bundle().apply {
                    putInt("tradeId", it.id)
                }
            )
        }

        salesHolderFactory.setOnDeleteClickListener { tradeViewModel.deleteTrade(it) }
        salesHolderFactory.setOnEditClickListener {
            findNavController().navigate(
                R.id.action_tradeFragment_to_tradeSalesModifyFragment,
                Bundle().apply {
                    putInt("tradeId", it.id)
                }
            )
        }

        viewHolderFactories[TradeType.DEPOSIT.type] = depositHolderFactory
        viewHolderFactories[TradeType.SALES.type] = salesHolderFactory
        viewHolderFactories[TradeType.COUNT.type] = countHolderFactory

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
            findNavController().navigate(R.id.action_tradeFragment_to_tradeEditFragment)
            true
        }
    }

    private fun setViewModel() {
        tradeViewModel.trades.observe(viewLifecycleOwner) { trades -> // 어댑터에 표시하는 거래내역들
            tradeAdapter.submitList(trades) {
                fragmentTradeBinding.transactionRecyclerView.scrollToPosition(0)
                tradeAdapter.updateCount()
            }
        }
    }
}
