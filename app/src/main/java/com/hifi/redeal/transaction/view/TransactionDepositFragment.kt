package com.hifi.redeal.transaction.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hifi.redeal.MainActivity
import com.hifi.redeal.databinding.FragmentTransactionDepositBinding
import com.hifi.redeal.transaction.repository.ClientRepository
import com.hifi.redeal.transaction.util.ClientConfiguration.Companion.getClientBookmarkResource
import com.hifi.redeal.transaction.util.ClientConfiguration.Companion.getClientStateResource
import com.hifi.redeal.transaction.viewmodel.ClientViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TransactionDepositFragment : Fragment() {

    private lateinit var mainActivity: MainActivity
    private lateinit var fragmentTransactionDepositBinding: FragmentTransactionDepositBinding
    private lateinit var selectTransactionClientDialog: SelectTransactionClientDialog
    private val clientViewModel: ClientViewModel by viewModels()

    @Inject
    lateinit var clientRepository: ClientRepository

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

            makeDepositBtnSelectClient.setOnClickListener {
                selectTransactionClientDialog = SelectTransactionClientDialog(clientViewModel, clientRepository)
                selectTransactionClientDialog.show(childFragmentManager, null)
            }

            mainActivity.hideKeyboardAndClearFocus(addDepositPriceEditTextNumber)
        }

        clientViewModel.selectedClient.observe(viewLifecycleOwner) {
            selectTransactionClientDialog.dismiss()
            fragmentTransactionDepositBinding.depositClientInfo.text = it.getClientName()
            getClientStateResource(it.getClientState())?.let { state ->
                fragmentTransactionDepositBinding.depositClientState.setBackgroundResource(state)
            }
            getClientBookmarkResource(it.getClientBookmark())?.let { bookmark ->
                fragmentTransactionDepositBinding.depositClientBookmark.setBackgroundResource(bookmark)
            }
        }

        return fragmentTransactionDepositBinding.root
    }

}
