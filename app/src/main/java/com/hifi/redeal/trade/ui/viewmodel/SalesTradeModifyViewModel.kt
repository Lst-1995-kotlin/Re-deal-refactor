package com.hifi.redeal.trade.ui.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
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
class SalesTradeModifyViewModel @Inject constructor(
    private val tradeUseCase: TradeUseCase,
    private val tradeClientUseCase: TradeClientUseCase
) : ViewModel() {
    private val _modifyItemName = MutableLiveData<String?>()
    private val _modifyItemCount = MutableLiveData<String?>()
    private val _modifyItemPrice = MutableLiveData<String?>()
    private val _modifyReceivedAmount = MutableLiveData<String?>()
    private val _modifyClient = MutableLiveData<TradeClientData>()
    private val _visibility = MutableLiveData<Int>()

    private val modifyItemNameObserver = Observer<String?>{
        buttonVisibilityCheck()
    }
    private val modifyItemCountObserver = Observer<String?>{
        buttonVisibilityCheck()
    }
    private val modifyItemPriceObserver = Observer<String?>{
        buttonVisibilityCheck()
    }
    private val modifyReceivedAmountObserver = Observer<String?>{
        buttonVisibilityCheck()
    }
    private val modifySelectedClientObserver = Observer<TradeClientData>{
        buttonVisibilityCheck()
    }

    private val modifyTradeId = MutableLiveData<Int>()

    private val modifyTrade = modifyTradeId.switchMap {
        it?.let {
            tradeUseCase.getTradeById(it).asLiveData()
        } ?: MutableLiveData()
    }

    private val modifyTradeObserver = Observer<TradeData> { tradeData ->
        _modifyReceivedAmount.postValue(tradeData.receivedAmount.toNumberFormat())
        _modifyItemName.postValue(tradeData.itemName)
        _modifyItemCount.postValue(tradeData.itemCount.toNumberFormat())
        _modifyItemPrice.postValue(tradeData.itemPrice.toNumberFormat())
        setModifyClient(tradeData.clientId)
    }

    val modifyItemName: LiveData<String?> get() = _modifyItemName
    val modifyItemCount: LiveData<String?> get() = _modifyItemCount
    val modifyItemPrice: LiveData<String?> get() = _modifyItemPrice
    val modifyReceivedAmount: LiveData<String?> get() = _modifyReceivedAmount
    val modifyClient: LiveData<TradeClientData> get() = _modifyClient
    val visibility: LiveData<Int> = _visibility // 버튼의 visibility 값을 관리.

    init {
        _visibility.postValue(View.VISIBLE)
        modifyTrade.observeForever(modifyTradeObserver)
        modifyItemName.observeForever(modifyItemNameObserver)
        modifyItemCount.observeForever(modifyItemCountObserver)
        modifyItemPrice.observeForever(modifyItemPriceObserver)
        modifyReceivedAmount.observeForever(modifyReceivedAmountObserver)
        modifyClient.observeForever(modifySelectedClientObserver)
    }

    override fun onCleared() {
        super.onCleared()
        modifyTrade.removeObserver(modifyTradeObserver)
        modifyItemName.removeObserver(modifyItemNameObserver)
        modifyItemCount.removeObserver(modifyItemCountObserver)
        modifyItemPrice.removeObserver(modifyItemPriceObserver)
        modifyReceivedAmount.removeObserver(modifyReceivedAmountObserver)
        modifyClient.removeObserver(modifySelectedClientObserver)
    }

    private fun liveDataValueCheck(): Boolean {
        return modifyTrade.value != null &&
                modifyItemName.value != null &&
                modifyItemCount.value != null &&
                modifyItemPrice.value != null &&
                modifyReceivedAmount.value != null &&
                modifyClient.value != null
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

    fun setItemName(value: String?) {
        _modifyItemName.postValue(value)
    }

    fun setItemCount(value: Long?) {
        value?.let {
            _modifyItemCount.postValue(it.toNumberFormat())
        }?: _modifyItemCount.postValue(null)
    }

    fun setItemPrice(value: Long?) {
        value?.let {
            _modifyItemPrice.postValue(it.toNumberFormat())
        }?: _modifyItemPrice.postValue(null)
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
                    modifyItemName.value!!,
                    modifyItemCount.value!!.numberFormatToLong(),
                    modifyItemPrice.value!!.numberFormatToLong(),
                    modifyReceivedAmount.value!!.numberFormatToLong(),
                    TradeType.SALES.type,
                    modifyTrade.value!!.date,
                    modifyClient.value!!.id,
                    modifyClient.value!!.name,
                    modifyClient.value!!.managerName
                )
                tradeUseCase.updateTrade(updateData.toTradeEntry())
            }
        }
    }

}