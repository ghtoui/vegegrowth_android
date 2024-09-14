package com.moritoui.vegegrowthapp.ui.main

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moritoui.vegegrowthapp.data.datastores.appsettings.AppSettingsPreferences
import com.moritoui.vegegrowthapp.ui.main.model.MainState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val appSettingsPreferences: DataStore<AppSettingsPreferences>
) : ViewModel() {
    private val _mainState = MutableStateFlow(MainState.initial())
    val mainState: StateFlow<MainState> = _mainState.asStateFlow()

    init {
        appStart()
    }

    private fun appStart() {
        viewModelScope.launch {
            _mainState.update {
                it.copy(
                    isLoading = true
                )
            }
            _mainState.update {
                it.copy(
                    isInitialStartApp = appSettingsPreferences.data.first().isInitialStartApp,
                    isLoading = false,
                )
            }
        }
    }
}
