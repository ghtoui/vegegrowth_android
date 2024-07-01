package com.moritoui.vegegrowthapp.ui.home.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moritoui.vegegrowthapp.R
import com.moritoui.vegegrowthapp.model.FilterStatus
import com.moritoui.vegegrowthapp.model.SelectMenu
import com.moritoui.vegegrowthapp.model.SelectMenuMethod
import com.moritoui.vegegrowthapp.previews.DarkLightPreview
import com.moritoui.vegegrowthapp.ui.theme.VegegrowthAppTheme

@Composable
fun ItemListTopBar(
    selectMenuExpanded: Boolean,
    filterMenuExpanded: Boolean,
    selectMenu: SelectMenu,
    onFilterDropDownMenuClose: () -> Unit,
    onSelectDropDownMenuClose: () -> Unit,
    onCancelClick: () -> Unit,
    onDeleteIconClick: () -> Unit,
    onEditIconClick: () -> Unit,
    onSelectMenuClick: () -> Unit,
    onFilterMenuClick: () -> Unit,
    onFilterItemClick: (FilterStatus) -> Unit,
    onFolderMoveIconClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val menuIcon = SelectMenuMethod.getIcon(selectMenu)
    val menuIconTint = SelectMenuMethod.getIconTint(selectMenu)

    Row(
        modifier =
        modifier
            .wrapContentSize(Alignment.TopStart),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = { onFilterMenuClick() }) {
            Row {
                Icon(
                    Icons.Filled.List,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.filter_text)
                )
            }
        }
        FilterDropDownMenu(
            filterMenuExpanded = filterMenuExpanded,
            onDismissRequest = onFilterDropDownMenuClose,
            onFilterItemClick = {
                onFilterItemClick(it)
                onFilterDropDownMenuClose()
            }
        )
        Box(
            modifier =
            Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopEnd)
        ) {
            when (selectMenu) {
                SelectMenu.None -> {
                    IconButton(
                        onClick = {
                            onCancelClick()
                            onSelectMenuClick()
                        }
                    ) {
                        Icon(
                            painterResource(id = menuIcon),
                            contentDescription = stringResource(R.string.drop_down_menu_button),
                            tint = menuIconTint ?: LocalContentColor.current
                        )
                    }
                }
                else -> {
                    TextButton(
                        onClick = {
                            onCancelClick()
                            onSelectDropDownMenuClose()
                        }
                    ) {
                        Text(stringResource(id = R.string.home_screen_select_menu_done))
                    }
                }
            }
            SelectDropDownMenu(
                modifier = Modifier.align(Alignment.BottomEnd),
                selectMenuExpanded = selectMenuExpanded,
                onDismissRequest = {
                    onSelectDropDownMenuClose()
                    onCancelClick()
                },
                onDeleteIconClick = {
                    onDeleteIconClick()
                    onSelectDropDownMenuClose()
                },
                onEditIconClick = {
                    onEditIconClick()
                    onSelectDropDownMenuClose()
                },
                onFolderMoveIconClick = {
                    onFolderMoveIconClick()
                    onSelectDropDownMenuClose()
                }
            )
        }
    }
}

@Composable
private fun SelectMenuHeader(selectMenu: SelectMenu, onMenuClick: () -> Unit) {
    val menuIcon = SelectMenuMethod.getIcon(selectMenu)
    val menuIconTint = SelectMenuMethod.getIconTint(selectMenu)
    when (selectMenu) {
        SelectMenu.None -> {
            IconButton(onClick = onMenuClick) {
                Icon(
                    painterResource(id = menuIcon),
                    contentDescription = stringResource(R.string.drop_down_menu_button),
                    tint = menuIconTint ?: LocalContentColor.current
                )
            }
        }
        else -> {
            TextButton(onClick = onMenuClick) {
                Text(stringResource(id = R.string.home_screen_select_menu_done))
            }
        }
    }
}

@DarkLightPreview
@Composable
fun ItemListTopBarPreview() {
    VegegrowthAppTheme {
        ItemListTopBar(
            filterMenuExpanded = false,
            selectMenuExpanded = false,
            selectMenu = SelectMenu.None,
            onCancelClick = {},
            onDeleteIconClick = {},
            onEditIconClick = {},
            onFilterItemClick = {},
            onFilterDropDownMenuClose = {},
            onSelectDropDownMenuClose = {},
            onFilterMenuClick = {},
            onSelectMenuClick = {},
            onFolderMoveIconClick = {}
        )
    }
}
