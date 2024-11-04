package com.moritoui.vegegrowthapp.ui.community.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object TimelineScreenRoute {
    const val SCREEN = "timelineScreen"
}

fun NavGraphBuilder.timelineScreenRoute(navController: NavController) {
    composable<TimelineScreenRoute> {
        TimelineScreen(navController = navController)
    }
}

fun NavController.navigateToTimeline() {
    navigate(TimelineScreenRoute) {
        launchSingleTop = true
    }
}
