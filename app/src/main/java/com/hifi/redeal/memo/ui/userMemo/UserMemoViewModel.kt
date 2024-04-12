package com.hifi.redeal.memo.ui.userMemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hifi.redeal.memo.repository.PhotoMemosRepository
import com.hifi.redeal.memo.repository.RecordMemosRepository
import com.hifi.redeal.memo.ui.photoMemo.PhotoMemosUiState
import com.hifi.redeal.memo.ui.recordMemo.RecordMemosUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class UserMemoViewModel @Inject constructor(
    photoMemosRepository: PhotoMemosRepository,
    recordMemosRepository: RecordMemosRepository
):ViewModel() {
    val photoMemosUiState: StateFlow<PhotoMemosUiState> =
        photoMemosRepository.getPhotoMemos()
            .map {
                PhotoMemosUiState(it)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = PhotoMemosUiState()
            )

    val recordMemosUiState: StateFlow<RecordMemosUiState> =
        recordMemosRepository.getRecordMemos()
            .map {
                RecordMemosUiState(it)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = RecordMemosUiState()
            )
}