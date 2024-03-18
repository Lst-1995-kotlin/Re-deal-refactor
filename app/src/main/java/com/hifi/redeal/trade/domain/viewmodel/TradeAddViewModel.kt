package com.hifi.redeal.trade.domain.viewmodel

import androidx.lifecycle.ViewModel
import com.hifi.redeal.trade.data.repository.TradeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TradeAddViewModel @Inject constructor(
    private val tradeRepository: TradeRepository
) : ViewModel() {

    fun addDepositTrade() {

    }
}