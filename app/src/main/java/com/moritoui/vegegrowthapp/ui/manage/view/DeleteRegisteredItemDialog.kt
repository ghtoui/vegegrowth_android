package com.moritoui.vegegrowthapp.ui.manage.view

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.moritoui.vegegrowthapp.R

@Composable
fun DeleteRegisteredItemDialog(
    onDismissDeleteDialog: () -> Unit,
    onDeleteDialogConfirmClick: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissDeleteDialog,
        dismissButton = {
            TextButton(onClick = onDismissDeleteDialog) {
                Text(stringResource(id = R.string.cancel_text))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteDialogConfirmClick) {
                Text(
                    text = stringResource(id = R.string.delete_text),
                    color = MaterialTheme.colorScheme.error,
                )
            }
        },
        title = {
            Text(
                text = stringResource(id = R.string.manage_delete_item_dialog_title),
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            Text(
                text = stringResource(id = R.string.manage_delete_item_dialog_sub_text),
                style = MaterialTheme.typography.labelLarge,
            )
        }
    )
}
