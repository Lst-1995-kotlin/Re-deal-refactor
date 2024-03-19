package com.hifi.redeal.trade.domain.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hifi.redeal.trade.configuration.TradeType
import com.hifi.redeal.trade.data.model.TradeData
import com.hifi.redeal.trade.data.repository.TradeRepository
import com.hifi.redeal.trade.util.TransactionNumberFormatUtil.replaceNumberFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TradeViewModel @Inject constructor(
    private val tradeRepository: TradeRepository
) : ViewModel() {

    val trades: LiveData<List<TradeData>> = tradeRepository.getAllTrades().asLiveData()
    private val _salesTradeCount = MutableLiveData<String>()
    private val _salesTradeAmount = MutableLiveData<String>()
    private val _tradeReceivables = MutableLiveData<String>()
    val salesTradeCount: LiveData<String> get() = _salesTradeCount
    val salesTradeAmount: LiveData<String> get() = _salesTradeAmount
    val tradeReceivables: LiveData<String> get() = _tradeReceivables


    init {
        updateLiveData()
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
        _salesTradeCount.postValue(replaceNumberFormat(trades.count { it.type == TradeType.SALES.type }))
    }

    private fun updateSalesTradeAmount(trades: List<TradeData>) {
        _salesTradeAmount.postValue(
            replaceNumberFormat(
                trades.sumOf { it.itemCount * it.itemPrice }
            )
        )
    }

    private fun updateSalesTradeReceivables(trades: List<TradeData>) {
        _tradeReceivables.postValue(
            replaceNumberFormat(
                trades.sumOf { it.itemCount * it.itemPrice - it.receivedAmount }
            )
        )
    }

}
