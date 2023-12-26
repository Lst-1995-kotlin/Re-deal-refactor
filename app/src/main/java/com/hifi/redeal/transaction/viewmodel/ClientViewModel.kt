package com.hifi.redeal.transaction.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hifi.redeal.transaction.model.Client
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor() : ViewModel() {

    val selectedClient = MutableLiveData<Client>()

    fun clickedClient(client: Client) {
        selectedClient.postValue(client)
    }
}
