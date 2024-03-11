package com.hifi.redeal.transaction.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hifi.redeal.transaction.repository.TradeRepository
import javax.inject.Inject

class TradeViewModelFactory @Inject constructor(
    private val tradeRepository: TradeRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TradeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TradeViewModel(tradeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
