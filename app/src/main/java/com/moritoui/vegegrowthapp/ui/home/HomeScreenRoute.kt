package com.moritoui.vegegrowthapp.ui.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.moritoui.vegegrowthapp.navigation.Screen

fun NavGraphBuilder.homeScreenRoute(navController: NavController) {
    composable(Screen.HomeScreen.route) {
        HomeScreen(navController = navController)
    }
}

fun NavController.navigateToHome() {
    navigate(Screen.HomeScreen.route) {
        launchSingleTop = true
    }
}
