package com.moritoui.vegegrowthapp.ui.initialmanual

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moritoui.vegegrowthapp.core.analytics.AnalyticsEvent
import com.moritoui.vegegrowthapp.core.analytics.AnalyticsHelper
import com.moritoui.vegegrowthapp.data.datastores.appsettings.AppSettingsPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InitialManualViewModel @Inject constructor(
    private val appSettingsPreferences: DataStore<AppSettingsPreferences>,
    private val analytics: AnalyticsHelper,
    ) : ViewModel() {
    fun finishReadManual() {
        viewModelScope.launch {
            appSettingsPreferences.updateData {
                it.copy(
                    isInitialStartApp = false
                )
            }
        }
    }

    fun finishedLookingPage(
        page: Int
    ) {
        analytics.logEvent(AnalyticsEvent.Analytics.lookManualPage("$page"))
    }
}
