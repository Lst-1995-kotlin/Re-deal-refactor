package com.hifi.redeal.trade.view_refactor_before

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hifi.redeal.databinding.FragmentTransactionsEditBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionsEditFragment : Fragment() {

    private lateinit var fragmentTransactionEditBinding: FragmentTransactionsEditBinding

    //private lateinit var transactionSelectAdapter: TransactionSelectAdapter
    //private val transactionViewModel: TransactionViewModel by activityViewModels()

//    @Inject
//    lateinit var transactionSelectAdapterDiffCallback: TransactionSelectAdapterDiffCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentTransactionEditBinding = FragmentTransactionsEditBinding.inflate(inflater)

//        setAdapter()
//        setBind()
//        setViewModel()
        return fragmentTransactionEditBinding.root
    }

//    private fun setAdapter() {
//        val viewHolderFactories = HashMap<Int, ViewHolderFactory>()
//        viewHolderFactories[TransactionType.DEPOSIT.type] =
//            DepositSelectHolderFactory(transactionViewModel)
//        viewHolderFactories[TransactionType.SALES.type] =
//            SalesSelectHolderFactory(transactionViewModel)
//
//        transactionSelectAdapter =
//            TransactionSelectAdapter(viewHolderFactories, transactionSelectAdapterDiffCallback)
//    }
//
//    private fun setBind() {
//        fragmentTransactionEditBinding.run {
//            toolbarTransactionEdit.setNavigationOnClickListener {
//                findNavController().popBackStack()
//            }
//
//            transactionEditSelectRecyclerView.run {
//                adapter = transactionSelectAdapter
//                layoutManager = LinearLayoutManager(context)
//            }
//
//            transactionEditCancelButton.setOnClickListener {
//                findNavController().popBackStack()
//            }
//
//            selectTransactionDeleteButton.setOnClickListener {
//                transactionViewModel.deleteSelectTransactions()
//                findNavController().popBackStack()
//            }
//        }
//    }
//
//    private fun setViewModel() {
//        transactionViewModel.transactionBasicList.observe(viewLifecycleOwner) { transactions ->
//            transactionSelectAdapter.submitList(transactions.sortedByDescending { it.getTransactionDate() })
//            fragmentTransactionEditBinding.toolbarTransactionEditTextView.text = "${transactions.count { it.isSelected() }}개 선택됨."
//        }
//
//        transactionViewModel.clearDeleteSelectTransactions()
//    }

}