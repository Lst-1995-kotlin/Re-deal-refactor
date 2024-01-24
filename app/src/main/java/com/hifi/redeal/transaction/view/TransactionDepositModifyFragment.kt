package com.hifi.redeal.transaction.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.hifi.redeal.MainActivity
import com.hifi.redeal.databinding.FragmentTransactionDepositModifyBinding
import com.hifi.redeal.transaction.viewmodel.ClientViewModel
import com.hifi.redeal.transaction.viewmodel.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionDepositModifyFragment : Fragment() {
    private lateinit var mainActivity: MainActivity
    private lateinit var fragmentTransactionDepositModifyBinding: FragmentTransactionDepositModifyBinding
    private val clientViewModel: ClientViewModel by viewModels()
    private val transactionViewModel: TransactionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        fragmentTransactionDepositModifyBinding = FragmentTransactionDepositModifyBinding.inflate(inflater)
        return fragmentTransactionDepositModifyBinding.root
    }

}