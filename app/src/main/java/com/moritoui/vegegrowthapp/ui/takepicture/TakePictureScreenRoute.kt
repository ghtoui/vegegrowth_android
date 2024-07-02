package com.moritoui.vegegrowthapp.ui.takepicture

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.moritoui.vegegrowthapp.navigation.Screen

fun NavGraphBuilder.takePictureScreenRoute(navController: NavController) {
    composable(
        "${Screen.TakePictureScreen.route}/{vegetableId}",
        // Intで受け取るように
        arguments = listOf(navArgument("vegetableId") { type = NavType.IntType })
    ) {
        it.arguments?.getString("vegetableId")
        TakePictureScreen(navController = navController)
    }
}

fun NavController.navigateToTakePicture(vegetableId: Int) {
    navigate("${Screen.TakePictureScreen.route}/$vegetableId") {
        launchSingleTop = true
    }
}
