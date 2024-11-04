package com.moritoui.vegegrowthapp.core.analytics

interface AnalyticsHelper {
    fun logScreen(screen: String)
    fun logEvent(event: AnalyticsEvent)
}
