package com.moritoui.vegegrowthapp.ui.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(

) : ViewModel() {
    private val _isInitial = MutableStateFlow(false)
    val isInitial: StateFlow<Boolean> = _isInitial.asStateFlow()

    init {
        _isInitial.update { true }
    }
}
