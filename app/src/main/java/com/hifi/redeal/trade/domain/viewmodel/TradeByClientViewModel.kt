package com.hifi.redeal.trade.domain.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.hifi.redeal.trade.data.model.TradeData
import com.hifi.redeal.trade.data.repository.TradeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TradeByClientViewModel @Inject constructor(
    private val tradeRepository: TradeRepository
) : ViewModel() {

    private val _clientId = MutableLiveData<Int?>()
    val clientId: LiveData<Int?> = _clientId
    // _clientId를 관찰하고 _trades를 이에 따라 업데이트하기 위해 switchMap 사용
    val trades: LiveData<List<TradeData>> = _clientId.switchMap { clientId ->
        if (clientId != null) {
            tradeRepository.getAllTradeByClient(clientId).asLiveData()
        } else {
            MutableLiveData()
        }
    }

    fun setClientId(clientId: Int?) {
        _clientId.postValue(clientId)
    }

    fun deleteTrade(tradeData: TradeData) {
        viewModelScope.launch {
            tradeRepository.deleteTrade(tradeData)
        }
    }

}
