package com.moritoui.vegegrowthapp.ui.home.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.moritoui.vegegrowthapp.R
import com.moritoui.vegegrowthapp.model.SelectMenu
import com.moritoui.vegegrowthapp.model.VegeCategory
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegeItemDetail
import com.moritoui.vegegrowthapp.model.VegeStatus
import com.moritoui.vegegrowthapp.model.VegeStatusMethod
import com.moritoui.vegegrowthapp.model.getIcon
import com.moritoui.vegegrowthapp.model.getTint
import com.moritoui.vegegrowthapp.previews.DarkLightPreview
import com.moritoui.vegegrowthapp.ui.theme.VegegrowthAppTheme

@Composable
fun VegeItemListCard(
    modifier: Modifier = Modifier,
    vegetable: VegeItem,
    vegetableDetail: VegeItemDetail?,
    selectMenu: SelectMenu,
    onItemDeleteClick: (VegeItem) -> Unit,
    onSelectVegeStatus: (VegeItem) -> Unit,
    onVegeItemClick: (Int) -> Unit,
) {
    val selectedStatus = remember {
        mutableStateOf(vegetable.status)
    }
    val statusIcon = VegeStatusMethod.getIcon(selectedStatus.value)
    val statusIconTint = VegeStatusMethod.getIconTint(vegeStatus = selectedStatus.value) ?: LocalContentColor.current

    Card(
        modifier = modifier
            .padding(6.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        onClick = { onVegeItemClick(vegetable.id) },
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 4.dp,
        )
    ) {
        Row(
            modifier = modifier
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(16.dp)),
                model = vegetableDetail?.imagePath,
                contentDescription = "最後に撮影された写真",
                error = painterResource(id = R.drawable.no_image),
                // Previewで見えるようにするため
                placeholder = painterResource(R.drawable.loading_image),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            VegetableInfo(
                modifier = Modifier.weight(1f),
                vegetable = vegetable,
                vegetableDetail = vegetableDetail
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                statusIcon,
                contentDescription = null,
                tint = statusIconTint,
                modifier = Modifier.size(24.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            VegetableEditMenu(
                vegetable = vegetable,
                selectMenu = selectMenu,
                selectedStatus = selectedStatus,
                onItemDeleteClick = onItemDeleteClick,
                onSelectVegeStatus = onSelectVegeStatus
            )
        }
    }
}

@Composable
private fun VegetableInfo(modifier: Modifier = Modifier, vegetable: VegeItem, vegetableDetail: VegeItemDetail?) {
    val iconTint: Color = vegetable.category.getTint(otherColor = LocalContentColor.current)
    val categoryIcon = vegetable.category.getIcon()
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (categoryIcon != null) {
                Icon(
                    painter = painterResource(id = categoryIcon),
                    contentDescription = null,
                    tint = iconTint
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                vegetable.name,
                style = MaterialTheme.typography.titleLarge
            )
        }
        if (vegetableDetail != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(R.string.vege_detail_save_last_date),
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    vegetableDetail.date,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun VegetableEditMenu(
    modifier: Modifier = Modifier,
    vegetable: VegeItem,
    selectMenu: SelectMenu,
    selectedStatus: MutableState<VegeStatus>,
    onItemDeleteClick: (VegeItem) -> Unit,
    onSelectVegeStatus: (VegeItem) -> Unit,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    if (selectMenu != SelectMenu.None) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.BottomEnd
        ) {
            IconButton(onClick = {
                onItemDeleteClick(vegetable)
            }) {
                if (selectMenu == SelectMenu.Delete) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = stringResource(id = R.string.delete_text),
                        tint = Color.Red,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = stringResource(id = R.string.delete_text),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    VegeStatus.values().forEach { status ->
                        ItemListDropDownMenuItem(
                            icon = VegeStatusMethod.getIcon(status),
                            text = stringResource(VegeStatusMethod.getText(status)),
                            iconTint =
                            VegeStatusMethod.getIconTint(status)
                                ?: LocalContentColor.current,
                            onClick = {
                                vegetable.status = status
                                selectedStatus.value = status
                                onSelectVegeStatus(vegetable)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@DarkLightPreview
@Composable
fun VegeItemListCardPreview() {
    VegegrowthAppTheme {
        VegeItemListCard(
            vegetable = VegeItem(
                name = "キャベツ",
                id = 0,
                status = VegeStatus.End,
                category = VegeCategory.Leaf
            ),
            vegetableDetail = VegeItemDetail(
                name = "キャベツ",
                id = 0,
                date = "2020-04-04",
                vegeItemId = 0,
                memo = "",
                size = 3.3,
                imagePath = "test",
                itemUuid = "",
                uuid = ""
            ),
            onVegeItemClick = {},
            onSelectVegeStatus = {},
            onItemDeleteClick = {},
            selectMenu = SelectMenu.Edit
        )
    }
}
