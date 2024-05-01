package com.hifi.redeal.trade.view_refactor_before

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hifi.redeal.R
import com.hifi.redeal.databinding.FragmentTradeByClientBinding
import com.hifi.redeal.trade.configuration.TradeType
import com.hifi.redeal.trade.domain.viewmodel.TradeByClientViewModel
import com.hifi.redeal.trade.ui.adapter.TradeAdapter
import com.hifi.redeal.trade.ui.adapter.TradeAdapterDiffCallback
import com.hifi.redeal.trade.ui.adapter.viewHolder.ViewHolderFactory
import com.hifi.redeal.trade.ui.adapter.viewHolder.trade.CountHolderFactory
import com.hifi.redeal.trade.ui.adapter.viewHolder.trade.DepositHolderFactory
import com.hifi.redeal.trade.ui.adapter.viewHolder.trade.SalesHolderFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TradeByClientFragment : Fragment() {

    private lateinit var fragmentTradeByClientBinding: FragmentTradeByClientBinding
    private val tradeByClientViewModel: TradeByClientViewModel by viewModels()
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
        fragmentTradeByClientBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_trade_by_client, container, false)
        fragmentTradeByClientBinding.lifecycleOwner = viewLifecycleOwner
        fragmentTradeByClientBinding.viewModel = tradeByClientViewModel

        setAdapter()
        setBind()
        setViewModel()
        return fragmentTradeByClientBinding.root
    }

    private fun setAdapter() {
        val viewHolderFactories = HashMap<Int, ViewHolderFactory>()

        depositHolderFactory.setOnDeleteClickListener { tradeByClientViewModel.deleteTrade(it) }
        depositHolderFactory.setOnEditClickListener {
            findNavController().navigate(
                R.id.action_tradeByClientFragment_to_tradeDepositModifyFragment,
                Bundle().apply {
                    putInt("tradeId", it.id)
                }
            )
        }

        salesHolderFactory.setOnDeleteClickListener { tradeByClientViewModel.deleteTrade(it) }
        salesHolderFactory.setOnEditClickListener {
            findNavController().navigate(
                R.id.action_tradeByClientFragment_to_tradeSalesModifyFragment,
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
        fragmentTradeByClientBinding.run {

            transactionByClientRecyclerView.run {
                adapter = tradeAdapter
                layoutManager = LinearLayoutManager(context)
            }

            ImgBtnAddDepositByClient.setOnClickListener {
                findNavController().navigate(
                    R.id.action_tradeByClientFragment_to_tradeDepositFragment,
                    Bundle().apply {
                        arguments?.getInt("clientId")
                    })
            }

            ImgBtnAddTransactionByClient.setOnClickListener {
                findNavController().navigate(
                    R.id.action_tradeByClientFragment_to_tradeSalesFragment,
                    Bundle().apply {
                        arguments?.getInt("clientId")
                    })
            }

            toolbarTransactionByClientMain.setOnMenuItemClickListener {
                findNavController().navigate(
                    R.id.action_tradeByClientFragment_to_tradeEditFragment,
                    Bundle().apply {
                        arguments?.getInt("clientId")
                    })
                true
            }
        }
    }

    private fun setViewModel() {

        CoroutineScope(Dispatchers.Main).launch{
            async {
                tradeByClientViewModel.setClientId(arguments?.getInt("clientId"))
            }.await()

            tradeByClientViewModel.trades.observe(viewLifecycleOwner) { trades -> // 어댑터에 표시하는 거래내역들
                tradeAdapter.submitList(trades) {
                    fragmentTradeByClientBinding.transactionByClientRecyclerView.scrollToPosition(0)
                    tradeAdapter.updateCount()
                }
            }

        }

    }
}