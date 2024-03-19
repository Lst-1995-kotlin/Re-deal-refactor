package com.hifi.redeal.trade.domain.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hifi.redeal.data.entrie.TradeEntry
import com.hifi.redeal.trade.configuration.TradeType
import com.hifi.redeal.trade.data.repository.TradeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TradeAddViewModel @Inject constructor(
    private val tradeRepository: TradeRepository
) : ViewModel() {

    private val _inputAmount = MutableLiveData<Long>()
    val inputAmount: LiveData<Long> get() = _inputAmount
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
}