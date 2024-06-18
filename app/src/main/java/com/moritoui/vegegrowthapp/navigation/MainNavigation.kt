package com.moritoui.vegegrowthapp.navigation

import android.os.Bundle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
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
    firebaseAnalytics: FirebaseAnalytics,
    navController: NavHostController = rememberNavController(),
) {
    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val route = destination.route
            if (route != null) {
                firebaseEventSend(firebaseAnalytics, route)
            }
        }
    }
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
    isVisibleBackButton: Boolean = true,
    onBackNavigationButtonClick: () -> Unit = {},
    title: String,
    actions: @Composable () -> Unit = { },
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    NavigationBar {
        TopAppBar(
            title = {
                Text(
                    text = title,
                )
            },
            navigationIcon = {
                if (isVisibleBackButton) {
                    IconButton(onClick = onBackNavigationButtonClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "戻る",
                        )
                    }
                }
            },
            actions = {
                actions()
            },
            scrollBehavior = scrollBehavior,
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
        isVisibleBackButton = true,
        onBackNavigationButtonClick = {},
    )
}

private fun firebaseEventSend(
    firebaseAnalytics: FirebaseAnalytics,
    screenName: String,
) {
    // ユーザが画面遷移をしたときにログを取る
    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
        Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            putString(FirebaseAnalytics.Param.SCREEN_CLASS, "MainNavigation")
        }
    }
}
