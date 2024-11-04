package com.moritoui.vegegrowthapp.ui.community.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.moritoui.vegegrowthapp.navigation.Screen

fun NavGraphBuilder.communityHomeScreenRoute(navController: NavController) {
    composable(Screen.CommunityHomeScreen.route) {
        CommunityHomeScreen(navController = navController)
    }
}

fun NavController.navigateToCommunityHome() {
    navigate(Screen.CommunityHomeScreen.route) {
        launchSingleTop = true
    }
}
