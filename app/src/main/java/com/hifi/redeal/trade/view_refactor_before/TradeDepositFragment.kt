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
import com.hifi.redeal.databinding.FragmentTradeDepositBinding
import com.hifi.redeal.trade.domain.viewmodel.TradeAddViewModel
import com.hifi.redeal.trade.util.TradeInputEditTextFocusListener
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
        fragmentTradeDepositBinding.tradeAddViewModel = tradeAddViewModel

        setBind()
        return fragmentTradeDepositBinding.root
    }

    private fun setBind() {
        fragmentTradeDepositBinding.run {
            // 금액 입력 뷰 포커스 시 뷰 설정
            addDepositPriceEditTextNumber.onFocusChangeListener =
                TradeInputEditTextFocusListener()
            // 상단 툴바 내 백버튼 눌렀을 경우.
            addDepositMaterialToolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            // 금액을 입력 하였을 경우

            // 거래처 선택 뷰를 클릭 하였을 경우
        }
    }

}
