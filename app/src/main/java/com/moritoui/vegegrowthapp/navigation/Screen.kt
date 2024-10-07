package com.moritoui.vegegrowthapp.navigation

sealed class Screen(val route: String) {
    object HomeScreen : Screen("homeScreen")

    object FolderScreen : Screen("folderScreen")

    object TakePictureScreen : Screen("takePictureScreen")

    object ManageScreen : Screen("manageScreen")
    object ManualScreen : Screen("manualScreen")
    object InitialManualScreen : Screen("initialManualScreen")
}
