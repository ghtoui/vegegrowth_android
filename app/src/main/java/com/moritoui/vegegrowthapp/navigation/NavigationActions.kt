package com.moritoui.vegegrowthapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
fun AddItem(onAddClick: () -> Unit) {
    IconButton(onClick = { onAddClick() }) {
        Icon(
            Icons.Filled.Add,
            contentDescription = "追加"
        )
    }
}
