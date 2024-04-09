package com.hifi.redeal.memo.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.hifi.redeal.memo.components.RecordMemoEntryDestination
import com.hifi.redeal.memo.model.RecordMemo
import com.hifi.redeal.memo.repository.RecordMemosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class RecordMemoEntryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val recordMemosRepository: RecordMemosRepository
) : ViewModel() {
    private val clientId: Int = checkNotNull(savedStateHandle[RecordMemoEntryDestination.clientId])
    var recordMemoUiState by mutableStateOf(
        RecordMemoUiState(
            RecordMemo(
                audioFilename = SimpleDateFormat(
                    "yyyyMMdd_HHmm ss",
                    Locale.getDefault()
                ).format(Date()),
                clientOwnerId = clientId
            ), isEntryValid = false
        )
    )
        private set

    fun updateUiState(recordMemo: RecordMemo) {
        recordMemoUiState =
            RecordMemoUiState(recordMemo = recordMemo, isEntryValid = validateInput())
    }

    suspend fun saveRecordMemo() {
        if (validateInput()) {
            updateUiState(
                recordMemoUiState.recordMemo.copy(
                    timestamp = System.currentTimeMillis()
                )
            )
            recordMemosRepository.insertRecordMemo(recordMemoUiState.recordMemo)
        }
    }

    private fun validateInput(uiState: RecordMemo = recordMemoUiState.recordMemo): Boolean {
        return with(uiState) {
            memo.isNotBlank() && audioFileUri.isNotBlank()
        }
    }
}

data class RecordMemoUiState(
    val recordMemo: RecordMemo,
    val isEntryValid: Boolean
)