package com.hifi.redeal.trade.view_refactor_before

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hifi.redeal.R
import com.hifi.redeal.databinding.FragmentTradeDepositBinding
import com.hifi.redeal.trade.domain.viewmodel.TradeAddViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TradeDepositFragment : Fragment() {

    private lateinit var fragmentTradeDepositBinding: FragmentTradeDepositBinding
    private val tradeAddViewModel : TradeAddViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        fragmentTradeDepositBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_trade_deposit, container, false)
//        setBind()
//        setViewModel()
        return fragmentTradeDepositBinding.root
    }

//    private fun setBind() {
//        fragmentTransactionDepositBinding.run {
//            addDepositBtn.setOnClickListener {
//                transactionClientViewModel.selectedClient.value?.let { client ->
//                    transactionViewModel.addDepositTransaction(
//                        client, removeNumberFormat("${addDepositPriceEditTextNumber.text}")
//                    )
//                    transactionViewModel.setMoveToPosition(0)
//                }
//                findNavController().popBackStack()
//            }
//
//            addDepositPriceEditTextNumber.onFocusChangeListener =
//                TransactionInputEditTextFocusListener()
//
//            addDepositPriceEditTextNumber.addTextChangedListener(
//                AmountTextWatcher(
//                    transactionClientViewModel,
//                    addDepositPriceEditTextNumber,
//                    addDepositBtn
//                )
//            )
//
//            selectDepositClientTextInputEditText.onFocusChangeListener =
//                TransactionSelectEditTextFocusListener(
//                    SelectTransactionClientDialog(transactionClientViewModel),
//                    childFragmentManager
//                )
//
//            addDepositMaterialToolbar.setNavigationOnClickListener {
//                transactionClientViewModel.setSelectClientIndex(null)
//                findNavController().popBackStack()
//            }
//
//            mainActivity.hideKeyboardAndClearFocus(addDepositPriceEditTextNumber)
//        }
//    }
//
//    private fun setViewModel() {
//        transactionClientViewModel.selectedClient.observe(viewLifecycleOwner) { client ->
//            client?.setClientInfoView(fragmentTransactionDepositBinding.selectDepositClientTextInputEditText)
//            if (fragmentTransactionDepositBinding.addDepositPriceEditTextNumber.text.isNullOrEmpty()) {
//                fragmentTransactionDepositBinding.addDepositBtn.visibility = View.GONE
//                return@observe
//            }
//            fragmentTransactionDepositBinding.addDepositBtn.visibility = View.VISIBLE
//        }
//    }
}
