package com.hifi.redeal.trade.domain.viewmodel

import android.view.View
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
class SalesTradeAddViewModel @Inject constructor(
    private val tradeRepository: TradeRepository
) : ViewModel() {

    private val _itemName = MutableLiveData<String>()
    private val _itemCount = MutableLiveData<Long>()
    private val _itemPrice = MutableLiveData<Long>()
    private val _receivedAmount = MutableLiveData<Long>()
    private val _selectedClient = MutableLiveData<TradeClientData>()
    private val _visibility = MutableLiveData<Int>()

    val itemName: LiveData<String> = _itemName
    val itemCount: LiveData<Long> = _itemCount
    val itemPrice: LiveData<Long> = _itemPrice
    val receivedAmount: LiveData<Long> = _receivedAmount
    val selectedClient: LiveData<TradeClientData> = _selectedClient
    val visibility: LiveData<Int> = _visibility // 버튼의 visibility 값을 관리.

    init {
        buttonVisibilityCheck()
    }

    fun setReceivedAmount(value: Long) {
        _receivedAmount.postValue(value)
    }

    fun setTradeClientData(tradeClientData: TradeClientData) {
        _selectedClient.postValue(tradeClientData)
    }

    private fun buttonVisibilityCheck() {
        if (liveDataValueCheck()) {
            _visibility.postValue(View.VISIBLE)
            return
        }
        _visibility.postValue(View.GONE)
    }

    private fun liveDataValueCheck(): Boolean {
        return itemName.value != null &&
                itemCount.value != null &&
                itemPrice.value != null &&
                receivedAmount.value != null &&
                selectedClient.value != null
    }

    fun insertSalesTrade() {
        viewModelScope.launch {
            if (liveDataValueCheck()) {
                tradeRepository.insertTrade(
                    TradeEntry(
                        itemName = itemName.value!!,
                        itemCount = itemCount.value!!,
                        itemPrice = itemPrice.value!!,
                        receivedAmount = receivedAmount.value!!,
                        type = TradeType.SALES.type,
                        date = Date(),
                        checked = false,
                        clientId = selectedClient.value!!.id
                    )
                )
            }
        }
    }

}