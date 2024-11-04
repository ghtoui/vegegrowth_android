package com.moritoui.vegegrowthapp.ui.timeline.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object TimelineHomeScreenRoute {
    const val SCREEN = "timelineHomeScreen"
}

fun NavGraphBuilder.timelineHomeScreenRoute(navController: NavController) {
    composable<TimelineHomeScreenRoute> {
        TimelineHomeScreen(navController = navController)
    }
}

fun NavController.navigateToTimelineHome() {
    navigate(TimelineHomeScreenRoute) {
        launchSingleTop = true
    }
}
