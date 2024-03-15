package com.hifi.redeal.transaction.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.hifi.redeal.transaction.model.TradeData
import com.hifi.redeal.transaction.repository.TradeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TradeViewModel @Inject constructor(
    private val tradeRepository: TradeRepository
) : ViewModel() {

    val trades: LiveData<List<TradeData>> = tradeRepository.getAllTrades().asLiveData()
    private val _salesTradeCount = MutableLiveData<Int>()
    private val _salesTradeAmount = MutableLiveData<Long>()
    private val _salesTradeReceivables = MutableLiveData<Long>()
    val salesTradeCount: LiveData<Int> get() = _salesTradeCount
    val salesTradeAmount: LiveData<Long> get() = _salesTradeAmount
    val salesTradeReceivables: LiveData<Long> get() = _salesTradeReceivables

    private val tradesCountObserver = { trades: List<TradeData> ->
        _salesTradeCount.postValue(trades.size)
    }
    private val tradesAmountObserver = { trades: List<TradeData> ->
        _salesTradeAmount.postValue(trades.sumOf { it.itemCount * it.itemPrice })
    }
    private val tradesReceivables = { trades: List<TradeData> ->
        _salesTradeReceivables.postValue(
            trades.sumOf { it.itemCount * it.itemPrice } - trades.sumOf { it.receivedAmount })
    }


    init {
        observeTrades()
    }

    override fun onCleared() {
        super.onCleared()
        // trades LiveData에 등록된 옵저버를 제거합니다.
        trades.removeObserver(tradesCountObserver)
        trades.removeObserver(tradesAmountObserver)
        trades.removeObserver(tradesReceivables)
    }

    fun deleteTrade(tradeData: TradeData) {
        viewModelScope.launch {
            tradeRepository.deleteTrade(tradeData)
        }
    }

    private fun observeTrades() {
        trades.observeForever(tradesCountObserver)
        trades.observeForever(tradesAmountObserver)
        trades.observeForever(tradesReceivables)
    }

}
