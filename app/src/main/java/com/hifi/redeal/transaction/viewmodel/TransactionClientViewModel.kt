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
class TransactionClientViewModel @Inject constructor(
    private val clientRepository: ClientRepository,
) : ViewModel() {


    private val _selectedClient = MutableLiveData<Client?>()
    private val _clients = MutableLiveData<List<Client>>()
    val clients: LiveData<List<Client>> get() = _clients
    val selectedClient: LiveData<Client?> get() = _selectedClient
    init {
        getUserAllClient()
    }

    fun setSelectClient(client: Client) {
        _selectedClient.postValue(client)
    }

    fun setSelectClientIndex(clientIndex: Long?) {
        clientIndex?.let {
            clientRepository.getIndexOfClient(it) {
                for (c1 in it.result) {
                    val client = Client(
                        ClientData(
                            c1["clientIdx"] as Long,
                            c1["clientName"] as String,
                            c1["clientManagerName"] as String,
                            c1["clientState"] as Long,
                            c1["isBookmark"] as Boolean
                        )
                    )
                    setSelectClient(client)
                }
            }
        }?: _selectedClient.postValue(null)
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
                    _clients.postValue(temp)
                }
            }
        }
    }
}