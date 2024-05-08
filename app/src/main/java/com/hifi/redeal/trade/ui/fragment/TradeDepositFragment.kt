package com.hifi.redeal.trade.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.hifi.redeal.R
import com.hifi.redeal.databinding.FragmentTradeDepositBinding
import com.hifi.redeal.trade.configuration.TradeAmountConfiguration.Companion.tradeAmountCheck
import com.hifi.redeal.trade.ui.adapter.viewHolder.client.TradeClientHolderFactory
import com.hifi.redeal.trade.ui.dialog.SelectTradeClientDialog
import com.hifi.redeal.trade.ui.viewmodel.DepositTradeAddViewModel
import com.hifi.redeal.trade.util.DialogShowingFocusListener
import com.hifi.redeal.trade.util.TradeInputEditTextFocusListener
import com.hifi.redeal.trade.util.TradeTextWatcher
import com.hifi.redeal.util.KeyboardFocusClearListener
import com.hifi.redeal.util.numberFormatToLong
import com.hifi.redeal.util.toNumberFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TradeDepositFragment : Fragment() {

    private lateinit var fragmentTradeDepositBinding: FragmentTradeDepositBinding
    private val tradeTextWatcher = TradeTextWatcher()
    private val depositTradeAddViewModel: DepositTradeAddViewModel by viewModels()

    @Inject
    lateinit var selectTradeClientDialog: SelectTradeClientDialog

    @Inject
    lateinit var tradeClientHolderFactory: TradeClientHolderFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        fragmentTradeDepositBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_trade_deposit, container, false)
        fragmentTradeDepositBinding.lifecycleOwner = viewLifecycleOwner
        fragmentTradeDepositBinding.viewModel = depositTradeAddViewModel

        setBind()
        setViewModel()
        return fragmentTradeDepositBinding.root
    }

    private fun setBind() {
        fragmentTradeDepositBinding.run {
            // 금액 입력 뷰 포커스 변경에 따른 백 그라운드 이미지 설정
            addDepositAmountEditTextNumber.onFocusChangeListener =
                TradeInputEditTextFocusListener()

            // 키보드 내려 갔을 경우 포커스 제거
            addDepositAmountEditTextNumber.viewTreeObserver.addOnGlobalLayoutListener(
                KeyboardFocusClearListener(addDepositAmountEditTextNumber)
            )

            selectDepositClientTextInputEditText.viewTreeObserver.addOnGlobalLayoutListener(
                KeyboardFocusClearListener(selectDepositClientTextInputEditText)
            )

            // 상단 툴바 내 백버튼 눌렀을 경우.
            addDepositTradeMaterialToolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            // 금액을 입력 하였을 경우
            tradeTextWatcher.setOnTextChangeListener {
                if (!it.isNullOrEmpty()) {
                    var inputNumber = "$it".numberFormatToLong()
                    if (inputNumber == 0L) {
                        depositTradeAddViewModel.setReceivedAmount(null)
                        return@setOnTextChangeListener
                    }
                    if (!tradeAmountCheck(inputNumber)) inputNumber /= 10L
                    val replaceNumber = inputNumber.toNumberFormat()
                    depositTradeAddViewModel.setReceivedAmount(inputNumber)
                    addDepositAmountEditTextNumber.run {
                        removeTextChangedListener(tradeTextWatcher)
                        setText(replaceNumber)
                        setSelection(replaceNumber.length)
                        addTextChangedListener(tradeTextWatcher)
                    }
                    return@setOnTextChangeListener
                }
                addDepositAmountEditTextNumber.run {
                    depositTradeAddViewModel.setReceivedAmount(null)
                    removeTextChangedListener(tradeTextWatcher)
                    text = null
                    addTextChangedListener(tradeTextWatcher)
                }
            }
            addDepositAmountEditTextNumber.addTextChangedListener(tradeTextWatcher)

            // 거래처 선택 뷰를 클릭 하였을 경우
            selectDepositClientTextInputEditText.onFocusChangeListener =
                DialogShowingFocusListener(
                    selectTradeClientDialog,
                    childFragmentManager
                )

            // 거래처를 클릭하여 선택하였을 경우
            tradeClientHolderFactory.setOnClickListener {
                depositTradeAddViewModel.setTradeClientData(it)
                selectTradeClientDialog.dismiss()
            }

            addDepositBtn.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    async {
                        depositTradeAddViewModel.insertDepositTrade()
                    }.await()
                    findNavController().popBackStack()
                }
            }

        }
    }

    private fun setViewModel() {
        Log.d("tttt","프래그먼트: ${arguments?.getInt("clientId")}")
        CoroutineScope(Dispatchers.Main).launch {
            async {
                arguments?.let { depositTradeAddViewModel.setTradeClientId(it.getInt("clientId")) }
            }.await()
        }
    }

}
