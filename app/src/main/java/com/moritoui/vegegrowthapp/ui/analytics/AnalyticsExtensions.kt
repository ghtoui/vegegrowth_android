package com.moritoui.vegegrowthapp.ui.analytics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.moritoui.vegegrowthapp.core.analytics.AnalyticsHelper
import com.moritoui.vegegrowthapp.core.analytics.LocalAnalyticsHelper
import com.moritoui.vegegrowthapp.navigation.Screen

@Composable
fun SendScreenEvent(
    screen: Screen,
    analyticsHelper: AnalyticsHelper = LocalAnalyticsHelper.current
) {
    val owner = LocalLifecycleOwner.current
    // onResumeでイベントを送る
    DisposableEffect(owner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                analyticsHelper.logScreen(screen)
            }
        }
        owner.lifecycle.addObserver(observer)
        onDispose { owner.lifecycle.removeObserver(observer) }
    }
}
