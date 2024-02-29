package com.hifi.redeal.transaction.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hifi.redeal.MainActivity
import com.hifi.redeal.databinding.FragmentTransactionsEditBinding
import com.hifi.redeal.transaction.adapter.TransactionSelectAdapter
import com.hifi.redeal.transaction.adapter.TransactionSelectAdapterDiffCallback
import com.hifi.redeal.transaction.adapter.viewHolder.ViewHolderFactory
import com.hifi.redeal.transaction.adapter.viewHolder.transactionEdit.DepositSelectHolderFactory
import com.hifi.redeal.transaction.adapter.viewHolder.transactionEdit.SalesSelectHolderFactory
import com.hifi.redeal.transaction.configuration.TransactionType
import com.hifi.redeal.transaction.viewmodel.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TransactionsEditFragment : Fragment() {

    private lateinit var fragmentTransactionEditBinding: FragmentTransactionsEditBinding
    private lateinit var mainActivity: MainActivity
    private lateinit var transactionSelectAdapter: TransactionSelectAdapter
    private val transactionViewModel: TransactionViewModel by activityViewModels()

    @Inject
    lateinit var transactionSelectAdapterDiffCallback: TransactionSelectAdapterDiffCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentTransactionEditBinding = FragmentTransactionsEditBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        setAdapter()
        setBind()
        setViewModel()
        return fragmentTransactionEditBinding.root
    }

    private fun setAdapter() {
        val viewHolderFactories = HashMap<Int, ViewHolderFactory>()
        viewHolderFactories[TransactionType.DEPOSIT.type] =
            DepositSelectHolderFactory(transactionViewModel)
        viewHolderFactories[TransactionType.SALES.type] =
            SalesSelectHolderFactory(transactionViewModel)

        transactionSelectAdapter =
            TransactionSelectAdapter(viewHolderFactories, transactionSelectAdapterDiffCallback)
    }

    private fun setBind() {
        fragmentTransactionEditBinding.run {
            toolbarTransactionEdit.setNavigationOnClickListener {
                mainActivity.removeFragment(MainActivity.TRANSACTIONS_EDIT_FRAGMENT)
            }

            transactionEditSelectRecyclerView.run {
                adapter = transactionSelectAdapter
                layoutManager = LinearLayoutManager(context)
            }

            transactionEditCancelButton.setOnClickListener {
                mainActivity.removeFragment(MainActivity.TRANSACTIONS_EDIT_FRAGMENT)
            }

            selectTransactionDeleteButton.setOnClickListener {
                transactionViewModel.deleteSelectTransactions()
                mainActivity.removeFragment(MainActivity.TRANSACTIONS_EDIT_FRAGMENT)
            }
        }
    }

    private fun setViewModel() {
        transactionViewModel.transactionList.observe(viewLifecycleOwner) { transactions ->
            transactionSelectAdapter.submitList(transactions.sortedByDescending { it.getTransactionDate() })
            fragmentTransactionEditBinding.toolbarTransactionEditTextView.text =
                "${transactions.count { it.isSelected() }}개 선택됨."
        }

        transactionViewModel.clearDeleteSelectTransactions()
    }

}