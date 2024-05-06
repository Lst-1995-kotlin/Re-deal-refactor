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
import com.hifi.redeal.databinding.FragmentTradeDepositModifyBinding
import com.hifi.redeal.trade.configuration.TradeAmountConfiguration
import com.hifi.redeal.trade.ui.adapter.viewHolder.client.TradeClientHolderFactory
import com.hifi.redeal.trade.ui.dialog.SelectTradeClientDialog
import com.hifi.redeal.trade.ui.viewmodel.DepositTradeModifyViewModel
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
class TradeDepositModifyFragment : Fragment() {

    private lateinit var fragmentTradeDepositModifyBinding: FragmentTradeDepositModifyBinding
    private val depositTradeModifyViewModel: DepositTradeModifyViewModel by viewModels()
    private val tradeTextWatcher = TradeTextWatcher()

    @Inject
    lateinit var selectTradeClientDialog: SelectTradeClientDialog

    @Inject
    lateinit var tradeClientHolderFactory: TradeClientHolderFactory
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentTradeDepositModifyBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_trade_deposit_modify,
                container,
                false
            )
        fragmentTradeDepositModifyBinding.lifecycleOwner = viewLifecycleOwner
        fragmentTradeDepositModifyBinding.viewModel = depositTradeModifyViewModel
        setBind()
        setViewModel()

        return fragmentTradeDepositModifyBinding.root
    }


    private fun setBind() {
        fragmentTradeDepositModifyBinding.run {
            // 거래처 선택 뷰를 클릭 하였을 경우
            modifyDepositClientTextInputEditText.onFocusChangeListener =
                DialogShowingFocusListener(
                    selectTradeClientDialog,
                    childFragmentManager
                )

            // 거래처를 클릭하여 선택하였을 경우
            tradeClientHolderFactory.setOnClickListener {
                depositTradeModifyViewModel.setModifyClient(it.id)
                selectTradeClientDialog.dismiss()
            }

            // 금액 입력 뷰 포커스 변경에 따른 백 그라운드 이미지 설정
            modifyDepositPriceEditTextNumber.onFocusChangeListener =
                TradeInputEditTextFocusListener()

            // 키보드 내려 갔을 경우 포커스 제거
            modifyDepositPriceEditTextNumber.viewTreeObserver.addOnGlobalLayoutListener(
                KeyboardFocusClearListener(modifyDepositPriceEditTextNumber)
            )

            // 금액을 입력 하였을 경우
            tradeTextWatcher.setOnTextChangeListener {
                if (!it.isNullOrEmpty()) {
                    var inputNumber = "$it".numberFormatToLong()
                    if (inputNumber == 0L) {
                        depositTradeModifyViewModel.setReceivedAmount(null)
                        return@setOnTextChangeListener
                    }
                    if (!TradeAmountConfiguration.tradeAmountCheck(inputNumber)) inputNumber /= 10L
                    val replaceNumber = inputNumber.toNumberFormat()
                    depositTradeModifyViewModel.setReceivedAmount(inputNumber)
                    modifyDepositPriceEditTextNumber.run {
                        removeTextChangedListener(tradeTextWatcher)
                        setText(replaceNumber)
                        setSelection(replaceNumber.length)
                        addTextChangedListener(tradeTextWatcher)
                    }
                    return@setOnTextChangeListener
                }
                modifyDepositPriceEditTextNumber.run {
                    depositTradeModifyViewModel.setReceivedAmount(null)
                    removeTextChangedListener(tradeTextWatcher)
                    text = null
                    addTextChangedListener(tradeTextWatcher)
                }
            }
            modifyDepositPriceEditTextNumber.addTextChangedListener(tradeTextWatcher)

            // 버튼을 눌렀을 경우 업데이트가 완료 된 후 프래그먼트 종료 로직 순차적 실행
            modifyDepositBtn.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    async {
                        depositTradeModifyViewModel.updateTradeData()
                    }.await()
                    findNavController().popBackStack()
                }
            }

            modifyDepositMaterialToolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun setViewModel() {
        arguments?.let { depositTradeModifyViewModel.setModifyTradeId(it.getInt("tradeId")) }
    }
}