package com.hifi.redeal.transaction.viewmodel

import androidx.lifecycle.ViewModel
import com.hifi.redeal.transaction.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
): ViewModel() {

    var nextTransactionIdx = 0L
    fun getNextTransactionIdx() {
        transactionRepository.getNextTransactionIdx {
            for (c1 in it.result) {
                nextTransactionIdx = c1["transactionIdx"] as Long + 1L
            }
        }
    }
}
