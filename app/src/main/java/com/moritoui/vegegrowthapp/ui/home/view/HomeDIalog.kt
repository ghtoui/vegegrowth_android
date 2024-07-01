package com.moritoui.vegegrowthapp.ui.home.view

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moritoui.vegegrowthapp.R
import com.moritoui.vegegrowthapp.data.room.model.VegetableFolderEntity
import com.moritoui.vegegrowthapp.model.VegeCategory
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegeStatus
import com.moritoui.vegegrowthapp.model.VegeStatusMethod
import com.moritoui.vegegrowthapp.previews.DarkLightPreview
import com.moritoui.vegegrowthapp.ui.theme.VegegrowthAppTheme
import kotlinx.coroutines.flow.Flow

@Composable
fun AddTextCategoryDialog(
    @StringRes titleResId: Int,
    selectCategory: VegeCategory,
    inputText: String,
    isAddAble: Boolean,
    onValueChange: (String) -> Unit,
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit,
    onSelectVegeCategory: (VegeCategory) -> Unit,
    onDismissRequest: () -> Unit = {},
    errorEvent: Flow<Boolean>? = null,
) {
    val isVisibleDuplicateInsertError = rememberSaveable {
        mutableStateOf(false)
    }
    AlertDialog(
        // ここが空だとウィンドウ外をタップしても何も起こらない
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = stringResource(titleResId))
        },
        text = {
            Column {
                TextField(
                    value = inputText,
                    onValueChange = { onValueChange(it) },
                    singleLine = true
                )
                LaunchedEffect(errorEvent) {
                    errorEvent?.collect {
                        isVisibleDuplicateInsertError.value = it
                    }
                }
                if (isVisibleDuplicateInsertError.value) {
                    Text(
                        stringResource(id = R.string.error_duplicate_insert),
                        color = MaterialTheme.colorScheme.error
                    )
                }
                CategoryDropMenu(
                    selectCategory = selectCategory,
                    onDropDownMenuClick = onSelectVegeCategory,
                    modifier =
                    Modifier
                        .padding(top = 4.dp)
                )
            }
        },
        confirmButton = {
            if (isAddAble) {
                TextButton(
                    onClick = { onConfirmClick() }
                ) {
                    Text(stringResource(R.string.add_text))
                }
            } else {
                TextButton(
                    onClick = { }
                ) {
                    Text(stringResource(id = R.string.add_text), color = Color.LightGray)
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onCancelClick() }
            ) {
                Text(stringResource(R.string.cancel_text))
            }
        }
    )
}

@Composable
fun EditAlertWindow(selectStatus: VegeStatus, isOpenDialog: Boolean, onMenuItemIconClick: (VegeStatus) -> Unit, onConfirmClick: () -> Unit, modifier: Modifier = Modifier) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    val selectIcon = VegeStatusMethod.getIconId(selectStatus)
    val selectIconTint = VegeStatusMethod.getIconTint(selectStatus)
    val selectText = VegeStatusMethod.getText(selectStatus)

    if (isOpenDialog) {
        AlertDialog(
            modifier = modifier,
            onDismissRequest = { },
            title = {
                Text(text = stringResource(R.string.edit_text_field_title))
            },
            text = {
                Row(
                    modifier =
                    Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = {
                            expanded = true
                        }
                    ) {
                        Icon(
                            painterResource(id = R.drawable.ic_more_vert),
                            contentDescription = stringResource(R.string.drop_down_menu_button)
                        )
                    }
                    Icon(
                        painterResource(id = selectIcon),
                        tint = selectIconTint ?: LocalContentColor.current,
                        contentDescription = null
                    )
                    Text(
                        text = stringResource(id = selectText)
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        }
                    ) {
                        VegeStatus.values().forEach { status ->
                            ItemListDropDownMenuItem(
                                iconId = R.drawable.ic_edit,
                                text = stringResource(R.string.edit_button),
                                onClick = {
                                    onMenuItemIconClick(status)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { onConfirmClick() }
                ) {
                    Text(stringResource(id = R.string.save_text))
                }
            }
        )
    }
}

@Preview
@Composable
fun AlertPreview() {
    MaterialTheme {
        EditAlertWindow(
            selectStatus = VegeStatus.Favorite,
            isOpenDialog = true,
            onMenuItemIconClick = {},
            onConfirmClick = { }
        )
    }
}

@Composable
fun ConfirmDeleteItemDialog(
    modifier: Modifier = Modifier,
    deleteItem: VegeItem? = null,
    deleteFolder: VegetableFolderEntity? = null,
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        icon = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (deleteItem != null) {
                    Icon(
                        painterResource(id = R.drawable.ic_leaf),
                        contentDescription = null
                    )
                }
                if (deleteFolder != null) {
                    Icon(
                        painterResource(id = R.drawable.ic_folder),
                        contentDescription = null
                    )
                }
                Icon(
                    painterResource(id = R.drawable.ic_delete),
                    contentDescription = stringResource(id = R.string.delete_text)
                )
            }
        },
        title = {
            Column {
                Text(
                    stringResource(id = R.string.home_delete_dialog_title, deleteItem?.name ?: deleteFolder?.folderName ?: ""),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    stringResource(id = R.string.home_delete_dialog_description),
                    style = MaterialTheme.typography.titleSmall
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirmClick,
                colors = ButtonDefaults.textButtonColors().copy(
                    contentColor = Color.Red
                )
            ) {
                Text(text = stringResource(id = R.string.delete_text))
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelClick) {
                Text(text = stringResource(id = R.string.cancel_text))
            }
        }
    )
}

@Composable
@DarkLightPreview
private fun ConfirmDeleteItemDialogPreview() {
    VegegrowthAppTheme {
        ConfirmDeleteItemDialog(
            deleteItem = VegeItem(
                id = 0,
                name = "キャベツ",
                VegeCategory.None,
                uuid = "",
                VegeStatus.End,
                folderId = null
            ),
            onDismissRequest = {},
            onConfirmClick = {},
            onCancelClick = {},
            deleteFolder = null
        )
    }
}
