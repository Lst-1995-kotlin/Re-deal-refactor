package com.hifi.redeal.trade.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.hifi.redeal.R
import com.hifi.redeal.databinding.FragmentTradeSalesBinding
import com.hifi.redeal.trade.configuration.TradeAmountConfiguration.Companion.tradeAmountCheck
import com.hifi.redeal.trade.ui.viewmodel.SalesTradeAddViewModel
import com.hifi.redeal.trade.ui.adapter.viewHolder.client.TradeClientHolderFactory
import com.hifi.redeal.trade.ui.dialog.SelectTradeClientDialog
import com.hifi.redeal.trade.util.DialogShowingFocusListener
import com.hifi.redeal.trade.util.TradeInputEditTextFocusListener
import com.hifi.redeal.trade.util.TradeTextWatcher
import com.hifi.redeal.util.KeyboardFocusClearListener
import com.hifi.redeal.util.numberFormatToLong
import com.hifi.redeal.util.toNumberFormat
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TradeSalesFragment : Fragment() {
    private lateinit var fragmentTradeSalesBinding: FragmentTradeSalesBinding
    private val salesTradeAddViewModel: SalesTradeAddViewModel by viewModels()
    private val itemNameTextWatcher = TradeTextWatcher()
    private val itemCountTextWatcher = TradeTextWatcher()
    private val itemPriceTextWatcher = TradeTextWatcher()
    private val amountReceivedTextWatcher = TradeTextWatcher()

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
            DataBindingUtil.inflate(inflater, R.layout.fragment_trade_sales, container, false)
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
            tradeAmountReceivedEditText.onFocusChangeListener =
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
            tradeAmountReceivedEditText.viewTreeObserver.addOnGlobalLayoutListener(
                KeyboardFocusClearListener(tradeAmountReceivedEditText)
            )


            tradeClientSelectEditText.onFocusChangeListener =
                DialogShowingFocusListener(
                    selectTradeClientDialog,
                    childFragmentManager
                )

            // 거래처를 클릭하여 선택하였을 경우
            tradeClientHolderFactory.setOnClickListener {
                salesTradeAddViewModel.setTradeClientData(it)
                selectTradeClientDialog.dismiss()
            }

            // 품명 수정 될 때
            itemNameTextWatcher.setOnTextChangeListener {
                if (!it.isNullOrEmpty()) {
                    salesTradeAddViewModel.setItemName("$it")
                    return@setOnTextChangeListener
                }
                salesTradeAddViewModel.setItemName(null)
            }
            tradeItemNameEditText.addTextChangedListener(itemNameTextWatcher)

            // 수량 수정 될 때
            itemCountTextWatcher.setOnTextChangeListener {
                if (!it.isNullOrEmpty()) {
                    var inputNumber = "$it".numberFormatToLong()
                    if (inputNumber == 0L) {
                        salesTradeAddViewModel.setItemCount(null)
                        return@setOnTextChangeListener
                    }
                    val itemPrice =
                        tradeItemPriceEditText.text.toString().numberFormatToLong()
                    var salesPrice = inputNumber * itemPrice
                    if (!tradeAmountCheck(salesPrice)) {
                        inputNumber /= 10
                        salesPrice = inputNumber * itemPrice
                    }
                    salesTradeAddViewModel.setItemCount(inputNumber)
                    val replaceNumber = inputNumber.toNumberFormat()
                    if (salesPrice > 0L) tradeAmountReceivedEditText.setText(salesPrice.toNumberFormat())
                    tradeItemCountEditText.run {
                        removeTextChangedListener(itemCountTextWatcher)
                        setText(replaceNumber)
                        setSelection(replaceNumber.length)
                        addTextChangedListener(itemCountTextWatcher)
                    }
                    return@setOnTextChangeListener
                }
                tradeItemCountEditText.run {
                    salesTradeAddViewModel.setItemCount(null)
                    removeTextChangedListener(itemCountTextWatcher)
                    text = null
                    addTextChangedListener(itemCountTextWatcher)
                }
                tradeAmountReceivedEditText.text = null
            }
            tradeItemCountEditText.addTextChangedListener(itemCountTextWatcher)

            // 단가 수정 될 때
            itemPriceTextWatcher.setOnTextChangeListener {
                if (!it.isNullOrEmpty()) {
                    var inputNumber = "$it".numberFormatToLong()
                    if (inputNumber == 0L) {
                        salesTradeAddViewModel.setItemPrice(null)
                        return@setOnTextChangeListener
                    }
                    val itemCount =
                        tradeItemCountEditText.text.toString().numberFormatToLong()
                    var salesPrice = inputNumber * itemCount
                    if (!tradeAmountCheck(salesPrice)) {
                        inputNumber /= 10
                        salesPrice = inputNumber * itemCount
                    }
                    salesTradeAddViewModel.setItemPrice(inputNumber)
                    val replaceNumber = inputNumber.toNumberFormat()
                    if (salesPrice > 0L) tradeAmountReceivedEditText.setText(salesPrice.toNumberFormat())
                    tradeItemPriceEditText.run {
                        removeTextChangedListener(itemPriceTextWatcher)
                        setText(replaceNumber)
                        setSelection(replaceNumber.length)
                        addTextChangedListener(itemPriceTextWatcher)
                    }
                    return@setOnTextChangeListener
                }
                tradeItemPriceEditText.run {
                    salesTradeAddViewModel.setItemPrice(null)
                    removeTextChangedListener(itemPriceTextWatcher)
                    text = null
                    addTextChangedListener(itemPriceTextWatcher)
                }
                tradeAmountReceivedEditText.text = null
            }
            tradeItemPriceEditText.addTextChangedListener(itemPriceTextWatcher)

            // 받은 금액이 수정 될 때
            amountReceivedTextWatcher.setOnTextChangeListener {
                if (!it.isNullOrEmpty()) {
                    var inputNumber = "$it".numberFormatToLong()
                    if (inputNumber == 0L) {
                        salesTradeAddViewModel.setReceivedAmount(null)
                        return@setOnTextChangeListener
                    }
                    if (!tradeAmountCheck(inputNumber)) inputNumber /= 10
                    if (tradeItemPriceEditText.text.toString().numberFormatToLong() *
                        tradeItemCountEditText.text.toString().numberFormatToLong() < inputNumber) {
                        inputNumber /= 10
                    }
                    salesTradeAddViewModel.setReceivedAmount(inputNumber)
                    val replaceNumber = inputNumber.toNumberFormat()
                    tradeAmountReceivedEditText.run {
                        removeTextChangedListener(amountReceivedTextWatcher)
                        setText(replaceNumber)
                        setSelection(replaceNumber.length)
                        addTextChangedListener(amountReceivedTextWatcher)
                    }
                    return@setOnTextChangeListener
                }
                tradeAmountReceivedEditText.run {
                    salesTradeAddViewModel.setReceivedAmount(null)
                    removeTextChangedListener(amountReceivedTextWatcher)
                    text = null
                    addTextChangedListener(amountReceivedTextWatcher)
                }
            }
            tradeAmountReceivedEditText.addTextChangedListener(amountReceivedTextWatcher)
        }
    }
}
