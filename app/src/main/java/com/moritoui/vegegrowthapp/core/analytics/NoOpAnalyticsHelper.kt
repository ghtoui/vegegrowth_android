package com.moritoui.vegegrowthapp.core.analytics

import com.moritoui.vegegrowthapp.navigation.Screen

class NoOpAnalyticsHelper : AnalyticsHelper {
    override fun logScreen(screen: Screen) = Unit

    override fun logEvent(event: AnalyticsEvent) = Unit
}
