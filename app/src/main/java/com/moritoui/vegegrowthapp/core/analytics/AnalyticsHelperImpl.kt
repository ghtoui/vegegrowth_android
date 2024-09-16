package com.moritoui.vegegrowthapp.core.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.moritoui.vegegrowthapp.navigation.Screen
import timber.log.Timber
import javax.inject.Inject

class AnalyticsHelperImpl @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) : AnalyticsHelper {
    override fun logScreen(screen: Screen) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            Timber.d("screenLog: ${screen.route}")
            param(FirebaseAnalytics.Param.SCREEN_NAME, screen.route)
        }
    }

    override fun logEvent(event: AnalyticsEvent) {
        firebaseAnalytics.logEvent(event.event) {
            Timber.d("eventLog: event=${event.event} param=${event.params}")
            event.params.forEach {
                param(it.key, it.value)
            }
        }
    }
}
