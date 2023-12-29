package com.hifi.redeal.transaction.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hifi.redeal.transaction.model.Client
import com.hifi.redeal.transaction.model.ClientSimpleData
import com.hifi.redeal.transaction.repository.ClientRepository
import com.hifi.redeal.transaction.util.ClientConfiguration
import com.hifi.redeal.transaction.util.ClientConfiguration.Companion.isClientStateNotStop
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor(
    private val clientRepository: ClientRepository
) : ViewModel() {

    val selectedClient = MutableLiveData<Client>()
    private val _clients = mutableListOf<Client>()
    val clients = MutableLiveData<List<Client>>()

    fun setSelectClient(client: Client) {
        selectedClient.postValue(client)
    }

    fun getUserAllClient() {
        clientRepository.getUserAllClient {
            _clients.clear()
            for (c1 in it.result) {
                val clientData = ClientSimpleData(
                    c1["clientIdx"] as Long,
                    c1["clientName"] as String,
                    c1["clientManagerName"] as String,
                    c1["clientState"] as Long,
                    c1["isBookmark"] as Boolean,
                )

                if (isClientStateNotStop(clientData.clientState)) {
                    _clients.add(Client(clientData))
                    clients.postValue(_clients)
                }
            }
        }
    }
}
