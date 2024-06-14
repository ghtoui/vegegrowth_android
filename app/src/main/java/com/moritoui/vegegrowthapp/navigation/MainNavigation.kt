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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.moritoui.vegegrowthapp.ui.home.homeScreenRoute
import com.moritoui.vegegrowthapp.ui.manage.manageScreenRoute
import com.moritoui.vegegrowthapp.ui.takepicture.takePictureScreenRoute

sealed class Screen(
    val route: String,
) {
    object HomeScreen : Screen("homeScreen")

    object TakePictureScreen : Screen("takePictureScreen")

    object ManageScreen : Screen("manageScreen")
}

@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.HomeScreen.route,
    ) {
        homeScreenRoute(navController)
        takePictureScreenRoute(navController)
        manageScreenRoute(navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationAppTopBar(
    isVisibleNavigationButton: Boolean = true,
    onBackNavigationButtonClick: () -> Unit,
    title: String,
    actions: @Composable () -> Unit = { },
) {
    NavigationBar {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackNavigationButtonClick) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = "戻る",
                    )
                }
            },
            actions = {
                if (isVisibleNavigationButton) {
                    actions()
                }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirstNavigationAppTopBar(
    isVisibleNavigationButton: Boolean = true,
    title: String,
    actions: @Composable () -> Unit = { },
) {
    NavigationBar {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                )
            },
            navigationIcon = {
            },
            actions = {
                if (isVisibleNavigationButton) {
                    actions()
                }
            },
        )
    }
}

@Preview
@Composable
fun NavigationAppTopBarPreview() {
    NavigationAppTopBar(
        title = "preview",
        isVisibleNavigationButton = true,
        onBackNavigationButtonClick = {},
    )
}