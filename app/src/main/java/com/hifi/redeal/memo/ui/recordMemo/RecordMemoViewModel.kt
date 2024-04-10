package com.hifi.redeal.memo.ui.recordMemo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hifi.redeal.memo.model.RecordMemo
import com.hifi.redeal.memo.repository.RecordMemosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RecordMemoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    recordMemosRepository: RecordMemosRepository
) : ViewModel() {

    private val clientId: Int = checkNotNull(savedStateHandle[RecordMemoDestination.clientId])
    val recordMemosUiState: StateFlow<RecordMemosUiState> =
        recordMemosRepository.getClientWithRecordMemos(clientId)
            .map {
                RecordMemosUiState(it)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = RecordMemosUiState()
            )
}

data class RecordMemosUiState(val recordMemos: List<RecordMemo> = emptyList())