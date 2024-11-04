package com.moritoui.vegegrowthapp.ui.folder

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data class FolderScreenRoute(val folderId: Int) {
    companion object {
        const val SCREEN = "folderScreen"
    }
}

fun NavGraphBuilder.folderScreenRoute(navController: NavController) {
    composable<FolderScreenRoute> {
        FolderScreen(navController = navController)
    }
}

fun NavController.navigateToFolder(folderId: Int) {
    navigate(FolderScreenRoute(folderId = folderId)) {
        launchSingleTop = true
    }
}
