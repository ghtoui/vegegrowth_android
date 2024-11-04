package com.moritoui.vegegrowthapp.ui.initialmanual

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object InitialManualScreenRoute {
    const val SCREEN = "initialManualScreen"
}

fun NavGraphBuilder.initialManualScreenRoute(navController: NavController) {
    composable<InitialManualScreenRoute> {
        InitialManualScreen(navController = navController)
    }
}

fun NavController.navigateToInitialManual() {
    navigate(InitialManualScreenRoute) {
        launchSingleTop = true
    }
}
