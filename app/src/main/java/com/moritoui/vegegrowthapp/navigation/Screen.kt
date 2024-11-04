package com.moritoui.vegegrowthapp.navigation

sealed class Screen(val route: String) {
    data object HomeScreen : Screen("homeScreen")

    data object FolderScreen : Screen("folderScreen")

    data object TakePictureScreen : Screen("takePictureScreen")

    data object ManageScreen : Screen("manageScreen")
    data object ManualScreen : Screen("manualScreen")
    data object InitialManualScreen : Screen("initialManualScreen")

    data object CommunityHomeScreen : Screen("communityHomeScreen")
}
