package com.hifi.redeal.trade.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.hifi.redeal.trade.data.model.TradeClientData
import com.hifi.redeal.trade.domain.usecase.TradeClientUseCase
import com.hifi.redeal.trade.domain.usecase.TradeUseCase
import com.hifi.redeal.util.toNumberFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DepositTradeModifyViewModel @Inject constructor(
    private val tradeUseCase: TradeUseCase,
    private val tradeClientUseCase: TradeClientUseCase
) : ViewModel() {

    private val _modifyReceivedAmount = MutableLiveData<String>()
    private val _modifyClient = MutableLiveData<TradeClientData>()

    private val modifyTradeId = MutableLiveData<Int>()

    private val modifyTrade = modifyTradeId.switchMap {
        it?.let {
            tradeUseCase.getTradeById(it).asLiveData()
        } ?: MutableLiveData()
    }

    val modifyReceivedAmount: LiveData<String> get() = _modifyReceivedAmount
    val modifyClient: LiveData<TradeClientData> get() = _modifyClient

    init {
        modifyTrade.observeForever {
            _modifyReceivedAmount.postValue(it.receivedAmount.toNumberFormat())
            setModifyClient(it.clientId)
        }
    }

    fun setModifyTradeId(id: Int) {
        modifyTradeId.postValue(id)
    }

    fun setModifyClient(id: Int) {
        viewModelScope.launch {
            val temp = tradeClientUseCase.getClientById(id)
            temp.collect {
                _modifyClient.postValue(it)
            }
        }
    }
}