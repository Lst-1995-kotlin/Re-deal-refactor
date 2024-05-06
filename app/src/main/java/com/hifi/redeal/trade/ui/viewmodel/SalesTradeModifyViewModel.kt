package com.hifi.redeal.trade.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.hifi.redeal.trade.domain.usecase.TradeClientUseCase
import com.hifi.redeal.trade.domain.usecase.TradeUseCase
import javax.inject.Inject

class SalesTradeModifyViewModel @Inject constructor(
    private val tradeUseCase: TradeUseCase,
    private val tradeClientUseCase: TradeClientUseCase
) : ViewModel() {
}