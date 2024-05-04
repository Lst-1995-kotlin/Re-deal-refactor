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
import com.hifi.redeal.trade.ui.viewmodel.DepositTradeModifyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TradeDepositModifyFragment : Fragment() {

    private lateinit var fragmentTradeDepositModifyBinding: FragmentTradeDepositModifyBinding
    private val depositTradeModifyViewModel: DepositTradeModifyViewModel by viewModels()

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
            //modifyDepositClientTextInputEditText.onFocusChangeListener =


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