package com.hifi.redeal.transaction.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.hifi.redeal.MainActivity
import com.hifi.redeal.databinding.FragmentTransactionReleaseBinding
import com.hifi.redeal.transaction.viewmodel.ClientViewModel
import com.hifi.redeal.transaction.viewmodel.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionReleaseFragment : Fragment() {

    private lateinit var mainActivity: MainActivity
    private lateinit var fragmentTransactionReleaseBinding: FragmentTransactionReleaseBinding
    private var selectTransactionClientDialog: SelectTransactionClientDialog? = null
    private val clientViewModel: ClientViewModel by viewModels()
    private val transactionViewModel: TransactionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fragmentTransactionReleaseBinding = FragmentTransactionReleaseBinding.inflate(inflater)
        mainActivity = activity as MainActivity
        setViewModel()
        setBind()

        return fragmentTransactionReleaseBinding.root
    }

    private fun setBind() {
        fragmentTransactionReleaseBinding.run {
            makeReleaseBtnSelectClient.setOnClickListener {
                selectTransactionClientDialog = SelectTransactionClientDialog(clientViewModel)
                selectTransactionClientDialog?.show(childFragmentManager, null)
            }

            addReleaseBtn.setOnClickListener {
                clientViewModel.selectedClient.value ?: return@setOnClickListener
                if (transactionNameEditText.text.isNullOrEmpty()) return@setOnClickListener
                if (transactionItemCountEditText.text.isNullOrEmpty()) return@setOnClickListener
                if (transactionItemPriceEditText.text.isNullOrEmpty()) return@setOnClickListener
                if (transactionAmountReceivedEditText.text.isNullOrEmpty()) return@setOnClickListener
                transactionViewModel.addReleaseTransaction(
                    clientViewModel.selectedClient.value!!,
                    transactionNameEditText.text.toString(),
                    transactionItemCountEditText.text.toString(),
                    transactionItemPriceEditText.text.toString(),
                    transactionAmountReceivedEditText.text.toString()
                )
                mainActivity.removeFragment(MainActivity.TRANSACTION_RELEASE_FRAGMENT)
            }

            mainActivity.hideKeyboardAndClearFocus(transactionNameEditText)
            mainActivity.hideKeyboardAndClearFocus(transactionItemCountEditText)
            mainActivity.hideKeyboardAndClearFocus(transactionItemPriceEditText)
            mainActivity.hideKeyboardAndClearFocus(transactionAmountReceivedEditText)
        }
    }

    private fun setViewModel() {
        clientViewModel.selectedClient.observe(viewLifecycleOwner) { client ->
            selectTransactionClientDialog?.dismiss()
            client.setClientInfoView(fragmentTransactionReleaseBinding.releaseClientInfo)
            client.setClientStateView(fragmentTransactionReleaseBinding.releaseClientState)
            client.setClientBookmarkView(fragmentTransactionReleaseBinding.releaseClientBookmark)
        }
    }
}
