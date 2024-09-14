package com.moritoui.vegegrowthapp.ui.initialmanual

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.moritoui.vegegrowthapp.navigation.Screen

fun NavGraphBuilder.initialManualScreenRoute(navController: NavController) {
    composable(
        Screen.InitialManualScreen.route
    ) {
        InitialManualScreen(navController = navController)
    }
}

fun NavController.navigateToInitialManual() {
    navigate(Screen.InitialManualScreen.route) {
        launchSingleTop = true
    }
}
