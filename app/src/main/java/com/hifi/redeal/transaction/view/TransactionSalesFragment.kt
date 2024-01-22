package com.hifi.redeal.transaction.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.hifi.redeal.MainActivity
import com.hifi.redeal.databinding.FragmentTransactionSalesBinding
import com.hifi.redeal.transaction.configuration.TransactionAmountConfiguration.Companion.setTransactionAmountMessage
import com.hifi.redeal.transaction.util.AmountTextWatcher
import com.hifi.redeal.transaction.util.ItemNameTextWatcher
import com.hifi.redeal.transaction.util.TransactionInputEditTextFocusListener
import com.hifi.redeal.transaction.util.TransactionSelectEditTextFocusListener
import com.hifi.redeal.transaction.util.ItemTextWatcher
import com.hifi.redeal.transaction.util.TransactionNumberFormatUtil.removeNumberFormat
import com.hifi.redeal.transaction.viewmodel.ClientViewModel
import com.hifi.redeal.transaction.viewmodel.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionSalesFragment : Fragment() {
    private lateinit var mainActivity: MainActivity
    private lateinit var fragmentTransactionSalesBinding: FragmentTransactionSalesBinding
    private val clientViewModel: ClientViewModel by viewModels()
    private val transactionViewModel: TransactionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        fragmentTransactionSalesBinding = FragmentTransactionSalesBinding.inflate(inflater)
        mainActivity = activity as MainActivity
        setViewModel()
        setBind()

        return fragmentTransactionSalesBinding.root
    }

    private fun setBind() {
        fragmentTransactionSalesBinding.run {
            addSalesFragmentToolbar.setNavigationOnClickListener {
                mainActivity.removeFragment(MainActivity.TRANSACTION_SALES_FRAGMENT)
            }

            addSalesBtn.setOnClickListener {
                val itemPrice = removeNumberFormat("${transactionItemPriceEditText.text}")
                val itemCount = removeNumberFormat("${transactionItemCountEditText.text}")
                val amountReceived = removeNumberFormat("${transactionAmountReceivedEditText.text}")

                transactionViewModel.addSalesTransaction(
                    clientViewModel.selectedClient.value!!,
                    transactionItemNameEditText.text.toString(),
                    itemCount,
                    itemPrice,
                    amountReceived,
                )
                mainActivity.removeFragment(MainActivity.TRANSACTION_SALES_FRAGMENT)
            }

            transactionItemNameEditText.onFocusChangeListener = TransactionInputEditTextFocusListener()
            transactionItemCountEditText.onFocusChangeListener = TransactionInputEditTextFocusListener()
            transactionItemPriceEditText.onFocusChangeListener = TransactionInputEditTextFocusListener()
            transactionAmountReceivedEditText.onFocusChangeListener =
                TransactionInputEditTextFocusListener()

            transactionClientSelectEditText.onFocusChangeListener =
                TransactionSelectEditTextFocusListener(
                    SelectTransactionClientDialog(clientViewModel),
                    childFragmentManager
                )

            transactionItemNameEditText.addTextChangedListener(
                ItemNameTextWatcher(
                    clientViewModel,
                    addSalesBtn
                )
            )

            transactionItemCountEditText.addTextChangedListener(
                ItemTextWatcher(
                    clientViewModel,
                    transactionItemCountEditText,
                    transactionItemPriceEditText,
                    transactionAmountReceivedEditText,
                    addSalesBtn
                )
            )

            transactionItemPriceEditText.addTextChangedListener(
                ItemTextWatcher(
                    clientViewModel,
                    transactionItemPriceEditText,
                    transactionItemCountEditText,
                    transactionAmountReceivedEditText,
                    addSalesBtn
                )
            )

            transactionAmountReceivedEditText.addTextChangedListener(
                AmountTextWatcher(
                    clientViewModel,
                    transactionAmountReceivedEditText,
                    addSalesBtn
                )
            )

            setTransactionAmountMessage(amountMessageTextView)

            mainActivity.hideKeyboardAndClearFocus(transactionItemNameEditText)
            mainActivity.hideKeyboardAndClearFocus(transactionItemCountEditText)
            mainActivity.hideKeyboardAndClearFocus(transactionItemPriceEditText)
            mainActivity.hideKeyboardAndClearFocus(transactionAmountReceivedEditText)
        }
    }
    private fun setViewModel() {
        clientViewModel.selectedClient.observe(viewLifecycleOwner) { client ->
            client.setClientInfoView(fragmentTransactionSalesBinding.transactionClientSelectEditText)
            if (fragmentTransactionSalesBinding.transactionItemNameEditText.text.isNullOrEmpty() ||
                fragmentTransactionSalesBinding.transactionItemCountEditText.text.isNullOrEmpty() ||
                fragmentTransactionSalesBinding.transactionItemPriceEditText.text.isNullOrEmpty() ||
                fragmentTransactionSalesBinding.transactionAmountReceivedEditText.text.isNullOrEmpty()
            ) {
                fragmentTransactionSalesBinding.addSalesBtn.visibility = View.GONE
                return@observe
            }
            fragmentTransactionSalesBinding.addSalesBtn.visibility = View.VISIBLE
        }
    }
}
