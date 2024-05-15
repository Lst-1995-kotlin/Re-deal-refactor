package com.hifi.redeal.trade.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.hifi.redeal.trade.configuration.TradeType
import com.hifi.redeal.trade.data.model.TradeData
import com.hifi.redeal.trade.data.model.TradeSelectData
import com.hifi.redeal.trade.domain.usecase.TradeUseCase
import com.hifi.redeal.util.toNumberFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TradeSelectViewModel @Inject constructor(
    private val tradeUseCase: TradeUseCase
) : ViewModel() {

    private val _clientId = MutableLiveData<Int?>()
    private val _selectTradeCount = MutableLiveData<Int>()

    val selectTradeCount: LiveData<Int> get() = _selectTradeCount

    val clientId: LiveData<Int?> = _clientId

    // _clientId를 관찰하고 _trades를 이에 따라 업데이트하기 위해 switchMap 사용
    val trades: LiveData<List<TradeSelectData>> = clientId.switchMap {
        it?.let {
            tradeUseCase.getSelectTradeByClient(it).asLiveData()
        } ?: tradeUseCase.getAllSelectTrade().asLiveData()
    }


    init {
        updateLiveData()
    }

    fun setClientId(clientId: Int?) {
        _clientId.postValue(clientId)
    }

    fun deleteTrade(tradeData: TradeData) {
        viewModelScope.launch {
            tradeUseCase.deleteTrade(tradeData)
        }
    }

    private fun updateLiveData() {
        viewModelScope.launch {
            trades.asFlow().collect { trades ->
                _selectTradeCount.postValue(trades.count { it.checked })
            }
        }
    }

}
