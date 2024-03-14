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

    fun deleteTrade(tradeData: TradeData) {
        viewModelScope.launch {
            tradeRepository.deleteTrade(tradeData)
        }
    }

}
