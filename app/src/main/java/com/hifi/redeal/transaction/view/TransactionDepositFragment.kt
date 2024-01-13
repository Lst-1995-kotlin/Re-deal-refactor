package com.hifi.redeal.transaction.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.hifi.redeal.MainActivity
import com.hifi.redeal.R
import com.hifi.redeal.databinding.FragmentTransactionDepositBinding
import com.hifi.redeal.transaction.viewmodel.ClientViewModel
import com.hifi.redeal.transaction.viewmodel.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.Locale

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
//                if (clientSelectedCheck() ||
//                    inputValueCheck(addDepositPriceEditTextNumber)
//                ) {
//                    return@setOnClickListener
//                }
                transactionViewModel.addDepositTransaction(
                    clientViewModel.selectedClient.value!!,
                    addDepositPriceEditTextNumber.text.toString().replace(",", ""),
                )
                mainActivity.removeFragment(MainActivity.TRANSACTION_DEPOSIT_FRAGMENT)
            }

            addDepositPriceEditTextNumber.setOnFocusChangeListener { view, _ ->
                if (view.hasFocus()) {
                    view.setBackgroundResource(R.drawable.bottom_border_focus)
                    return@setOnFocusChangeListener
                }
                view.setBackgroundResource(R.drawable.bottom_border_not_focus)
            }

            addDepositPriceEditTextNumber.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0.isNullOrEmpty() || clientViewModel.selectedClient.value == null) {
                        addDepositBtn.visibility = View.GONE
                    } else {
                        addDepositBtn.visibility = View.VISIBLE
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0.isNullOrEmpty()) {
                        addDepositPriceEditTextNumber.removeTextChangedListener(this)
                        addDepositPriceEditTextNumber.setText("")
                        addDepositPriceEditTextNumber.addTextChangedListener(this)
                    } else if ("$p0".all { it.isDigit() || it == ',' }) {
                        val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
                        val number = numberFormat.format("$p0".replace(",", "").toLong())
                        addDepositPriceEditTextNumber.removeTextChangedListener(this)
                        addDepositPriceEditTextNumber.setText(number)
                        addDepositPriceEditTextNumber.setSelection(number.length)
                        addDepositPriceEditTextNumber.addTextChangedListener(this)
                    }
                }
            })

            selectDepositClientTextInputEditText.setOnFocusChangeListener { view, _ -> // 현재 임시 테스트 중
                if (view.hasFocus()) {
                    selectTransactionClientDialog = SelectTransactionClientDialog(clientViewModel)
                    selectTransactionClientDialog?.show(childFragmentManager, null)
                }
                view.clearFocus()
                return@setOnFocusChangeListener
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
            client.setClientInfoView(fragmentTransactionDepositBinding.selectDepositClientTextInputEditText)
            if (!fragmentTransactionDepositBinding.addDepositPriceEditTextNumber.text.isNullOrEmpty()) {
                fragmentTransactionDepositBinding.addDepositBtn.visibility = View.VISIBLE
                return@observe
            }
            fragmentTransactionDepositBinding.addDepositBtn.visibility = View.GONE
//            client.setClientInfoView(fragmentTransactionDepositBinding.depositClientInfo)
//            client.setClientStateView(fragmentTransactionDepositBinding.depositClientState)
//            client.setClientBookmarkView(fragmentTransactionDepositBinding.depositClientBookmark)
        }
    }

//    private fun inputValueCheck(textInputEditText: TextInputEditText): Boolean {
//        transactionViewModel.inputValueCheck(textInputEditText)?.let {
//            it.apply {
//                anchorView = fragmentTransactionDepositBinding.root
//            }.show()
//            return true
//        } ?: return false
//    }

//    private fun clientSelectedCheck(): Boolean {
//        clientViewModel.selectedClient.value?.let {
//            return false
//        } ?: Snackbar.make(
//            fragmentTransactionDepositBinding.root,
//            "선택된 거래처가 없습니다..",
//            Snackbar.LENGTH_SHORT,
//        ).apply {
//            anchorView = fragmentTransactionDepositBinding.addDepositBtn
//        }.show()
//        return true
//    }
}
