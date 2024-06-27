package com.hifi.redeal.trade.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hifi.redeal.R
import com.hifi.redeal.databinding.FragmentTradeEditBinding
import com.hifi.redeal.trade.configuration.TradeType
import com.hifi.redeal.trade.data.model.toCheckChange
import com.hifi.redeal.trade.ui.adapter.TradeSelectAdapter
import com.hifi.redeal.trade.ui.adapter.TradeSelectAdapterDiffCallback
import com.hifi.redeal.trade.ui.adapter.viewHolder.ViewHolderFactory
import com.hifi.redeal.trade.ui.adapter.viewHolder.tradeEdit.DepositSelectHolderFactory
import com.hifi.redeal.trade.ui.adapter.viewHolder.tradeEdit.SalesSelectHolderFactory
import com.hifi.redeal.trade.ui.viewmodel.TradeSelectViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
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
        tradeSelectViewModel.setClientId(arguments?.getInt("clientId"))
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
            tradeSelectViewModel.updateSelectTradeData(it.toCheckChange())
        }
        viewHolderFactories[TradeType.DEPOSIT.type] = depositSelectHolderFactory

        salesSelectHolderFactory.setOnClickListener {
            tradeSelectViewModel.updateSelectTradeData(it.toCheckChange())
        }
        viewHolderFactories[TradeType.SALES.type] = salesSelectHolderFactory

        tradeSelectAdapter =
            TradeSelectAdapter(viewHolderFactories, tradeSelectAdapterDiffCallback)
    }

    private fun setBind() {
        fragmentTradeEditBinding.run {
            toolbarTradeEdit.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            tradeEditSelectRecyclerView.run {
                adapter = tradeSelectAdapter
                layoutManager = LinearLayoutManager(context)
            }

            tradeEditCancelButton.setOnClickListener {
                findNavController().popBackStack()
            }

            selectTradeDeleteButton.setOnClickListener {
                lifecycleScope.launch {
                    async {
                        tradeSelectViewModel.selectTradeDelete()
                    }.await()
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun setViewModel() {

        tradeSelectViewModel.trades.observe(viewLifecycleOwner) { trades -> // 어댑터에 표시하는 거래내역들
            tradeSelectAdapter.submitList(trades)
        }
        fragmentTradeEditBinding.tradeEditSelectRecyclerView.scrollToPosition(0)
    }

}