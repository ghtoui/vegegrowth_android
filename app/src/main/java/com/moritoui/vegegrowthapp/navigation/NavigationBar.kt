package com.moritoui.vegegrowthapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.moritoui.vegegrowthapp.ui.FirstScreen
import com.moritoui.vegegrowthapp.ui.ManageScreen
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
    val context = LocalContext.current.applicationContext
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.FirstScreen.route
    ) {
        composable(Screen.FirstScreen.route) {
            FirstScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }
        composable(
            "${Screen.TakePictureScreen.route}/{index}/{sortText}",
            arguments = listOf(
                navArgument("index") { type = NavType.IntType },
                navArgument("sortText") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            TakePicScreen(
                sortText = backStackEntry.arguments?.getString("sortText") ?: "All",
                navController = navController,
                viewModel = hiltViewModel()
            )
        }
        composable(
            "${Screen.ManageVegeScreen.route}/{index}/{sortText}",
            arguments = listOf(
                navArgument("index") { type = NavType.IntType },
                navArgument("sortText") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            ManageScreen(
                navController = navController,
                viewModel = hiltViewModel()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationAppTopBar(
    isVisibleNavigationButton: Boolean = true,
    navController: NavHostController,
    title: String,
    actions: @Composable () -> Unit = { }
) {
    NavigationBar {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = title,
                    textAlign = TextAlign.Center
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = "戻る"
                    )
                }
            },
            actions = {
                if (isVisibleNavigationButton) {
                    actions()
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirstNavigationAppTopBar(
    isVisibleNavigationButton: Boolean = true,
    title: String,
    actions: @Composable () -> Unit = { }
) {
    NavigationBar {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = title,
                    textAlign = TextAlign.Center
                )
            },
            navigationIcon = {
            },
            actions = {
                if (isVisibleNavigationButton) {
                    actions()
                }
            }
        )
    }
}
@Preview
@Composable
fun NavigationAppTopBarPreview() {
    NavigationAppTopBar(
        navController = rememberNavController(),
        title = "preview",
        isVisibleNavigationButton = true
    )
}
