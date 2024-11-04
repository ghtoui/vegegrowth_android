package com.moritoui.vegegrowthapp.ui.manage

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data class ManageScreenRoute(val vegetableId: Int) {
    companion object {
        const val SCREEN = "manageScreen"
    }
}

fun NavGraphBuilder.manageScreenRoute(navController: NavController) {
    composable<ManageScreenRoute> {
        ManageScreen(navController = navController)
    }
}

fun NavController.navigateToManage(vegetableId: Int) {
    navigate(ManageScreenRoute(vegetableId = vegetableId)) {
        launchSingleTop = true
    }
}
