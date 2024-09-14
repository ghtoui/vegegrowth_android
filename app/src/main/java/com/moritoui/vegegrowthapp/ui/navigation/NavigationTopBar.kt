package com.moritoui.vegegrowthapp.ui.navigation

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationAppTopBar(
    modifier: Modifier = Modifier,
    onBackNavigationButtonClick: () -> Unit = {},
    title: String,
    actions: @Composable () -> Unit = { },
    navigationIcon: @Composable () -> Unit = { BackNavigationIconButton(onBackNavigationButtonClick = onBackNavigationButtonClick) },
) {
    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            titleContentColor = MaterialTheme.colorScheme.secondary
        ),
        title = {
            Text(
                text = title
            )
        },
        navigationIcon = navigationIcon,
        actions = {
            actions()
        }
    )
}

@Composable
fun BackNavigationIconButton(onBackNavigationButtonClick: () -> Unit) {
    IconButton(onClick = onBackNavigationButtonClick) {
        Icon(
            Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "戻る",
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}

@Preview
@Composable
fun NavigationAppTopBarPreview() {
    NavigationAppTopBar(
        title = "preview",
        onBackNavigationButtonClick = {}
    )
}
