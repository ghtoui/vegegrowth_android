package com.moritoui.vegegrowthapp.model

sealed class Screen(val route: String) {
    object FirstScreen : Screen("firstScreen")
    object TakePictureScreen : Screen("takePictureScreen")
    object ManageVegeScreen : Screen("manageVegeScreen")
}

