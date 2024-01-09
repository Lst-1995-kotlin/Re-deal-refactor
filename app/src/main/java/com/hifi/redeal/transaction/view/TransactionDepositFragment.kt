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
import com.hifi.redeal.databinding.FragmentTransactionDepositBinding
import com.hifi.redeal.transaction.viewmodel.ClientViewModel
import com.hifi.redeal.transaction.viewmodel.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionDepositFragment : Fragment() {

    private lateinit var mainActivity: MainActivity
    private lateinit var fragmentTransactionDepositBinding: FragmentTransactionDepositBinding
    private var selectTransactionClientDialog: SelectTransactionClientDialog? = null
    private val clientViewModel: ClientViewModel by viewModels()
    private val transactionViewModel: TransactionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        fragmentTransactionDepositBinding = FragmentTransactionDepositBinding.inflate(inflater)
        mainActivity = activity as MainActivity
        setBind()
        setViewModel()
        return fragmentTransactionDepositBinding.root
    }

    private fun setBind() {
        fragmentTransactionDepositBinding.run {
            addDepositBtn.setOnClickListener {
                if (clientSelectedCheck() ||
                    inputValueCheck(addDepositPriceEditTextNumber)
                ) {
                    return@setOnClickListener
                }
                transactionViewModel.addDepositTransaction(
                    clientViewModel.selectedClient.value!!,
                    addDepositPriceEditTextNumber.text.toString().trim(),
                )
                mainActivity.removeFragment(MainActivity.TRANSACTION_DEPOSIT_FRAGMENT)
            }

            makeDepositBtnSelectClient.setOnClickListener {
                selectTransactionClientDialog = SelectTransactionClientDialog(clientViewModel)
                selectTransactionClientDialog?.show(childFragmentManager, null)
            }

            materialToolbar.setNavigationOnClickListener {
                mainActivity.removeFragment(MainActivity.TRANSACTION_DEPOSIT_FRAGMENT)
            }

            mainActivity.hideKeyboardAndClearFocus(addDepositPriceEditTextNumber)
        }
    }

    private fun setViewModel() {
        clientViewModel.selectedClient.observe(viewLifecycleOwner) { client ->
            selectTransactionClientDialog?.dismiss()
            client.setClientInfoView(fragmentTransactionDepositBinding.depositClientInfo)
            client.setClientStateView(fragmentTransactionDepositBinding.depositClientState)
            client.setClientBookmarkView(fragmentTransactionDepositBinding.depositClientBookmark)
        }
    }

    private fun inputValueCheck(textInputEditText: TextInputEditText): Boolean {
        transactionViewModel.inputValueCheck(textInputEditText)?.let {
            it.apply {
                anchorView = fragmentTransactionDepositBinding.addDepositBtn
            }.show()
            return true
        } ?: return false
    }

    private fun clientSelectedCheck(): Boolean {
        clientViewModel.selectedClient.value?.let {
            return false
        } ?: Snackbar.make(
            fragmentTransactionDepositBinding.root,
            "선택된 거래처가 없습니다..",
            Snackbar.LENGTH_SHORT,
        ).apply {
            anchorView = fragmentTransactionDepositBinding.addDepositBtn
        }.show()
        return true
    }
}
