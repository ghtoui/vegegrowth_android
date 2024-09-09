package com.moritoui.vegegrowthapp.ui.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.moritoui.vegegrowthapp.navigation.Screen
import com.moritoui.vegegrowthapp.ui.startup.StartupScreen

fun NavGraphBuilder.homeScreenRoute(navController: NavController) {
    composable(Screen.HomeScreen.route) {
//        HomeScreen(navController = navController)
        StartupScreen()
    }
}
//
// private fun setupHomeScreenTracking() {
//    var params = Bundle()
//    params.putString(FirebaseAnalytics.Param.SCREEN_NAME, destination.label as String?)
//    params.putString(FirebaseAnalytics.Param.SCREEN_CLASS, destination.label as String?)
//    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, params)
// }
