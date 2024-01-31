package com.hifi.redeal.transaction.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hifi.redeal.transaction.model.Client
import com.hifi.redeal.transaction.model.ClientData
import com.hifi.redeal.transaction.repository.ClientRepository
import com.hifi.redeal.transaction.configuration.ClientConfiguration.Companion.isClientStateNotStop
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor(
    private val clientRepository: ClientRepository,
) : ViewModel() {

    val selectedClient = MutableLiveData<Client?>()
    private val _clients = MutableLiveData<List<Client>>()
    val clients: LiveData<List<Client>> get() = _clients

    init {
        getUserAllClient()
    }

    fun setSelectClient(client: Client) {
        selectedClient.postValue(client)
    }

    fun setSelectClient(clientIdx: Long?) {
        clientIdx?.let {
            val selectClient = _clients.value?.firstOrNull { it.getClientIdx() == clientIdx }
            selectClient?.let { selectedClient.postValue(it) }
        } ?: selectedClient.postValue(null)
    }

    private fun updateClients(newData: List<Client>?) {
        newData?.let { _clients.postValue(it.sortedByDescending { it.getClientIdx() }) }
            ?: _clients.postValue(emptyList())
    }

    private fun getUserAllClient() {
        clientRepository.getUserAllClient {
            val temp = mutableListOf<Client>()
            for (c1 in it.result) {
                val clientData = ClientData(
                    c1["clientIdx"] as Long,
                    c1["clientName"] as String,
                    c1["clientManagerName"] as String,
                    c1["clientState"] as Long,
                    c1["isBookmark"] as Boolean,
                )

                if (isClientStateNotStop(clientData.clientState)) {
                    temp.add(Client(clientData))
                    updateClients(temp)
                }
            }
        }
    }
}
