package com.hifi.redeal.memo.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.hifi.redeal.memo.model.PhotoMemo
import com.hifi.redeal.memo.repository.PhotoMemosRepository
import javax.inject.Inject

class PhotoMemoEntryViewModel @Inject constructor(
    private val photoMemosRepository: PhotoMemosRepository
):ViewModel() {
    var photoMemoUiState by mutableStateOf(PhotoMemoUiState())
        private set

    fun updateUiState(photoMemo: PhotoMemo){
        photoMemoUiState =
            PhotoMemoUiState(photoMemo = photoMemo, isEntryValid = validateInput(photoMemo))
    }
    suspend fun savePhotoMemo(){
        if(validateInput()){
            photoMemosRepository.insertPhotoMemo(photoMemoUiState.photoMemo)
        }
    }
    private fun validateInput(uiState: PhotoMemo = photoMemoUiState.photoMemo): Boolean{
        return with(uiState){
            memo.isNotBlank() && imageUris.isNotEmpty()
        }
    }
}

data class PhotoMemoUiState(
    val photoMemo: PhotoMemo = PhotoMemo(),
    val isEntryValid: Boolean = false
)