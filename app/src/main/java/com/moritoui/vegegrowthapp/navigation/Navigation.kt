package com.moritoui.vegegrowthapp.navigation

import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.moritoui.vegegrowthapp.ui.FirstScreen

sealed class Screen(val route: String) {
    object FirstScreen : Screen("firstScreen")
    object TakePictureScreen : Screen("takePictureScreen")
    object ManageVegeScreen : Screen("manageVegeScreen")
}

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.FirstScreen.route
    ) {
        composable(Screen.FirstScreen.route) {
            FirstScreen(navController = navController)
        }
        composable(Screen.TakePictureScreen.route) {
            Text("takePicture", modifier = Modifier.safeDrawingPadding())
        }
        composable(Screen.ManageVegeScreen.route) {
            Text("manageView", modifier = Modifier.safeDrawingPadding())
        }
    }
}
