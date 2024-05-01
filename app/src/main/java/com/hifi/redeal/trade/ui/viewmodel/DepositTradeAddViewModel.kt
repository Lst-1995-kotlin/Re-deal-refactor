package com.hifi.redeal.trade.ui.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hifi.redeal.data.entrie.TradeEntity
import com.hifi.redeal.trade.configuration.TradeType
import com.hifi.redeal.trade.data.model.TradeClientData
import com.hifi.redeal.trade.data.repository.TradeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class DepositTradeAddViewModel @Inject constructor(
    private val tradeRepository: TradeRepository
) : ViewModel() {

    private val _receivedAmount = MutableLiveData<Long?>()
    private val _selectedClient = MutableLiveData<TradeClientData>()
    private val _visibility = MutableLiveData<Int>()

    val receivedAmount: LiveData<Long?> get() = _receivedAmount
    val selectedClient: LiveData<TradeClientData> get() = _selectedClient
    val visibility: LiveData<Int> = _visibility // 버튼의 visibility 값을 관리.

    init {
        _visibility.postValue(View.GONE)

        receivedAmount.observeForever {
            buttonVisibilityCheck()
        }

        selectedClient.observeForever {
            buttonVisibilityCheck()
        }
    }

    private fun liveDataValueCheck(): Boolean {
        return receivedAmount.value != null &&
                selectedClient.value != null
    }

    private fun buttonVisibilityCheck() {
        if (liveDataValueCheck()) {
            _visibility.postValue(View.VISIBLE)
            return
        }
        _visibility.postValue(View.GONE)
    }

    fun setTradeClientData(tradeClientData: TradeClientData) {
        _selectedClient.postValue(tradeClientData)
    }

    fun setReceivedAmount(value: Long?) {
        _receivedAmount.postValue(value)
    }

    fun insertDepositTrade() {
        viewModelScope.launch {
            if (receivedAmount.value != null && selectedClient.value != null) {
                tradeRepository.insertTrade(
                    TradeEntity(
                        itemName = "",
                        itemCount = 0L,
                        itemPrice = 0L,
                        receivedAmount = receivedAmount.value!!,
                        type = TradeType.DEPOSIT.type,
                        date = Date(),
                        checked = false,
                        clientId = selectedClient.value!!.id
                    )
                )
            }
        }
    }

}