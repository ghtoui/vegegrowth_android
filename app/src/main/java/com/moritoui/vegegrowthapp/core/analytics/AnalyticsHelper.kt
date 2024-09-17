package com.moritoui.vegegrowthapp.core.analytics

import com.moritoui.vegegrowthapp.navigation.Screen

interface AnalyticsHelper {
    fun logScreen(screen: Screen)
    fun logEvent(event: AnalyticsEvent)
}
