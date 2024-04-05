package com.hifi.redeal.memo.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.hifi.redeal.memo.components.PhotoMemoEntryDestination
import com.hifi.redeal.memo.model.PhotoMemo
import com.hifi.redeal.memo.repository.PhotoMemosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhotoMemoEntryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val photoMemosRepository: PhotoMemosRepository
) : ViewModel() {

    private val clientId: Int =
        checkNotNull(savedStateHandle[PhotoMemoEntryDestination.clientIdArg])
    var photoMemoUiState by mutableStateOf(PhotoMemoUiState(PhotoMemo(clientOwnerId = clientId)))
        private set

    fun updateUiState(photoMemo: PhotoMemo) {
        photoMemoUiState =
            PhotoMemoUiState(photoMemo = photoMemo, isEntryValid = validateInput(photoMemo))
    }

    suspend fun savePhotoMemo() {
        if (validateInput()) {
            updateUiState(photoMemoUiState.photoMemo.copy(
                timestamp = System.currentTimeMillis()
            ))
            photoMemosRepository.insertPhotoMemo(photoMemoUiState.photoMemo)
        }
    }

    private fun validateInput(uiState: PhotoMemo = photoMemoUiState.photoMemo): Boolean {
        return with(uiState) {
            memo.isNotBlank() && imageUris.isNotEmpty()
        }
    }
}

data class PhotoMemoUiState(
    val photoMemo: PhotoMemo,
    val isEntryValid: Boolean = false
)