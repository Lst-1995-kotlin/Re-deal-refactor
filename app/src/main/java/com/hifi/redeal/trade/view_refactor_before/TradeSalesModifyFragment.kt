package com.hifi.redeal.trade.view_refactor_before

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hifi.redeal.MainActivity
import com.hifi.redeal.R
import com.hifi.redeal.databinding.FragmentTradeSalesModifyBinding
import com.hifi.redeal.trade.ui.viewmodel.SalesTradeModifyViewModel

class TradeSalesModifyFragment : Fragment() {

    private lateinit var fragmentTradeSalesModifyBinding: FragmentTradeSalesModifyBinding
    private val salesTradeModifyViewModel: SalesTradeModifyViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentTradeSalesModifyBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_trade_sales_modify,
            container,
            false
        )
        fragmentTradeSalesModifyBinding.viewModel = salesTradeModifyViewModel
        fragmentTradeSalesModifyBinding.lifecycleOwner = viewLifecycleOwner

//        setBind()
//        setViewModel()
        return fragmentTradeSalesModifyBinding.root
    }


//    private fun setBind() {
//        fragmentTransactionSalesModifyBinding.run {
//            modifySalesFragmentToolbar.setNavigationOnClickListener {
//                findNavController().popBackStack()
//            }
//
//            modifySalesBtn.setOnClickListener {
//                transactionClientViewModel.selectedClient.value?.let {
//                    val itemPrice = removeNumberFormat("${transactionModifyItemPriceEditText.text}")
//                    val itemCount = removeNumberFormat("${transactionModifyItemCountEditText.text}")
//                    val amountReceived =
//                        removeNumberFormat("${transactionModifyAmountReceivedEditText.text}")
//
//                    transactionViewModel.updateModifySalesTransaction(
//                        it,
//                        transactionModifyItemNameEditText.text.toString(),
//                        itemCount,
//                        itemPrice,
//                        amountReceived
//                    )
//                    transactionViewModel.setModifyTransaction(null)
//                }
//            }
//            transactionModifyItemNameEditText.onFocusChangeListener =
//                TransactionInputEditTextFocusListener()
//            transactionModifyItemCountEditText.onFocusChangeListener =
//                TransactionInputEditTextFocusListener()
//            transactionModifyItemPriceEditText.onFocusChangeListener =
//                TransactionInputEditTextFocusListener()
//            transactionModifyAmountReceivedEditText.onFocusChangeListener =
//                TransactionInputEditTextFocusListener()
//
//            transactionModifyClientSelectEditText.onFocusChangeListener =
//                TransactionSelectEditTextFocusListener(
//                    SelectTransactionClientDialog(transactionClientViewModel),
//                    childFragmentManager
//                )
//
//            transactionModifyItemNameEditText.addTextChangedListener(
//                ItemNameTextWatcher(
//                    transactionClientViewModel,
//                    modifySalesBtn
//                )
//            )
//
//            transactionModifyItemCountEditText.addTextChangedListener(
//                ItemTextWatcher(
//                    transactionClientViewModel,
//                    transactionModifyItemCountEditText,
//                    transactionModifyItemPriceEditText,
//                    transactionModifyAmountReceivedEditText,
//                    modifySalesBtn
//                )
//            )
//
//            transactionModifyItemPriceEditText.addTextChangedListener(
//                ItemTextWatcher(
//                    transactionClientViewModel,
//                    transactionModifyItemPriceEditText,
//                    transactionModifyItemCountEditText,
//                    transactionModifyAmountReceivedEditText,
//                    modifySalesBtn
//                )
//            )
//
//            transactionModifyAmountReceivedEditText.addTextChangedListener(
//                AmountSalesTextWatcher(
//                    transactionClientViewModel,
//                    transactionModifyAmountReceivedEditText,
//                    transactionModifyItemPriceEditText,
//                    transactionModifyItemCountEditText,
//                    modifySalesBtn
//                )
//            )
//
//            modifySalesFragmentToolbar.setNavigationOnClickListener {
//                findNavController().popBackStack()
//            }
//
//            setTransactionAmountMessage(amountModifyMessageTextView)
//
//            mainActivity.hideKeyboardAndClearFocus(transactionModifyItemNameEditText)
//            mainActivity.hideKeyboardAndClearFocus(transactionModifyItemCountEditText)
//            mainActivity.hideKeyboardAndClearFocus(transactionModifyItemPriceEditText)
//            mainActivity.hideKeyboardAndClearFocus(transactionModifyAmountReceivedEditText)
//        }
//    }
//
//    private fun setViewModel() {
//        transactionViewModel.modifyTransactionBasic.observe(viewLifecycleOwner) { transaction ->
//            transaction?.let {
//                transaction.setModifyViewValue(
//                    fragmentTransactionSalesModifyBinding.transactionModifyItemNameEditText,
//                    fragmentTransactionSalesModifyBinding.transactionModifyItemCountEditText,
//                    fragmentTransactionSalesModifyBinding.transactionModifyItemPriceEditText,
//                    fragmentTransactionSalesModifyBinding.transactionModifyAmountReceivedEditText
//                )
//                transactionClientViewModel.setSelectClient(transaction.getClientInformation())
//            } ?: findNavController().popBackStack()
//        }
//        transactionClientViewModel.selectedClient.observe(viewLifecycleOwner) { client ->
//            client?.setClientInfoView(fragmentTransactionSalesModifyBinding.transactionModifyClientSelectEditText)
//            if (fragmentTransactionSalesModifyBinding.transactionModifyItemNameEditText.text.isNullOrEmpty() ||
//                fragmentTransactionSalesModifyBinding.transactionModifyItemCountEditText.text.isNullOrEmpty() ||
//                fragmentTransactionSalesModifyBinding.transactionModifyItemPriceEditText.text.isNullOrEmpty() ||
//                fragmentTransactionSalesModifyBinding.transactionModifyAmountReceivedEditText.text.isNullOrEmpty()
//            ) {
//                fragmentTransactionSalesModifyBinding.modifySalesBtn.visibility = View.GONE
//                return@observe
//            }
//            fragmentTransactionSalesModifyBinding.modifySalesBtn.visibility = View.VISIBLE
//        }
//    }
}