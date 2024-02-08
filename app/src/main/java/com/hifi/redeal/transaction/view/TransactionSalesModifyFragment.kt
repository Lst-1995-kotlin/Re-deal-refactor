package com.hifi.redeal.transaction.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.hifi.redeal.MainActivity
import com.hifi.redeal.databinding.FragmentTransactionSalesModifyBinding
import com.hifi.redeal.transaction.configuration.TransactionAmountConfiguration.Companion.setTransactionAmountMessage
import com.hifi.redeal.transaction.util.AmountTextWatcher
import com.hifi.redeal.transaction.util.ItemNameTextWatcher
import com.hifi.redeal.transaction.util.ItemTextWatcher
import com.hifi.redeal.transaction.util.TransactionInputEditTextFocusListener
import com.hifi.redeal.transaction.util.TransactionNumberFormatUtil.removeNumberFormat
import com.hifi.redeal.transaction.util.TransactionSelectEditTextFocusListener
import com.hifi.redeal.transaction.viewmodel.TransactionClientViewModel
import com.hifi.redeal.transaction.viewmodel.TransactionViewModel

class TransactionSalesModifyFragment : Fragment() {

    private lateinit var fragmentTransactionSalesModifyBinding: FragmentTransactionSalesModifyBinding
    private lateinit var mainActivity: MainActivity
    private val transactionClientViewModel: TransactionClientViewModel by activityViewModels()
    private val transactionViewModel: TransactionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentTransactionSalesModifyBinding =
            FragmentTransactionSalesModifyBinding.inflate(inflater)
        mainActivity = activity as MainActivity
        setBind()
        setViewModel()
        return fragmentTransactionSalesModifyBinding.root
    }

    private fun setBind() {
        fragmentTransactionSalesModifyBinding.run {
            modifySalesFragmentToolbar.setNavigationOnClickListener {
                mainActivity.removeFragment(MainActivity.TRANSACTION_SALES_MODIFY_FRAGMENT)
            }

            modifySalesBtn.setOnClickListener {
                transactionClientViewModel.selectedClient.value?.let {
                    val itemPrice = removeNumberFormat("${transactionModifyItemPriceEditText.text}")
                    val itemCount = removeNumberFormat("${transactionModifyItemCountEditText.text}")
                    val amountReceived =
                        removeNumberFormat("${transactionModifyAmountReceivedEditText.text}")

                    transactionViewModel.updateModifySalesTransaction(
                        it,
                        transactionModifyItemNameEditText.text.toString(),
                        itemCount,
                        itemPrice,
                        amountReceived
                    )
                    transactionViewModel.setModifyTransaction(null)
                }
            }
            transactionModifyItemNameEditText.onFocusChangeListener =
                TransactionInputEditTextFocusListener()
            transactionModifyItemCountEditText.onFocusChangeListener =
                TransactionInputEditTextFocusListener()
            transactionModifyItemPriceEditText.onFocusChangeListener =
                TransactionInputEditTextFocusListener()
            transactionModifyAmountReceivedEditText.onFocusChangeListener =
                TransactionInputEditTextFocusListener()

            transactionModifyClientSelectEditText.onFocusChangeListener =
                TransactionSelectEditTextFocusListener(
                    SelectTransactionClientDialog(transactionClientViewModel),
                    childFragmentManager
                )

            transactionModifyItemNameEditText.addTextChangedListener(
                ItemNameTextWatcher(
                    transactionClientViewModel,
                    modifySalesBtn
                )
            )

            transactionModifyItemCountEditText.addTextChangedListener(
                ItemTextWatcher(
                    transactionClientViewModel,
                    transactionModifyItemCountEditText,
                    transactionModifyItemPriceEditText,
                    transactionModifyAmountReceivedEditText,
                    modifySalesBtn
                )
            )

            transactionModifyItemPriceEditText.addTextChangedListener(
                ItemTextWatcher(
                    transactionClientViewModel,
                    transactionModifyItemPriceEditText,
                    transactionModifyItemCountEditText,
                    transactionModifyAmountReceivedEditText,
                    modifySalesBtn
                )
            )

            transactionModifyAmountReceivedEditText.addTextChangedListener(
                AmountTextWatcher(
                    transactionClientViewModel,
                    transactionModifyAmountReceivedEditText,
                    modifySalesBtn
                )
            )

            modifySalesFragmentToolbar.setNavigationOnClickListener {
                mainActivity.removeFragment(MainActivity.TRANSACTION_SALES_MODIFY_FRAGMENT)
            }

            setTransactionAmountMessage(amountModifyMessageTextView)

            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                transactionViewModel.setModifyTransaction(null)
            }

            mainActivity.hideKeyboardAndClearFocus(transactionModifyItemNameEditText)
            mainActivity.hideKeyboardAndClearFocus(transactionModifyItemCountEditText)
            mainActivity.hideKeyboardAndClearFocus(transactionModifyItemPriceEditText)
            mainActivity.hideKeyboardAndClearFocus(transactionModifyAmountReceivedEditText)
        }
    }

    private fun setViewModel() {
        transactionViewModel.modifyTransaction.observe(viewLifecycleOwner) { transaction ->
            transaction?.let {
                transaction.setModifyViewValue(
                    fragmentTransactionSalesModifyBinding.transactionModifyItemNameEditText,
                    fragmentTransactionSalesModifyBinding.transactionModifyItemCountEditText,
                    fragmentTransactionSalesModifyBinding.transactionModifyItemPriceEditText,
                    fragmentTransactionSalesModifyBinding.transactionModifyAmountReceivedEditText
                )
                transactionClientViewModel.setSelectClient(transaction.getClientInformation())
            } ?: mainActivity.removeFragment(MainActivity.TRANSACTION_SALES_MODIFY_FRAGMENT)
        }
        transactionClientViewModel.selectedClient.observe(viewLifecycleOwner) { client ->
            client?.setClientInfoView(fragmentTransactionSalesModifyBinding.transactionModifyClientSelectEditText)
            if (fragmentTransactionSalesModifyBinding.transactionModifyItemNameEditText.text.isNullOrEmpty() ||
                fragmentTransactionSalesModifyBinding.transactionModifyItemCountEditText.text.isNullOrEmpty() ||
                fragmentTransactionSalesModifyBinding.transactionModifyItemPriceEditText.text.isNullOrEmpty() ||
                fragmentTransactionSalesModifyBinding.transactionModifyAmountReceivedEditText.text.isNullOrEmpty()
            ) {
                fragmentTransactionSalesModifyBinding.modifySalesBtn.visibility = View.GONE
                return@observe
            }
            fragmentTransactionSalesModifyBinding.modifySalesBtn.visibility = View.VISIBLE
        }
    }
}