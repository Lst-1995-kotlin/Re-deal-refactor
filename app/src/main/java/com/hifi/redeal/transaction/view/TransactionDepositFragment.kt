package com.hifi.redeal.transaction.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.api.ResourceProto.resource
import com.hifi.redeal.MainActivity
import com.hifi.redeal.databinding.FragmentTransactionDepositBinding
import com.hifi.redeal.transaction.util.ClientConfiguration.Companion.getClientBookmarkResource
import com.hifi.redeal.transaction.util.ClientConfiguration.Companion.getClientStateResource
import com.hifi.redeal.transaction.viewmodel.ClientViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionDepositFragment : Fragment() {

    private lateinit var mainActivity: MainActivity
    private lateinit var fragmentTransactionDepositBinding: FragmentTransactionDepositBinding
    private lateinit var selectTransactionClientDialog: SelectTransactionClientDialog
    private val clientViewModel: ClientViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fragmentTransactionDepositBinding = FragmentTransactionDepositBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        setViewModel()
        setBind()

        return fragmentTransactionDepositBinding.root
    }

    private fun setBind() {
        fragmentTransactionDepositBinding.run {
            addDepositBtn.setOnClickListener {
            }

            makeDepositBtnSelectClient.setOnClickListener {
                selectTransactionClientDialog = SelectTransactionClientDialog(clientViewModel)
                selectTransactionClientDialog.show(childFragmentManager, null)
            }

            materialToolbar.setNavigationOnClickListener {
                mainActivity.removeFragment(MainActivity.TRANSACTION_DEPOSIT_FRAGMENT)
            }

            mainActivity.hideKeyboardAndClearFocus(addDepositPriceEditTextNumber)
        }
    }

    private fun setViewModel() {
        clientViewModel.selectedClient.observe(viewLifecycleOwner) {
            selectTransactionClientDialog.dismiss()
            fragmentTransactionDepositBinding.depositClientInfo.text = it.getClientInfo()

            val clientState = it.getClientState()
            val stateResource = getClientStateResource(clientState)
            fragmentTransactionDepositBinding.depositClientState.run {
                setImageResource(stateResource ?: 0)
                visibility = stateResource?.let { View.VISIBLE } ?: View.GONE
            }

            val clientBookmark = it.getClientBookmark()
            val bookmarkResource = getClientBookmarkResource(clientBookmark)
            fragmentTransactionDepositBinding.depositClientBookmark.run {
                setImageResource(bookmarkResource ?: 0)
                visibility = bookmarkResource?.let { View.VISIBLE } ?: View.GONE
            }
        }
    }
}
