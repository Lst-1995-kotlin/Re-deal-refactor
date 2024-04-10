package com.hifi.redeal.trade.view_refactor_before

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.hifi.redeal.R
import com.hifi.redeal.databinding.FragmentTransactionSalesBinding
import com.hifi.redeal.trade.domain.viewmodel.SalesTradeAddViewModel
import com.hifi.redeal.trade.ui.adapter.viewHolder.client.TradeClientHolderFactory
import com.hifi.redeal.trade.ui.dialog.SelectTradeClientDialog
import com.hifi.redeal.trade.util.DialogShowingFocusListener
import com.hifi.redeal.trade.util.TradeInputEditTextFocusListener
import com.hifi.redeal.util.KeyboardFocusClearListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TransactionSalesFragment : Fragment() {
    private lateinit var fragmentTradeSalesBinding: FragmentTransactionSalesBinding
    private val salesTradeAddViewModel: SalesTradeAddViewModel by viewModels()

    @Inject
    lateinit var selectTradeClientDialog: SelectTradeClientDialog

    @Inject
    lateinit var tradeClientHolderFactory: TradeClientHolderFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        fragmentTradeSalesBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_transaction_sales, container, false)
        fragmentTradeSalesBinding.lifecycleOwner = viewLifecycleOwner
        fragmentTradeSalesBinding.viewModel = salesTradeAddViewModel
        setBind()

        return fragmentTradeSalesBinding.root
    }


    private fun setBind() {
        fragmentTradeSalesBinding.run {
            addSalesMaterialToolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            addSalesBtn.setOnClickListener {
                salesTradeAddViewModel.insertSalesTrade()
                findNavController().popBackStack()
            }

            // 입력 뷰 포커스 변경에 따른 백 그라운드 이미지 설정
            tradeItemNameEditText.onFocusChangeListener =
                TradeInputEditTextFocusListener()
            tradeItemCountEditText.onFocusChangeListener =
                TradeInputEditTextFocusListener()
            tradeItemPriceEditText.onFocusChangeListener =
                TradeInputEditTextFocusListener()
            transactionAmountReceivedEditText.onFocusChangeListener =
                TradeInputEditTextFocusListener()

            // 키보드 내려 갔을 경우 포커스 제거
            tradeItemNameEditText.viewTreeObserver.addOnGlobalLayoutListener(
                KeyboardFocusClearListener(tradeItemNameEditText)
            )
            tradeItemCountEditText.viewTreeObserver.addOnGlobalLayoutListener(
                KeyboardFocusClearListener(tradeItemCountEditText)
            )
            tradeItemPriceEditText.viewTreeObserver.addOnGlobalLayoutListener(
                KeyboardFocusClearListener(tradeItemPriceEditText)
            )
            transactionAmountReceivedEditText.viewTreeObserver.addOnGlobalLayoutListener(
                KeyboardFocusClearListener(transactionAmountReceivedEditText)
            )


            tradeClientSelectEditText.onFocusChangeListener =
                DialogShowingFocusListener(
                    selectTradeClientDialog,
                    childFragmentManager
                )

//            transactionItemNameEditText.addTextChangedListener(
//
//            )
//
//            transactionItemCountEditText.addTextChangedListener(
//
//            )
//
//            transactionItemPriceEditText.addTextChangedListener(
//
//            )
//
//            transactionAmountReceivedEditText.addTextChangedListener(
//
//            )


        }
    }
//
//    private fun setViewModel() {
//        transactionClientViewModel.selectedClient.observe(viewLifecycleOwner) { client ->
//            client?.setClientInfoView(fragmentTransactionSalesBinding.transactionClientSelectEditText)
//            if (fragmentTransactionSalesBinding.transactionItemNameEditText.text.isNullOrEmpty() ||
//                fragmentTransactionSalesBinding.transactionItemCountEditText.text.isNullOrEmpty() ||
//                fragmentTransactionSalesBinding.transactionItemPriceEditText.text.isNullOrEmpty() ||
//                fragmentTransactionSalesBinding.transactionAmountReceivedEditText.text.isNullOrEmpty()
//            ) {
//                fragmentTransactionSalesBinding.addSalesBtn.visibility = View.GONE
//                return@observe
//            }
//            fragmentTransactionSalesBinding.addSalesBtn.visibility = View.VISIBLE
//        }
//    }
}
