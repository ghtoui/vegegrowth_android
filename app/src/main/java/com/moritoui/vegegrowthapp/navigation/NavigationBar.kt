package com.moritoui.vegegrowthapp.navigation

import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.moritoui.vegegrowthapp.ui.FirstScreen
import com.moritoui.vegegrowthapp.ui.TakePicScreen

sealed class Screen(
    val route: String,
) {
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
            TakePicScreen(navController = navController)
        }
        composable(Screen.ManageVegeScreen.route) {
            Text("manageView", modifier = Modifier.safeDrawingPadding())
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationAppTopBar(
    navController: NavHostController,
    title: String,
    actions: @Composable () -> Unit = { }
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    NavigationBar {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = title,
                    textAlign = TextAlign.Center
                )
            },
            navigationIcon = {
                if (navBackStackEntry?.destination?.route != Screen.FirstScreen.route) {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "戻る"
                        )
                    }
                }
            },
            actions = {
                actions()
            }
        )
    }
}
