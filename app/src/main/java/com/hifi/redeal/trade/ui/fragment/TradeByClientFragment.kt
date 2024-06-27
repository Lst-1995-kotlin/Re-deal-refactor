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
import com.hifi.redeal.databinding.FragmentTradeByClientBinding
import com.hifi.redeal.trade.configuration.TradeType
import com.hifi.redeal.trade.ui.adapter.TradeAdapter
import com.hifi.redeal.trade.ui.adapter.TradeAdapterDiffCallback
import com.hifi.redeal.trade.ui.adapter.viewHolder.ViewHolderFactory
import com.hifi.redeal.trade.ui.adapter.viewHolder.trade.CountHolderFactory
import com.hifi.redeal.trade.ui.adapter.viewHolder.trade.DepositHolderFactory
import com.hifi.redeal.trade.ui.adapter.viewHolder.trade.SalesHolderFactory
import com.hifi.redeal.trade.ui.viewmodel.TradeViewModel
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tradeViewModel.setClientId(arguments?.getInt("clientId"))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentTradeByClientBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_trade_by_client, container, false)
        fragmentTradeByClientBinding.lifecycleOwner = viewLifecycleOwner
        fragmentTradeByClientBinding.viewModel = tradeViewModel

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
                R.id.action_tradeByClientFragment_to_tradeDepositModifyFragment,
                Bundle().apply {
                    putInt("tradeId", it.id)
                }
            )
        }

        salesHolderFactory.setOnDeleteClickListener { tradeViewModel.deleteTrade(it) }
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

            tradeByClientRecyclerView.run {
                adapter = tradeAdapter
                layoutManager = LinearLayoutManager(context)
            }

            ImgBtnAddDepositByClient.setOnClickListener {
                val bundle = Bundle()
                arguments?.getInt("clientId")?.let { bundle.putInt("clientId", it) }
                findNavController().navigate(
                    R.id.action_tradeByClientFragment_to_tradeDepositFragment,
                    bundle
                )
            }

            ImgBtnAddTradeByClient.setOnClickListener {
                val bundle = Bundle()
                arguments?.getInt("clientId")?.let { bundle.putInt("clientId", it) }
                findNavController().navigate(
                    R.id.action_tradeByClientFragment_to_tradeSalesFragment,
                    bundle
                )
            }

            toolbarTradeByClientMain.setOnMenuItemClickListener {
                val bundle = Bundle()
                arguments?.getInt("clientId")?.let { bundle.putInt("clientId", it) }
                findNavController().navigate(
                    R.id.action_tradeByClientFragment_to_tradeEditFragment,
                    bundle
                )
                true
            }
        }
    }

    private fun setViewModel() {
        // 코루틴을 이용하여 불러올 클라이언트 ID 설정 후 거래내역들 표시
        tradeViewModel.trades.observe(viewLifecycleOwner) { trades -> // 어댑터에 표시하는 거래내역들
            tradeAdapter.submitList(trades) {
                fragmentTradeByClientBinding.tradeByClientRecyclerView.scrollToPosition(0)
                tradeAdapter.updateCount()
            }
        }
    }
}