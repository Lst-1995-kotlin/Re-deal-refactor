package com.hifi.redeal.trade.view_refactor_before

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hifi.redeal.R
import com.hifi.redeal.databinding.FragmentTradeDepositModifyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TradeDepositModifyFragment : Fragment() {

    private lateinit var fragmentTradeDepositModifyBinding: FragmentTradeDepositModifyBinding


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
        //fragmentTradeDepositModifyBinding.viewModel = tradeViewModel
//        setBind()
//        setViewModel()

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

    }
}