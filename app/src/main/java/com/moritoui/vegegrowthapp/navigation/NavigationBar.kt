package com.moritoui.vegegrowthapp.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.moritoui.vegegrowthapp.ui.FirstScreen
import com.moritoui.vegegrowthapp.ui.ManageScreen
import com.moritoui.vegegrowthapp.ui.ManageScreenViewModel
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
        composable(
            "${Screen.TakePictureScreen.route}/{index}",
            arguments = listOf(navArgument("index") { type = NavType.IntType })
        ) { backStackEntry ->
            TakePicScreen(
                index = backStackEntry.arguments?.getInt("index") ?: 0,
                navController = navController
            )
        }
        composable(
            "${Screen.ManageVegeScreen.route}/{index}",
            arguments = listOf(navArgument("index") { type = NavType.IntType })
        ) { backStackEntry ->
            ManageScreen(
                navController = navController,
                viewModel = ManageScreenViewModel(
                    index = backStackEntry.arguments?.getInt("index") ?: 0,
                    applicationContext = LocalContext.current.applicationContext
                )
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
    onNavigationIconClick: () -> Unit,
    onCanselIconClick: () -> Unit,
    isDeleteMode: Boolean,
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
                Row() {
                    if (!isDeleteMode) {
                        Icon(
                            Icons.Filled.Info,
                            contentDescription = null,
                            tint = Color.Transparent
                        )
                        IconButton(onClick = { onNavigationIconClick() }) {
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = "削除"
                            )
                        }
                    } else {
                        Row() {
                            IconButton(onClick = {
                                onCanselIconClick()
                                onNavigationIconClick()
                            }) {
                                Icon(
                                    Icons.Filled.Close,
                                    contentDescription = "キャンセル"
                                )
                            }
                            IconButton(onClick = { onNavigationIconClick() }) {
                                Icon(
                                    Icons.Filled.Delete,
                                    contentDescription = "削除",
                                    tint = Color.Red
                                )
                            }
                        }
                    }
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
@Preview
@Composable
fun NavigationAppTopBarPreview() {
    NavigationAppTopBar(
        navController = rememberNavController(),
        title = "preview",
        isVisibleNavigationButton = true
    )
}
