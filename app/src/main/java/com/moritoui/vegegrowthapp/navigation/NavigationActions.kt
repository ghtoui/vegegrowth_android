package com.moritoui.vegegrowthapp.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.moritoui.vegegrowthapp.R

@Composable
fun HomeAddItem(onFolderAddClick: () -> Unit, onAddClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onFolderAddClick) {
            Icon(
                painterResource(R.drawable.ic_folder_create),
                contentDescription = stringResource(id = R.string.description_folder_add),
                tint = MaterialTheme.colorScheme.secondary
            )
        }
        IconButton(onClick = onAddClick) {
            Icon(
                Icons.Filled.Add,
                contentDescription = stringResource(id = R.string.description_item_add),
                tint = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun GoToManageItem(onNavigateClick: () -> Unit) {
    TextButton(
        onClick = onNavigateClick
    ) {
        Text(
            text = "管理画面",
            color = MaterialTheme.colorScheme.secondary
        )
    }
}
