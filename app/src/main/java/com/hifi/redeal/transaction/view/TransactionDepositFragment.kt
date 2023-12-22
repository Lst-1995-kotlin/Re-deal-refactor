package com.hifi.redeal.transaction.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hifi.redeal.MainActivity
import com.hifi.redeal.MainActivity.Companion.TRANSACTION_DEPOSIT_FRAGMENT
import com.hifi.redeal.databinding.FragmentTransactionDepositBinding
import com.hifi.redeal.transaction.adapter.ClientAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TransactionDepositFragment : Fragment() {

    private lateinit var mainActivity: MainActivity
    private lateinit var fragmentTransactionDepositBinding: FragmentTransactionDepositBinding

    @Inject
    lateinit var clientAdapter: ClientAdapter

    @Inject
    lateinit var selectTransactionClientDialog: SelectTransactionClientDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fragmentTransactionDepositBinding = FragmentTransactionDepositBinding.inflate(inflater)
        mainActivity = activity as MainActivity
        fragmentTransactionDepositBinding.run {
            addDepositBtn.setOnClickListener {
                mainActivity.removeFragment(TRANSACTION_DEPOSIT_FRAGMENT)
            }

            makeDepositBtnSelectClient.setOnClickListener {
                selectTransactionClientDialog.show(childFragmentManager, "test")
            }

            mainActivity.hideKeyboardAndClearFocus(addDepositPriceEditTextNumber)
        }
        return fragmentTransactionDepositBinding.root
    }
}
