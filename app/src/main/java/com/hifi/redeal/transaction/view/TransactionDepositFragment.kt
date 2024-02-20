package com.hifi.redeal.transaction.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.hifi.redeal.MainActivity
import com.hifi.redeal.databinding.FragmentTransactionDepositBinding
import com.hifi.redeal.transaction.util.AmountTextWatcher
import com.hifi.redeal.transaction.util.TransactionInputEditTextFocusListener
import com.hifi.redeal.transaction.util.TransactionSelectEditTextFocusListener
import com.hifi.redeal.transaction.util.TransactionNumberFormatUtil.removeNumberFormat
import com.hifi.redeal.transaction.view.dialog.SelectTransactionClientDialog
import com.hifi.redeal.transaction.viewmodel.TransactionClientViewModel
import com.hifi.redeal.transaction.viewmodel.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionDepositFragment : Fragment() {

    private lateinit var mainActivity: MainActivity
    private lateinit var fragmentTransactionDepositBinding: FragmentTransactionDepositBinding
    private val transactionClientViewModel: TransactionClientViewModel by activityViewModels()
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
    override fun onDestroyView() {
        super.onDestroyView()
        transactionClientViewModel.setSelectClientIndex(null)
    }

    private fun setBind() {
        fragmentTransactionDepositBinding.run {
            addDepositBtn.setOnClickListener {
                transactionClientViewModel.selectedClient.value?.let { client ->
                    transactionViewModel.addDepositTransaction(
                        client, removeNumberFormat("${addDepositPriceEditTextNumber.text}")
                    )
                    transactionViewModel.setMoveToPosition(0)
                }
                mainActivity.removeFragment(MainActivity.TRANSACTION_DEPOSIT_FRAGMENT)
            }

            addDepositPriceEditTextNumber.onFocusChangeListener =
                TransactionInputEditTextFocusListener()

            addDepositPriceEditTextNumber.addTextChangedListener(
                AmountTextWatcher(
                    transactionClientViewModel,
                    addDepositPriceEditTextNumber,
                    addDepositBtn
                )
            )

            selectDepositClientTextInputEditText.onFocusChangeListener =
                TransactionSelectEditTextFocusListener(
                    SelectTransactionClientDialog(transactionClientViewModel),
                    childFragmentManager
                )

            addDepositMaterialToolbar.setNavigationOnClickListener {
                transactionClientViewModel.setSelectClientIndex(null)
                mainActivity.removeFragment(MainActivity.TRANSACTION_DEPOSIT_FRAGMENT)
            }

            mainActivity.hideKeyboardAndClearFocus(addDepositPriceEditTextNumber)
        }
    }

    private fun setViewModel() {
        transactionClientViewModel.selectedClient.observe(viewLifecycleOwner) { client ->
            client?.setClientInfoView(fragmentTransactionDepositBinding.selectDepositClientTextInputEditText)
            if (fragmentTransactionDepositBinding.addDepositPriceEditTextNumber.text.isNullOrEmpty()) {
                fragmentTransactionDepositBinding.addDepositBtn.visibility = View.GONE
                return@observe
            }
            fragmentTransactionDepositBinding.addDepositBtn.visibility = View.VISIBLE
        }
    }
}
