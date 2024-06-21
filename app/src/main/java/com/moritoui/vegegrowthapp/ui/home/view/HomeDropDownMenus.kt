package com.moritoui.vegegrowthapp.ui.home.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moritoui.vegegrowthapp.R
import com.moritoui.vegegrowthapp.model.FilterStatus
import com.moritoui.vegegrowthapp.model.VegeCategory
import com.moritoui.vegegrowthapp.model.getIcon
import com.moritoui.vegegrowthapp.model.getText

@Composable
fun CategoryDropMenu(selectCategory: VegeCategory, onDropDownMenuClick: (VegeCategory) -> Unit, modifier: Modifier = Modifier) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { expanded = true }) {
            Icon(
                Icons.Filled.MoreVert,
                contentDescription = stringResource(R.string.menu_select)
            )
        }
        if (selectCategory.getIcon() != null) {
            Icon(
                painter = painterResource(id = selectCategory.getIcon()!!),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        Text(text = stringResource(id = selectCategory.getText()))
        DropdownMenu(
            modifier =
            Modifier
                // タップされた時の背景を円にする
                .clip(RoundedCornerShape(16.dp)),
            expanded = expanded,
            // メニューの外がタップされた時に閉じる
            onDismissRequest = { expanded = false }
        ) {
            VegeCategory.values().forEach { category ->
                if (category != VegeCategory.None) {
                    DropdownMenuItem(
                        onClick = {
                            onDropDownMenuClick(category)
                            expanded = false
                        },
                        text = { Text(text = stringResource(id = category.getText())) }
                    )
                }
            }
        }
    }
}

@Composable
fun ItemListDropDownMenuItem(modifier: Modifier = Modifier, icon: ImageVector, text: String, iconTint: Color = LocalContentColor.current, onClick: () -> Unit) {
    DropdownMenuItem(
        text = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(icon, contentDescription = null, tint = iconTint)
                Text(text)
            }
        },
        onClick = { onClick() },
        modifier = modifier
    )
}

@Composable
fun SelectDropDownMenu(modifier: Modifier = Modifier, selectMenuExpanded: Boolean, onDismissRequest: () -> Unit, onDeleteIconClick: () -> Unit, onEditIconClick: () -> Unit) {
    DropdownMenu(
        expanded = selectMenuExpanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier
    ) {
        ItemListDropDownMenuItem(
            icon = Icons.Filled.Delete,
            text = stringResource(R.string.delete_text),
            iconTint = Color.Red,
            onClick = onDeleteIconClick
        )
        ItemListDropDownMenuItem(
            icon = Icons.Filled.Edit,
            text = stringResource(R.string.edit_button),
            onClick = onEditIconClick
        )
    }
}

@Composable
fun FilterDropDownMenu(filterMenuExpanded: Boolean, onDismissRequest: () -> Unit, onFilterItemClick: (FilterStatus) -> Unit) {
    DropdownMenu(
        expanded = filterMenuExpanded,
        onDismissRequest = onDismissRequest
    ) {
        FilterStatus.values().forEach { filterStatus ->
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(id = filterStatus.getText())
                    )
                },
                onClick = {
                    onFilterItemClick(filterStatus)
                }
            )
        }
    }
}
