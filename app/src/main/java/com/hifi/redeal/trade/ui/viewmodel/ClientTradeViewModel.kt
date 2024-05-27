package com.hifi.redeal.trade.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.hifi.redeal.trade.domain.usecase.TradeClientUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClientTradeViewModel @Inject constructor(
    private val clientUseCase: TradeClientUseCase,
) : ViewModel() {

    val clients = clientUseCase.getClients().asLiveData()

}