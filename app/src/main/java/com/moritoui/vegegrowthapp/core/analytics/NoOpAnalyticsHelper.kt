package com.moritoui.vegegrowthapp.core.analytics

class NoOpAnalyticsHelper : AnalyticsHelper {
    override fun logScreen(screen: String) = Unit

    override fun logEvent(event: AnalyticsEvent) = Unit
}
