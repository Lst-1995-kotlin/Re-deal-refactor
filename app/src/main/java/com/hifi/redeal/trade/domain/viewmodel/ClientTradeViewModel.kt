package com.hifi.redeal.trade.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.hifi.redeal.trade.data.repository.TradeClientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClientTradeViewModel @Inject constructor(
    private val tradeClientRepository: TradeClientRepository
): ViewModel() {

    val clients = tradeClientRepository.getClientTradeData().asLiveData()

}