package com.moritoui.vegegrowthapp.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * 基礎のレイアウト
 */
@Composable
fun BaseLayout(modifier: Modifier = Modifier, bottomBar: @Composable () -> Unit = {}, topBar: @Composable () -> Unit = {}, content: @Composable (PaddingValues) -> Unit) {
    Scaffold(
        modifier = modifier,
        bottomBar = bottomBar,
        topBar = topBar,
        content = content
    )
}
