package com.moritoui.vegegrowthapp.ui.manual

import androidx.lifecycle.ViewModel
import com.moritoui.vegegrowthapp.core.analytics.AnalyticsEvent
import com.moritoui.vegegrowthapp.core.analytics.AnalyticsHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ManualViewModel @Inject constructor(private val analytics: AnalyticsHelper) : ViewModel() {
    fun finishedLookingPage(page: Int) {
        analytics.logEvent(AnalyticsEvent.Analytics.lookManualPage("$page"))
    }
}
