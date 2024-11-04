package com.moritoui.vegegrowthapp.ui.community.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object CommunityHomeScreenRoute {
    const val SCREEN = "communityHomeScreen"
}

fun NavGraphBuilder.communityHomeScreenRoute(navController: NavController) {
    composable<CommunityHomeScreenRoute> {
        CommunityHomeScreen(navController = navController)
    }
}

fun NavController.navigateToCommunityHome() {
    navigate(CommunityHomeScreenRoute) {
        launchSingleTop = true
    }
}
