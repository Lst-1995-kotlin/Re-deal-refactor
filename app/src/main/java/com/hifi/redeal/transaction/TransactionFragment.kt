package com.hifi.redeal.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hifi.redeal.MainActivity
import com.hifi.redeal.R
import com.hifi.redeal.databinding.DialogAddDepositBinding
import com.hifi.redeal.databinding.DialogAddTransactionBinding
import com.hifi.redeal.databinding.FragmentTransactionBinding
import com.hifi.redeal.databinding.TransactionSelectClientBinding
import com.hifi.redeal.databinding.TransactionSelectClientItemBinding
import com.hifi.redeal.transaction.adapter.TransactionAdapter
import com.hifi.redeal.transaction.model.ClientSimpleData
import com.hifi.redeal.transaction.model.Transaction
import com.hifi.redeal.transaction.model.TransactionData
import java.math.BigInteger

class TransactionFragment : Fragment() {

    lateinit var fragmentTransactionBinding: FragmentTransactionBinding
    lateinit var mainActivity: MainActivity
    lateinit var transactionVM: TransactionViewModel

    private val transactions = mutableListOf<Transaction>()
    private val transactionAdapter = TransactionAdapter(transactions)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentTransactionBinding = FragmentTransactionBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        setViewModel()

        fragmentTransactionBinding.transactionRecyclerView.run {
            adapter = transactionAdapter
            layoutManager = LinearLayoutManager(context)
        }

        return fragmentTransactionBinding.root

    }

    private fun setViewModel() {
        transactionVM = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]

        transactionVM.run {

            transactionList.observe(viewLifecycleOwner) {
                it.sortedByDescending { it.date }.forEach {CustomTransaction ->
                    transactionAdapter.addTransaction(Transaction(CustomTransaction))
                }
            }

            transactionVM.getAllTransactionData(Firebase.auth.uid!!)
            transactionVM.getNextTransactionIdx(Firebase.auth.uid!!)
            transactionVM.getUserAllClient(Firebase.auth.uid!!)
        }

    }

}
