package com.hifi.redeal.trade.ui.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.hifi.redeal.trade.configuration.TradeType
import com.hifi.redeal.trade.data.model.TradeClientData
import com.hifi.redeal.trade.data.model.TradeData
import com.hifi.redeal.trade.data.model.toTradeEntry
import com.hifi.redeal.trade.domain.usecase.TradeClientUseCase
import com.hifi.redeal.trade.domain.usecase.TradeUseCase
import com.hifi.redeal.util.numberFormatToLong
import com.hifi.redeal.util.toNumberFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DepositTradeModifyViewModel @Inject constructor(
    private val tradeUseCase: TradeUseCase,
    private val tradeClientUseCase: TradeClientUseCase
) : ViewModel() {

    private val _modifyReceivedAmount = MutableLiveData<String?>()
    private val _modifyClient = MutableLiveData<TradeClientData>()
    private val _visibility = MutableLiveData<Int>()

    private val modifyTradeId = MutableLiveData<Int>()

    private val modifyTrade = modifyTradeId.switchMap {
        it?.let {
            tradeUseCase.getTradeById(it).asLiveData()
        } ?: MutableLiveData()
    }

    val modifyReceivedAmount: LiveData<String?> get() = _modifyReceivedAmount
    val modifyClient: LiveData<TradeClientData> get() = _modifyClient
    val visibility: LiveData<Int> = _visibility // 버튼의 visibility 값을 관리.

    init {
        _visibility.postValue(View.VISIBLE)
        modifyTrade.observeForever {
            _modifyReceivedAmount.postValue(it.receivedAmount.toNumberFormat())
            setModifyClient(it.clientId)
        }
        modifyReceivedAmount.observeForever {
            buttonVisibilityCheck()
        }
        modifyClient.observeForever {
            buttonVisibilityCheck()
        }
    }

    private fun liveDataValueCheck(): Boolean {
        return modifyTrade.value != null &&
                modifyReceivedAmount.value != null
    }

    private fun buttonVisibilityCheck() {
        if (liveDataValueCheck()) {
            _visibility.postValue(View.VISIBLE)
            return
        }
        _visibility.postValue(View.GONE)
    }

    fun setModifyTradeId(id: Int) {
        modifyTradeId.postValue(id)
    }

    fun setModifyClient(id: Int) {
        viewModelScope.launch {
            tradeClientUseCase.getClientById(id).collect {
                _modifyClient.postValue(it)
            }
        }
    }

    fun setReceivedAmount(value: Long?) {
        value?.let {
            _modifyReceivedAmount.postValue(it.toNumberFormat())
        } ?: _modifyReceivedAmount.postValue(null)
    }

    fun updateTradeData() {
        if (liveDataValueCheck()) { // 한번 더 확인
            viewModelScope.launch {
                val updateData = TradeData(
                    modifyTrade.value!!.id,
                    "",
                    0,
                    0,
                    modifyReceivedAmount.value!!.numberFormatToLong(),
                    TradeType.DEPOSIT.type,
                    modifyTrade.value!!.date,
                    modifyClient.value!!.id,
                    modifyClient.value!!.name
                )
                tradeUseCase.updateTrade(updateData.toTradeEntry())
            }
        }
    }
}