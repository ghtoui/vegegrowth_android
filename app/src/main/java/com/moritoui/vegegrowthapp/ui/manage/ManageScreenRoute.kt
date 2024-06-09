package com.moritoui.vegegrowthapp.ui.manage

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.moritoui.vegegrowthapp.navigation.Screen

fun NavGraphBuilder.manageScreenRoute(
    navController: NavController,
) {
    composable(
        "${Screen.ManageScreen.route}/{vegetableId}",
        // Intで受け取るように
        arguments = listOf(navArgument("vegetableId") { type = NavType.IntType })
    ) {

        it.arguments?.getString("vegetableId")
        ManageScreen(navController = navController)
    }
}

fun NavController.navigateToManage(vegetableId: Int) {
    navigate("${Screen.ManageScreen.route}/$vegetableId")
}
