package com.moritoui.vegegrowthapp.ui.manual

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.moritoui.vegegrowthapp.navigation.Screen

fun NavGraphBuilder.manualScreenRoute(navController: NavController) {
    composable(
        Screen.ManualScreen.route
    ) {
        ManualScreen(navController = navController)
    }
}

fun NavController.navigateToManual() {
    navigate(Screen.ManualScreen.route) {
        launchSingleTop = true
    }
}
