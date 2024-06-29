package com.moritoui.vegegrowthapp.ui.folder

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.moritoui.vegegrowthapp.navigation.Screen

fun NavGraphBuilder.folderScreenRoute(navController: NavController) {
    composable(
        "${Screen.FolderScreen.route}/{folderId}",
        arguments = listOf(navArgument("folderId") { type = NavType.IntType} )
    ) {
        FolderScreen(navController = navController)
    }
}

fun NavController.navigateToFolder(folderId: Int) {
    navigate("${Screen.FolderScreen.route}/$folderId")
}
