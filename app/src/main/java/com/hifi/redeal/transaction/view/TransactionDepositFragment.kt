package com.hifi.redeal.transaction.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hifi.redeal.MainActivity
import com.hifi.redeal.databinding.FragmentTransactionDepositBinding
import com.hifi.redeal.transaction.viewmodel.ClientViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionDepositFragment : Fragment() {

    private val clientViewModel: ClientViewModel by viewModels()
    private lateinit var mainActivity: MainActivity
    private lateinit var fragmentTransactionDepositBinding: FragmentTransactionDepositBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fragmentTransactionDepositBinding = FragmentTransactionDepositBinding.inflate(inflater)
        mainActivity = activity as MainActivity
        fragmentTransactionDepositBinding.run {
            addDepositBtn.setOnClickListener {
            }
            mainActivity.hideKeyboardAndClearFocus(addDepositPriceEditTextNumber)
        }

        return fragmentTransactionDepositBinding.root
    }
}
