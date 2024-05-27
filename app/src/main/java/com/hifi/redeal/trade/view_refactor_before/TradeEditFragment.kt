package com.hifi.redeal.trade.view_refactor_before

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
import com.hifi.redeal.databinding.FragmentTradeEditBinding
import com.hifi.redeal.trade.configuration.TradeType
import com.hifi.redeal.trade.ui.adapter.TradeSelectAdapter
import com.hifi.redeal.trade.ui.adapter.TradeSelectAdapterDiffCallback
import com.hifi.redeal.trade.ui.adapter.viewHolder.ViewHolderFactory
import com.hifi.redeal.trade.ui.adapter.viewHolder.tradeEdit.DepositSelectHolderFactory
import com.hifi.redeal.trade.ui.adapter.viewHolder.tradeEdit.SalesSelectHolderFactory
import com.hifi.redeal.trade.ui.viewmodel.TradeSelectViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TradeEditFragment : Fragment() {

    private lateinit var fragmentTradeEditBinding: FragmentTradeEditBinding
    private val tradeSelectViewModel: TradeSelectViewModel by viewModels()
    private lateinit var tradeSelectAdapter: TradeSelectAdapter

    @Inject
    lateinit var tradeSelectAdapterDiffCallback: TradeSelectAdapterDiffCallback

    @Inject
    lateinit var depositSelectHolderFactory: DepositSelectHolderFactory

    @Inject
    lateinit var salesSelectHolderFactory: SalesSelectHolderFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tradeSelectViewModel.setClientId(null)
        tradeSelectViewModel.selectHistoryClear()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentTradeEditBinding = FragmentTradeEditBinding.inflate(inflater)
        fragmentTradeEditBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_trade_edit, container, false)
        fragmentTradeEditBinding.lifecycleOwner = viewLifecycleOwner
        fragmentTradeEditBinding.viewModel = tradeSelectViewModel
        setAdapter()
        setBind()
        setViewModel()
        return fragmentTradeEditBinding.root
    }

    private fun setAdapter() {
        val viewHolderFactories = HashMap<Int, ViewHolderFactory>()
        depositSelectHolderFactory.setOnClickListener {
            val updateData = it.copy(
                checked = !it.checked
            )
            tradeSelectViewModel.updateSelectTradeData(updateData)
        }
        viewHolderFactories[TradeType.DEPOSIT.type] = depositSelectHolderFactory

        salesSelectHolderFactory.setOnClickListener {
            val updateData = it.copy(
                checked = !it.checked
            )
            tradeSelectViewModel.updateSelectTradeData(updateData)
        }
        viewHolderFactories[TradeType.SALES.type] = salesSelectHolderFactory

        tradeSelectAdapter =
            TradeSelectAdapter(viewHolderFactories, tradeSelectAdapterDiffCallback)
    }

    private fun setBind() {
        fragmentTradeEditBinding.run {
            toolbarTransactionEdit.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            transactionEditSelectRecyclerView.run {
                adapter = tradeSelectAdapter
                layoutManager = LinearLayoutManager(context)
            }

            transactionEditCancelButton.setOnClickListener {
                findNavController().popBackStack()
            }

            selectTransactionDeleteButton.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun setViewModel() {
        tradeSelectViewModel.trades.observe(viewLifecycleOwner) { trades -> // 어댑터에 표시하는 거래내역들
            tradeSelectAdapter.submitList(trades)
        }
        fragmentTradeEditBinding.transactionEditSelectRecyclerView.scrollToPosition(0)
    }

}