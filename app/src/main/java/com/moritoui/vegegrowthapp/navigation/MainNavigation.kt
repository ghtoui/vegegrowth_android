package com.moritoui.vegegrowthapp.navigation

import android.os.Bundle
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.moritoui.vegegrowthapp.ui.folder.folderScreenRoute
import com.moritoui.vegegrowthapp.ui.home.homeScreenRoute
import com.moritoui.vegegrowthapp.ui.manage.manageScreenRoute
import com.moritoui.vegegrowthapp.ui.takepicture.takePictureScreenRoute

sealed class Screen(val route: String) {
    object HomeScreen : Screen("homeScreen")

    object FolderScreen : Screen("folderScreen")

    object TakePictureScreen : Screen("takePictureScreen")

    object ManageScreen : Screen("manageScreen")
}

@Composable
fun MainNavigation(modifier: Modifier = Modifier, firebaseAnalytics: FirebaseAnalytics, navController: NavHostController = rememberNavController()) {
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
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { screenWidth -> screenWidth },
                animationSpec = tween()
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { screenWidth -> -screenWidth },
                animationSpec = tween()
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { screenWidth -> -screenWidth },
                animationSpec = tween()
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { screenWidth -> screenWidth },
                animationSpec = tween()
            )
        }
    ) {
        homeScreenRoute(navController)
        takePictureScreenRoute(navController)
        manageScreenRoute(navController)
        folderScreenRoute(navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationAppTopBar(isVisibleBackButton: Boolean = true, onBackNavigationButtonClick: () -> Unit = {}, title: String, actions: @Composable () -> Unit = { }) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            titleContentColor = MaterialTheme.colorScheme.secondary
        ),
        title = {
            Text(
                text = title
            )
        },
        navigationIcon = {
            if (isVisibleBackButton) {
                IconButton(onClick = onBackNavigationButtonClick) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "戻る",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        },
        actions = {
            actions()
        }
    )
}

@Preview
@Composable
fun NavigationAppTopBarPreview() {
    NavigationAppTopBar(
        title = "preview",
        isVisibleBackButton = true,
        onBackNavigationButtonClick = {}
    )
}

private fun firebaseEventSend(firebaseAnalytics: FirebaseAnalytics, screenName: String) {
    // ユーザが画面遷移をしたときにログを取る
    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
        Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            putString(FirebaseAnalytics.Param.SCREEN_CLASS, "MainNavigation")
        }
    }
}
