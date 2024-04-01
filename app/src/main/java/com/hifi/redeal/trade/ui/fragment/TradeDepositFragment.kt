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
import com.hifi.redeal.databinding.FragmentTradeDepositBinding
import com.hifi.redeal.trade.configuration.TradeAmountConfiguration.Companion.tradeAmountCheck
import com.hifi.redeal.trade.domain.viewmodel.TradeAddViewModel
import com.hifi.redeal.trade.util.AmountTextWatcher
import com.hifi.redeal.trade.util.TradeInputEditTextFocusListener
import com.hifi.redeal.trade.util.TradeSelectClientEditTextFocusListener
import com.hifi.redeal.trade.view_refactor_before.dialog.SelectTransactionClientDialog
import com.hifi.redeal.util.KeyboardFocusClearListener
import com.hifi.redeal.util.numberFormatToLong
import com.hifi.redeal.util.toNumberFormat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TradeDepositFragment : Fragment() {

    private lateinit var fragmentTradeDepositBinding: FragmentTradeDepositBinding
    private val tradeAddViewModel: TradeAddViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        fragmentTradeDepositBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_trade_deposit, container, false)
        fragmentTradeDepositBinding.lifecycleOwner = viewLifecycleOwner
        fragmentTradeDepositBinding.viewModel = tradeAddViewModel

        setBind()
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
            val amountTextWatcher = AmountTextWatcher()
            amountTextWatcher.setOnTextChangeListener {// 변경되고 나서
                addDepositBtn.visibility =
                    if (it.isNullOrEmpty() ||
                        tradeAddViewModel.selectedClient.value == null
                    ) View.GONE
                    else View.VISIBLE
            }
            amountTextWatcher.setAfterTextChangListener { // 변경이 진행될 때
                if (!it.isNullOrEmpty()) {
                    var inputNumber = "$it".numberFormatToLong()
                    while (!tradeAmountCheck(inputNumber)) {
                        inputNumber /= 10L
                    }
                    val replaceNumber = inputNumber.toNumberFormat()
                    addDepositAmountEditTextNumber.run {
                        removeTextChangedListener(amountTextWatcher)
                        setText(replaceNumber)
                        setSelection(replaceNumber.length)
                        addTextChangedListener(amountTextWatcher)
                    }
                }
            }
            addDepositAmountEditTextNumber.addTextChangedListener(amountTextWatcher)

            // 거래처 선택 뷰를 클릭 하였을 경우
            selectDepositClientTextInputEditText.onFocusChangeListener =
                TradeSelectClientEditTextFocusListener(
                    SelectTransactionClientDialog(),
                    childFragmentManager
                )
        }
    }

}