package com.moritoui.vegegrowthapp.ui.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object HomeScreenRoute {
    const val SCREEN = "homeScreen"
}

fun NavGraphBuilder.homeScreenRoute(navController: NavController) {
    composable<HomeScreenRoute> {
        HomeScreen(navController = navController)
    }
}

fun NavController.navigateToHome() {
    navigate(HomeScreenRoute) {
        launchSingleTop = true
    }
}
