package com.hifi.redeal.transaction.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hifi.redeal.transaction.model.ClientSimpleData
import com.hifi.redeal.transaction.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
) : ViewModel() {

    var nextTransactionIdx = 0L

    var clientSimpleDataListVM = MutableLiveData<MutableList<ClientSimpleData>>()
    var tempClientSimpleDataList = mutableListOf<ClientSimpleData>()
    fun getNextTransactionIdx() {
        transactionRepository.getNextTransactionIdx {
            for (c1 in it.result) {
                nextTransactionIdx = c1["transactionIdx"] as Long + 1L
            }
        }
    }

    fun getUserAllClient() {
        tempClientSimpleDataList.clear()
        transactionRepository.getUserAllClient() {
            for (c1 in it.result) {
                val newClientData = ClientSimpleData(
                    c1["clientIdx"] as Long,
                    c1["clientName"] as String,
                    c1["clientManagerName"] as String,
                    c1["clientState"] as Long,
                    c1["isBookmark"] as Boolean,
                )
                tempClientSimpleDataList.add(newClientData)
                clientSimpleDataListVM.postValue(tempClientSimpleDataList)
            }
        }
    }
}
