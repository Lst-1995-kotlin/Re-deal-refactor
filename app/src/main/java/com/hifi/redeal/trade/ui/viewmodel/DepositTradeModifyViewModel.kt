package com.hifi.redeal.trade.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.hifi.redeal.trade.domain.usecase.TradeUseCase
import com.hifi.redeal.util.toNumberFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DepositTradeModifyViewModel @Inject constructor(
    private val tradeUseCase: TradeUseCase
) : ViewModel() {

    private val _modifyReceivedAmount = MutableLiveData<String>()
    private val _modifyClientName = MutableLiveData<String>()

    private val modifyTradeIndex = MutableLiveData<Int>()

    private val modifyTrade = modifyTradeIndex.switchMap {
        it?.let {
            tradeUseCase.getTradeById(it).asLiveData()
        } ?: MutableLiveData()
    }

    val modifyReceivedAmount: LiveData<String> get() = _modifyReceivedAmount
    val modifyClientName: LiveData<String> get() = _modifyClientName

    init {
        modifyTrade.observeForever {
            _modifyReceivedAmount.postValue(it.receivedAmount.toNumberFormat())
            _modifyClientName.postValue(it.clientName)
        }
    }

    fun setModifyTradeId(id: Int) {
        modifyTradeIndex.postValue(id)
    }

}