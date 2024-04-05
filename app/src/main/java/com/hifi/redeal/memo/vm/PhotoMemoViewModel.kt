package com.hifi.redeal.memo.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hifi.redeal.memo.components.PhotoMemoDestination
import com.hifi.redeal.memo.model.PhotoMemo
import com.hifi.redeal.memo.repository.PhotoMemosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PhotoMemoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    photoMemosRepository: PhotoMemosRepository
) : ViewModel() {

    private val clientId: Int = checkNotNull(savedStateHandle[PhotoMemoDestination.clientIdArg])

    val photoMemosUiState: StateFlow<PhotoMemosUiState> =
        photoMemosRepository.getClientWithPhotoMemos(clientId)
            .map {
                PhotoMemosUiState(it)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = PhotoMemosUiState()
            )
}

data class PhotoMemosUiState(val photoMemos: List<PhotoMemo> = listOf())