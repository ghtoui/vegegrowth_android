package com.moritoui.vegegrowthapp.ui.manual

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object ManualScreenRoute {
    const val SCREEN = "manualScreen"
}

fun NavGraphBuilder.manualScreenRoute(navController: NavController) {
    composable<ManualScreenRoute> {
        ManualScreen(navController = navController)
    }
}

fun NavController.navigateToManual() {
    navigate(ManualScreenRoute) {
        launchSingleTop = true
    }
}
