package com.moritoui.vegegrowthapp.ui.takepicture

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data class TakePictureScreenRoute(
    val vegetableId: Int
) {
    companion object {
        const val SCREEN = "takePictureScreen"
    }
}

fun NavGraphBuilder.takePictureScreenRoute(navController: NavController) {
    composable<TakePictureScreenRoute> {
        TakePictureScreen(navController = navController)
    }
}

fun NavController.navigateToTakePicture(vegetableId: Int) {
    navigate(TakePictureScreenRoute(vegetableId = vegetableId)) {
        launchSingleTop = true
    }
}
