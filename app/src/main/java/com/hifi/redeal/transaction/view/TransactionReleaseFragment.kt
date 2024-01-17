package com.hifi.redeal.transaction.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.hifi.redeal.MainActivity
import com.hifi.redeal.databinding.FragmentTransactionReleaseBinding
import com.hifi.redeal.transaction.util.CustomInputEditTextFocusListener
import com.hifi.redeal.transaction.util.CustomSelectEditTextFocusListener
import com.hifi.redeal.transaction.util.CustomTextWatcher
import com.hifi.redeal.transaction.viewmodel.ClientViewModel
import com.hifi.redeal.transaction.viewmodel.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionReleaseFragment : Fragment() {
    private lateinit var mainActivity: MainActivity
    private lateinit var fragmentTransactionReleaseBinding: FragmentTransactionReleaseBinding
    private val clientViewModel: ClientViewModel by viewModels()
    private val transactionViewModel: TransactionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        fragmentTransactionReleaseBinding = FragmentTransactionReleaseBinding.inflate(inflater)
        mainActivity = activity as MainActivity
        setViewModel()
        setBind()

        return fragmentTransactionReleaseBinding.root
    }

    private fun setBind() {
        fragmentTransactionReleaseBinding.run {

            materialToolbar2.setNavigationOnClickListener {
                mainActivity.removeFragment(MainActivity.TRANSACTION_RELEASE_FRAGMENT)
            }

            addReleaseBtn.setOnClickListener {
                transactionViewModel.addReleaseTransaction(
                    clientViewModel.selectedClient.value!!,
                    transactionItemNameEditText.text.toString(),
                    transactionItemCountEditText.text.toString(),
                    transactionItemPriceEditText.text.toString(),
                    transactionAmountReceivedEditText.text.toString(),
                )
                mainActivity.removeFragment(MainActivity.TRANSACTION_RELEASE_FRAGMENT)
            }

            transactionItemNameEditText.onFocusChangeListener = CustomInputEditTextFocusListener()
            transactionItemCountEditText.onFocusChangeListener = CustomInputEditTextFocusListener()
            transactionItemPriceEditText.onFocusChangeListener = CustomInputEditTextFocusListener()
            transactionAmountReceivedEditText.onFocusChangeListener =
                CustomInputEditTextFocusListener()
            transactionAmountReceivedEditText2.onFocusChangeListener =
                CustomSelectEditTextFocusListener(
                    SelectTransactionClientDialog(clientViewModel),
                    childFragmentManager
                )

            transactionItemNameEditText.addTextChangedListener(
                CustomTextWatcher(
                    clientViewModel,
                    transactionItemNameEditText,
                    addReleaseBtn
                )
            )

            transactionItemCountEditText.addTextChangedListener(
                CustomTextWatcher(
                    clientViewModel,
                    transactionItemCountEditText,
                    addReleaseBtn
                )
            )

            transactionItemPriceEditText.addTextChangedListener(
                CustomTextWatcher(
                    clientViewModel,
                    transactionItemPriceEditText,
                    addReleaseBtn
                )
            )

            transactionAmountReceivedEditText.addTextChangedListener(
                CustomTextWatcher(
                    clientViewModel,
                    transactionAmountReceivedEditText,
                    addReleaseBtn
                )
            )

            mainActivity.hideKeyboardAndClearFocus(transactionItemNameEditText)
            mainActivity.hideKeyboardAndClearFocus(transactionItemCountEditText)
            mainActivity.hideKeyboardAndClearFocus(transactionItemPriceEditText)
            mainActivity.hideKeyboardAndClearFocus(transactionAmountReceivedEditText)
        }
    }

    private fun setViewModel() {
        clientViewModel.selectedClient.observe(viewLifecycleOwner) { client ->
            client.setClientInfoView(fragmentTransactionReleaseBinding.transactionAmountReceivedEditText2)
            if (fragmentTransactionReleaseBinding.transactionItemNameEditText.text.isNullOrEmpty() ||
                fragmentTransactionReleaseBinding.transactionItemCountEditText.text.isNullOrEmpty() ||
                fragmentTransactionReleaseBinding.transactionItemPriceEditText.text.isNullOrEmpty() ||
                fragmentTransactionReleaseBinding.transactionAmountReceivedEditText.text.isNullOrEmpty()
            ) {
                fragmentTransactionReleaseBinding.addReleaseBtn.visibility = View.GONE
                return@observe
            }
            fragmentTransactionReleaseBinding.addReleaseBtn.visibility = View.VISIBLE
        }
    }
}
