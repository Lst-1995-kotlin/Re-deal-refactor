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
import com.hifi.redeal.databinding.FragmentTradeSalesModifyBinding
import com.hifi.redeal.trade.configuration.TradeAmountConfiguration
import com.hifi.redeal.trade.ui.adapter.viewHolder.client.TradeClientHolderFactory
import com.hifi.redeal.trade.ui.dialog.SelectTradeClientDialog
import com.hifi.redeal.trade.ui.viewmodel.SalesTradeModifyViewModel
import com.hifi.redeal.trade.util.DialogShowingFocusListener
import com.hifi.redeal.trade.util.TradeInputEditTextFocusListener
import com.hifi.redeal.trade.util.TradeTextWatcher
import com.hifi.redeal.util.numberFormatToLong
import com.hifi.redeal.util.toNumberFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TradeSalesModifyFragment : Fragment() {

    private lateinit var fragmentTradeSalesModifyBinding: FragmentTradeSalesModifyBinding
    private val salesTradeModifyViewModel: SalesTradeModifyViewModel by viewModels()

    private val itemNameTextWatcher = TradeTextWatcher()
    private val itemCountTextWatcher = TradeTextWatcher()
    private val itemPriceTextWatcher = TradeTextWatcher()
    private val amountReceivedTextWatcher = TradeTextWatcher()

    @Inject
    lateinit var selectTradeClientDialog: SelectTradeClientDialog

    @Inject
    lateinit var tradeClientHolderFactory: TradeClientHolderFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentTradeSalesModifyBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_trade_sales_modify,
            container,
            false
        )
        fragmentTradeSalesModifyBinding.lifecycleOwner = viewLifecycleOwner
        fragmentTradeSalesModifyBinding.viewModel = salesTradeModifyViewModel

        setBind()
        setViewModel()
        return fragmentTradeSalesModifyBinding.root
    }


    private fun setBind() {
        fragmentTradeSalesModifyBinding.run {
            modifySalesFragmentToolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            // 거래처를 클릭하여 선택하였을 경우
            tradeClientHolderFactory.setOnClickListener {
                salesTradeModifyViewModel.setModifyClient(it.id)
                selectTradeClientDialog.dismiss()
            }

            modifySalesBtn.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    async {
                        salesTradeModifyViewModel.updateTradeData()
                    }.await()
                    findNavController().popBackStack()
                }
            }
            tradeModifyItemNameEditText.onFocusChangeListener =
                TradeInputEditTextFocusListener()
            tradeModifyItemCountEditText.onFocusChangeListener =
                TradeInputEditTextFocusListener()
            tradeModifyItemPriceEditText.onFocusChangeListener =
                TradeInputEditTextFocusListener()
            tradeModifyAmountReceivedEditText.onFocusChangeListener =
                TradeInputEditTextFocusListener()

            tradeModifyClientSelectEditText.onFocusChangeListener =
                DialogShowingFocusListener(
                    selectTradeClientDialog,
                    childFragmentManager
                )

            // TODO : 이후 수량, 가격, 받은 금액에 대한 코드 수정 예정
            // 품명이 변경되었을 경우
            itemNameTextWatcher.setOnTextChangeListener {
                if (it.isNullOrEmpty()) {
                    salesTradeModifyViewModel.setItemName(null)
                    return@setOnTextChangeListener
                }
                salesTradeModifyViewModel.setItemName(it.toString())
            }
            tradeModifyItemNameEditText.addTextChangedListener(itemNameTextWatcher)

            // 갯수가 변경되었을 경우
            itemCountTextWatcher.setOnTextChangeListener {
                if (!it.isNullOrEmpty()) {
                    var inputNumber = "$it".numberFormatToLong()
                    if (inputNumber == 0L) {
                        salesTradeModifyViewModel.setItemCount(null)
                        return@setOnTextChangeListener
                    }
                    val itemPrice =
                        tradeModifyItemPriceEditText.text.toString().numberFormatToLong()
                    var salesPrice = inputNumber * itemPrice
                    if (!TradeAmountConfiguration.tradeAmountCheck(salesPrice)) {
                        inputNumber /= 10
                        salesPrice = inputNumber * itemPrice
                    }
                    salesTradeModifyViewModel.setItemCount(inputNumber)
                    val replaceNumber = inputNumber.toNumberFormat()
                    if (salesPrice > 0L) tradeModifyAmountReceivedEditText.setText(salesPrice.toNumberFormat())
                    tradeModifyItemCountEditText.run {
                        removeTextChangedListener(itemCountTextWatcher)
                        setText(replaceNumber)
                        setSelection(replaceNumber.length)
                        addTextChangedListener(itemCountTextWatcher)
                    }
                    return@setOnTextChangeListener
                }
                tradeModifyItemCountEditText.run {
                    salesTradeModifyViewModel.setItemCount(null)
                    removeTextChangedListener(itemCountTextWatcher)
                    text = null
                    addTextChangedListener(itemCountTextWatcher)
                }
                tradeModifyAmountReceivedEditText.text = null
            }
            tradeModifyItemCountEditText.addTextChangedListener(itemCountTextWatcher)

            // 가격이 변경될 때
            itemPriceTextWatcher.setOnTextChangeListener {
                if (!it.isNullOrEmpty()) {
                    var inputNumber = "$it".numberFormatToLong()
                    if (inputNumber == 0L) {
                        salesTradeModifyViewModel.setItemPrice(null)
                        return@setOnTextChangeListener
                    }
                    val itemCount =
                        tradeModifyItemCountEditText.text.toString().numberFormatToLong()
                    var salesPrice = inputNumber * itemCount
                    if (!TradeAmountConfiguration.tradeAmountCheck(salesPrice)) {
                        inputNumber /= 10
                        salesPrice = inputNumber * itemCount
                    }
                    salesTradeModifyViewModel.setItemPrice(inputNumber)
                    val replaceNumber = inputNumber.toNumberFormat()
                    if (salesPrice > 0L) tradeModifyAmountReceivedEditText.setText(salesPrice.toNumberFormat())
                    tradeModifyItemPriceEditText.run {
                        removeTextChangedListener(itemPriceTextWatcher)
                        setText(replaceNumber)
                        setSelection(replaceNumber.length)
                        addTextChangedListener(itemPriceTextWatcher)
                    }
                    return@setOnTextChangeListener
                }
                tradeModifyItemPriceEditText.run {
                    salesTradeModifyViewModel.setItemPrice(null)
                    removeTextChangedListener(itemPriceTextWatcher)
                    text = null
                    addTextChangedListener(itemPriceTextWatcher)
                }
                tradeModifyAmountReceivedEditText.text = null
            }
            tradeModifyItemPriceEditText.addTextChangedListener(itemPriceTextWatcher)

            // 받은 금액이 수정 될 때
            amountReceivedTextWatcher.setOnTextChangeListener {
                if (!it.isNullOrEmpty()) {
                    var inputNumber = "$it".numberFormatToLong()
                    if (inputNumber == 0L) {
                        salesTradeModifyViewModel.setReceivedAmount(null)
                        return@setOnTextChangeListener
                    }
                    if (!TradeAmountConfiguration.tradeAmountCheck(inputNumber)) inputNumber /= 10
                    if (tradeModifyItemPriceEditText.text.toString().numberFormatToLong() *
                        tradeModifyItemCountEditText.text.toString()
                            .numberFormatToLong() < inputNumber
                    ) {
                        inputNumber /= 10
                    }
                    salesTradeModifyViewModel.setReceivedAmount(inputNumber)
                    val replaceNumber = inputNumber.toNumberFormat()
                    tradeModifyAmountReceivedEditText.run {
                        removeTextChangedListener(amountReceivedTextWatcher)
                        setText(replaceNumber)
                        setSelection(replaceNumber.length)
                        addTextChangedListener(amountReceivedTextWatcher)
                    }
                    return@setOnTextChangeListener
                }
                tradeModifyAmountReceivedEditText.run {
                    salesTradeModifyViewModel.setReceivedAmount(null)
                    removeTextChangedListener(amountReceivedTextWatcher)
                    text = null
                    addTextChangedListener(amountReceivedTextWatcher)
                }
            }
            tradeModifyAmountReceivedEditText.addTextChangedListener(amountReceivedTextWatcher)

            modifySalesFragmentToolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

        }
    }

    private fun setViewModel() {
        CoroutineScope(Dispatchers.Main).launch {
            async {
                arguments?.let { salesTradeModifyViewModel.setModifyTradeId(it.getInt("tradeId")) }
            }.await()
        }
    }
}