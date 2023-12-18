package com.hifi.redeal.transaction.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.hifi.redeal.databinding.FragmentTransactionDepositBinding
import com.hifi.redeal.transaction.viewmodel.TransactionViewModel

class TransactionDepositFragment : DialogFragment() {

    private val clientViewModel: TransactionViewModel by viewModels()

    private lateinit var fragmentTransactionDepositBinding: FragmentTransactionDepositBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fragmentTransactionDepositBinding = FragmentTransactionDepositBinding.inflate(inflater)
        fragmentTransactionDepositBinding.run {

        }

        return fragmentTransactionDepositBinding.root
    }
}
