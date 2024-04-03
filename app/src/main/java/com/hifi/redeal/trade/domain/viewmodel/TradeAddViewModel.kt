package com.hifi.redeal.trade.domain.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hifi.redeal.data.entrie.TradeEntry
import com.hifi.redeal.trade.configuration.TradeType
import com.hifi.redeal.trade.data.model.TradeClientData
import com.hifi.redeal.trade.data.repository.TradeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TradeAddViewModel @Inject constructor(
    private val tradeRepository: TradeRepository
) : ViewModel() {

    private val _closeFragmentEvent = MutableLiveData<Unit>()
    private val _inputAmount = MutableLiveData<Long>()
    private val _selectedClient = MutableLiveData<TradeClientData>()
    val closeFragmentEvent: LiveData<Unit> get() = _closeFragmentEvent
    val inputAmount: LiveData<Long> get() = _inputAmount
    val selectedClient: LiveData<TradeClientData> get() = _selectedClient

    fun callFragmentCloseEvent() {
        _closeFragmentEvent.postValue(Unit)
    }

    fun insertTrade() {
        viewModelScope.launch {
            inputAmount.value?.let { receivedAmount ->
                tradeRepository.insertTrade(
                    TradeEntry(
                        itemName = "",
                        itemCount = 0L,
                        itemPrice = 0L,
                        receivedAmount = receivedAmount,
                        type = TradeType.DEPOSIT.type,
                        date = Date(),
                        checked = false,
                        clientId = 0
                    )
                )
            }
        }
    }

    fun setTradeClientData(tradeClientData: TradeClientData) {
        _selectedClient.postValue(tradeClientData)
    }

}