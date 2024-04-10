package com.hifi.redeal.trade.domain.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.hifi.redeal.trade.configuration.TradeType
import com.hifi.redeal.trade.data.model.TradeData
import com.hifi.redeal.trade.data.repository.TradeRepository
import com.hifi.redeal.util.toNumberFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TradeByClientViewModel @Inject constructor(
    private val tradeRepository: TradeRepository
) : ViewModel() {

    private val _clientId = MutableLiveData<Int?>()
    private val _salesTradeCount = MutableLiveData<String>()
    private val _salesTradeAmount = MutableLiveData<String>()
    private val _tradeReceivables = MutableLiveData<String>()
    val salesTradeCount: LiveData<String> get() = _salesTradeCount
    val salesTradeAmount: LiveData<String> get() = _salesTradeAmount
    val tradeReceivables: LiveData<String> get() = _tradeReceivables

    val clientId: LiveData<Int?> = _clientId
    // _clientId를 관찰하고 _trades를 이에 따라 업데이트하기 위해 switchMap 사용
    val trades: LiveData<List<TradeData>> = _clientId.switchMap { clientId ->
        if (clientId != null) {
            tradeRepository.getAllTradeByClient(clientId).asLiveData()
        } else {
            MutableLiveData()
        }
    }

    init {
        updateLiveData()
    }

    fun setClientId(clientId: Int?) {
        _clientId.postValue(clientId)
    }

    fun deleteTrade(tradeData: TradeData) {
        viewModelScope.launch {
            tradeRepository.deleteTrade(tradeData)
        }
    }

    private fun updateLiveData() {
        viewModelScope.launch {
            trades.asFlow().collect { trades ->
                updateSalesTradeCount(trades)
                updateSalesTradeAmount(trades)
                updateSalesTradeReceivables(trades)
            }
        }
    }

    private fun updateSalesTradeCount(trades: List<TradeData>) {
        _salesTradeCount.postValue(trades.count { it.type == TradeType.SALES.type }
            .toNumberFormat())
    }

    private fun updateSalesTradeAmount(trades: List<TradeData>) {
        _salesTradeAmount.postValue(
            trades.sumOf { it.itemCount * it.itemPrice }.toNumberFormat()
        )
    }

    private fun updateSalesTradeReceivables(trades: List<TradeData>) {
        _tradeReceivables.postValue(
            trades.sumOf { it.itemCount * it.itemPrice - it.receivedAmount }.toNumberFormat()
        )
    }

}
