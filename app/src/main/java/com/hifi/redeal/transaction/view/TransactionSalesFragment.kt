package com.hifi.redeal.transaction.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.hifi.redeal.MainActivity
import com.hifi.redeal.databinding.FragmentTransactionSalesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionSalesFragment : Fragment() {
    private lateinit var mainActivity: MainActivity
    private lateinit var fragmentTransactionSalesBinding: FragmentTransactionSalesBinding

    //private val transactionClientViewModel: TransactionClientViewModel by activityViewModels()
    //private val transactionViewModel: TransactionViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        fragmentTransactionSalesBinding = FragmentTransactionSalesBinding.inflate(inflater)
        mainActivity = activity as MainActivity
//        setViewModel()
//        setBind()

        return fragmentTransactionSalesBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //transactionClientViewModel.setSelectClientIndex(null)
    }

//    private fun setBind() {
//        fragmentTransactionSalesBinding.run {
//            addSalesMaterialToolbar.setNavigationOnClickListener {
//                findNavController().popBackStack()
//            }
//
//            addSalesBtn.setOnClickListener {
//                val itemPrice = removeNumberFormat("${transactionItemPriceEditText.text}")
//                val itemCount = removeNumberFormat("${transactionItemCountEditText.text}")
//                val amountReceived = removeNumberFormat("${transactionAmountReceivedEditText.text}")
//
//                transactionClientViewModel.selectedClient.value?.let { client ->
//                    transactionViewModel.addSalesTransaction(
//                        client,
//                        "${transactionItemNameEditText.text}",
//                        itemCount,
//                        itemPrice,
//                        amountReceived
//                    )
//                    transactionViewModel.setMoveToPosition(0)
//                    findNavController().popBackStack()
//                }
//            }
//
//            transactionItemNameEditText.onFocusChangeListener =
//                TransactionInputEditTextFocusListener()
//            transactionItemCountEditText.onFocusChangeListener =
//                TransactionInputEditTextFocusListener()
//            transactionItemPriceEditText.onFocusChangeListener =
//                TransactionInputEditTextFocusListener()
//            transactionAmountReceivedEditText.onFocusChangeListener =
//                TransactionInputEditTextFocusListener()
//
//            transactionClientSelectEditText.onFocusChangeListener =
//                TransactionSelectEditTextFocusListener(
//                    SelectTransactionClientDialog(transactionClientViewModel),
//                    childFragmentManager
//                )
//
//            transactionItemNameEditText.addTextChangedListener(
//                ItemNameTextWatcher(
//                    transactionClientViewModel,
//                    addSalesBtn
//                )
//            )
//
//            transactionItemCountEditText.addTextChangedListener(
//                ItemTextWatcher(
//                    transactionClientViewModel,
//                    transactionItemCountEditText,
//                    transactionItemPriceEditText,
//                    transactionAmountReceivedEditText,
//                    addSalesBtn
//                )
//            )
//
//            transactionItemPriceEditText.addTextChangedListener(
//                ItemTextWatcher(
//                    transactionClientViewModel,
//                    transactionItemPriceEditText,
//                    transactionItemCountEditText,
//                    transactionAmountReceivedEditText,
//                    addSalesBtn
//                )
//            )
//
//            transactionAmountReceivedEditText.addTextChangedListener(
//                AmountSalesTextWatcher(
//                    transactionClientViewModel,
//                    transactionAmountReceivedEditText,
//                    transactionItemPriceEditText,
//                    transactionItemCountEditText,
//                    addSalesBtn
//                )
//            )
//
//            setTransactionAmountMessage(amountMessageTextView)
//
//            mainActivity.hideKeyboardAndClearFocus(transactionItemNameEditText)
//            mainActivity.hideKeyboardAndClearFocus(transactionItemCountEditText)
//            mainActivity.hideKeyboardAndClearFocus(transactionItemPriceEditText)
//            mainActivity.hideKeyboardAndClearFocus(transactionAmountReceivedEditText)
//        }
//    }
//
//    private fun setViewModel() {
//        transactionClientViewModel.selectedClient.observe(viewLifecycleOwner) { client ->
//            client?.setClientInfoView(fragmentTransactionSalesBinding.transactionClientSelectEditText)
//            if (fragmentTransactionSalesBinding.transactionItemNameEditText.text.isNullOrEmpty() ||
//                fragmentTransactionSalesBinding.transactionItemCountEditText.text.isNullOrEmpty() ||
//                fragmentTransactionSalesBinding.transactionItemPriceEditText.text.isNullOrEmpty() ||
//                fragmentTransactionSalesBinding.transactionAmountReceivedEditText.text.isNullOrEmpty()
//            ) {
//                fragmentTransactionSalesBinding.addSalesBtn.visibility = View.GONE
//                return@observe
//            }
//            fragmentTransactionSalesBinding.addSalesBtn.visibility = View.VISIBLE
//        }
//    }
}
