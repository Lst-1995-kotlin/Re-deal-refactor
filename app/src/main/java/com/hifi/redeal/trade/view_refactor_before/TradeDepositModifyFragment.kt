package com.hifi.redeal.trade.view_refactor_before

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.hifi.redeal.R
import com.hifi.redeal.databinding.FragmentTradeDepositModifyBinding
import com.hifi.redeal.trade.ui.adapter.viewHolder.client.TradeClientHolderFactory
import com.hifi.redeal.trade.ui.dialog.SelectTradeClientDialog
import com.hifi.redeal.trade.ui.viewmodel.DepositTradeModifyViewModel
import com.hifi.redeal.trade.util.DialogShowingFocusListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TradeDepositModifyFragment : Fragment() {

    private lateinit var fragmentTradeDepositModifyBinding: FragmentTradeDepositModifyBinding
    private val depositTradeModifyViewModel: DepositTradeModifyViewModel by viewModels()

    @Inject
    lateinit var selectTradeClientDialog: SelectTradeClientDialog

    @Inject
    lateinit var tradeClientHolderFactory: TradeClientHolderFactory
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentTradeDepositModifyBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_trade_deposit_modify,
                container,
                false
            )
        fragmentTradeDepositModifyBinding.lifecycleOwner = viewLifecycleOwner
        fragmentTradeDepositModifyBinding.viewModel = depositTradeModifyViewModel
        setBind()
        setViewModel()

        return fragmentTradeDepositModifyBinding.root
    }


    private fun setBind() {
        fragmentTradeDepositModifyBinding.run {
            modifyDepositClientTextInputEditText.onFocusChangeListener =
                DialogShowingFocusListener(
                    selectTradeClientDialog,
                    childFragmentManager
                )

            // 거래처를 클릭하여 선택하였을 경우
            tradeClientHolderFactory.setOnClickListener {
                depositTradeModifyViewModel.setModifyClient(it.id)
                selectTradeClientDialog.dismiss()
            }


//            modifyDepositPriceEditTextNumber.addTextChangedListener(
//
//            )

            modifyDepositBtn.setOnClickListener {

            }

            modifyDepositMaterialToolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun setViewModel() {
        arguments?.let { depositTradeModifyViewModel.setModifyTradeId(it.getInt("tradeId")) }
    }
}