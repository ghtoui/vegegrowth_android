package com.moritoui.vegegrowthapp.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class FirstScreenUiState (
    val isOpenDialog: Boolean = false,
    val inputText: String = ""
)
class FirstScreenViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(FirstScreenUiState())
    val uiState: StateFlow<FirstScreenUiState> = _uiState.asStateFlow()

    fun updateState(isOpenDialog: Boolean = _uiState.value.isOpenDialog,
                    inputText: String = _uiState.value.inputText) {
        _uiState.update { currentState ->
            currentState.copy(
                isOpenDialog = isOpenDialog,
                inputText = inputText
            )
        }
    }
}
