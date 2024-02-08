package com.hifi.redeal.transaction.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.hifi.redeal.MainActivity
import com.hifi.redeal.databinding.FragmentTransactionDepositModifyBinding
import com.hifi.redeal.transaction.util.AmountTextWatcher
import com.hifi.redeal.transaction.util.TransactionNumberFormatUtil.removeNumberFormat
import com.hifi.redeal.transaction.util.TransactionSelectEditTextFocusListener
import com.hifi.redeal.transaction.viewmodel.ClientViewModel
import com.hifi.redeal.transaction.viewmodel.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionDepositModifyFragment : Fragment() {
    private lateinit var mainActivity: MainActivity
    private lateinit var fragmentTransactionDepositModifyBinding: FragmentTransactionDepositModifyBinding
    private val clientViewModel: ClientViewModel by activityViewModels()
    private val transactionViewModel: TransactionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainActivity = activity as MainActivity
        fragmentTransactionDepositModifyBinding =
            FragmentTransactionDepositModifyBinding.inflate(inflater)
        setBind()
        setViewModel()

        return fragmentTransactionDepositModifyBinding.root
    }

    private fun setBind() {
        fragmentTransactionDepositModifyBinding.run {
            modifyDepositClientTextInputEditText.onFocusChangeListener =
                TransactionSelectEditTextFocusListener(
                    SelectTransactionClientDialog(clientViewModel),
                    childFragmentManager
                )

            modifyDepositPriceEditTextNumber.addTextChangedListener(
                AmountTextWatcher(
                    clientViewModel,
                    modifyDepositPriceEditTextNumber,
                    modifyDepositBtn
                )
            )

            modifyDepositBtn.setOnClickListener {
                clientViewModel.selectedClient.value?.let {
                    transactionViewModel.updateModifyDepositTransaction(
                        it,
                        removeNumberFormat("${modifyDepositPriceEditTextNumber.text}")
                    )
                    transactionViewModel.setModifyTransaction(null)
                }
            }

            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                transactionViewModel.setModifyTransaction(null)
            }
        }
    }

    private fun setViewModel() {
        transactionViewModel.modifyTransaction.observe(viewLifecycleOwner) { transaction ->
            transaction?.let {
                transaction.setModifyViewValue(fragmentTransactionDepositModifyBinding.modifyDepositPriceEditTextNumber)
                clientViewModel.setSelectClient(transaction.getTransactionClientIdx())
            } ?: mainActivity.removeFragment(MainActivity.TRANSACTION_DEPOSIT_MODIFY_FRAGMENT)
        }
        clientViewModel.selectedClient.observe(viewLifecycleOwner) { client ->
            client?.setClientInfoView(fragmentTransactionDepositModifyBinding.modifyDepositClientTextInputEditText)
            if (fragmentTransactionDepositModifyBinding.modifyDepositPriceEditTextNumber.text.isNullOrEmpty()) {
                fragmentTransactionDepositModifyBinding.modifyDepositBtn.visibility = View.GONE
                return@observe
            }
            fragmentTransactionDepositModifyBinding.modifyDepositBtn.visibility = View.VISIBLE
        }
    }
}