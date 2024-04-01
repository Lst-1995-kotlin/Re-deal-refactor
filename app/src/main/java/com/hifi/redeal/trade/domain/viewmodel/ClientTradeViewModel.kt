package com.hifi.redeal.trade.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.hifi.redeal.trade.data.repository.ClientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClientTradeViewModel @Inject constructor(
    private val clientRepository: ClientRepository
): ViewModel() {

    val clients = clientRepository.getClientTradeData().asLiveData()



}