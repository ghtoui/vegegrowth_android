package com.moritoui.vegegrowthapp.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddItem(onAddClick: () -> Unit) {
    IconButton(onClick = { onAddClick() }) {
        Icon(
            Icons.Filled.Add,
            contentDescription = "追加",
        )
    }
}

@Composable
fun NavigateItem(onNavigateClick: () -> Unit) {
    Box(
        modifier =
            Modifier
                .fillMaxHeight()
                .padding(top = 4.dp, end = 12.dp)
                .clickable(onClick = { onNavigateClick() }),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "管理画面",
        )
    }
}
