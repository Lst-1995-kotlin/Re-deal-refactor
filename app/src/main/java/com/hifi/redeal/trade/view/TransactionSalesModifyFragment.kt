package com.hifi.redeal.trade.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hifi.redeal.MainActivity
import com.hifi.redeal.databinding.FragmentTransactionSalesModifyBinding

class TransactionSalesModifyFragment : Fragment() {

    private lateinit var fragmentTransactionSalesModifyBinding: FragmentTransactionSalesModifyBinding
    private lateinit var mainActivity: MainActivity

    //private val transactionClientViewModel: TransactionClientViewModel by activityViewModels()
    //private val transactionViewModel: TransactionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentTransactionSalesModifyBinding =
            FragmentTransactionSalesModifyBinding.inflate(inflater)
        mainActivity = activity as MainActivity
//        setBind()
//        setViewModel()
        return fragmentTransactionSalesModifyBinding.root
    }

    override fun onPause() {
        super.onPause()
//        transactionViewModel.modifyTransactionBasic.value?.let {
//            transactionViewModel.setModifyTransaction(null)
//        }
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