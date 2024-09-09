package com.moritoui.vegegrowthapp.ui.home.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.moritoui.vegegrowthapp.R
import com.moritoui.vegegrowthapp.data.room.model.VegetableFolderEntity
import com.moritoui.vegegrowthapp.model.SelectMenu
import com.moritoui.vegegrowthapp.model.VegeCategory
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegeItemDetail
import com.moritoui.vegegrowthapp.model.VegeStatus
import com.moritoui.vegegrowthapp.model.VegeStatusMethod
import com.moritoui.vegegrowthapp.model.getIconId
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
    onSelectMoveFolder: (VegeItem) -> Unit,
) {
    val statusIcon = VegeStatusMethod.getIconId(vegetable.status)
    val statusIconTint = VegeStatusMethod.getIconTint(vegetable.status) ?: LocalContentColor.current

    Card(
        modifier = modifier
            .padding(6.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        onClick = { onVegeItemClick(vegetable.id) },
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 4.dp
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
                placeholder = painterResource(R.drawable.no_image),
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
                painterResource(id = statusIcon),
                contentDescription = null,
                tint = statusIconTint,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            VegetableEditMenu(
                selectMenu = selectMenu,
                onItemDeleteClick = { onItemDeleteClick(vegetable) },
                onSelectVegeStatus = {
                    onSelectVegeStatus(
                        VegeItem(
                            id = vegetable.id,
                            folderId = vegetable.folderId,
                            name = vegetable.name,
                            uuid = vegetable.uuid,
                            category = vegetable.category,
                            status = it
                        )
                    )
                },
                onSelectMoveFolder = { onSelectMoveFolder(vegetable) }
            )
        }
    }
}

@Composable
private fun VegetableInfo(modifier: Modifier = Modifier, vegetable: VegeItem, vegetableDetail: VegeItemDetail?) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            VegetableCategoryIcon(category = vegetable.category)
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
private fun VegetableCategoryIcon(category: VegeCategory) {
    val iconTint: Color = category.getTint(otherColor = LocalContentColor.current)
    val categoryIcon = category.getIconId()

    if (categoryIcon != null) {
        Icon(
            painter = painterResource(id = categoryIcon),
            contentDescription = null,
            tint = iconTint
        )
    }
}

@Composable
private fun VegetableEditMenu(
    modifier: Modifier = Modifier,
    selectMenu: SelectMenu,
    onItemDeleteClick: (() -> Unit)? = null,
    onSelectVegeStatus: ((VegeStatus) -> Unit)? = null,
    onSelectMoveFolder: (() -> Unit)? = null,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    when (selectMenu) {
        SelectMenu.Edit -> {
            onSelectVegeStatus ?: return
            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        painterResource(id = R.drawable.ic_edit),
                        contentDescription = stringResource(id = R.string.delete_text),
                        modifier = Modifier.size(24.dp)
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    VegeStatus.values().forEach { status ->
                        ItemListDropDownMenuItem(
                            iconId = VegeStatusMethod.getIconId(status),
                            text = stringResource(VegeStatusMethod.getText(status)),
                            iconTint = VegeStatusMethod.getIconTint(status) ?: LocalContentColor.current,
                            onClick = {
                                onSelectVegeStatus(status)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        SelectMenu.Delete -> {
            onItemDeleteClick ?: return
            IconButton(
                modifier = modifier,
                onClick = onItemDeleteClick
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_delete),
                    contentDescription = stringResource(id = R.string.delete_text),
                    tint = Color.Red,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        SelectMenu.MoveFolder -> {
            onSelectMoveFolder ?: return
            IconButton(
                modifier = modifier,
                onClick = onSelectMoveFolder
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_folder_move),
                    contentDescription = stringResource(id = R.string.folder_move_button),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        else -> {
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
                category = VegeCategory.Leaf,
                folderId = null
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
            selectMenu = SelectMenu.Edit,
            onSelectMoveFolder = {}
        )
    }
}

@Composable
fun VegeFolderCard(
    modifier: Modifier = Modifier,
    vegetableFolder: VegetableFolderEntity,
    selectMenu: SelectMenu,
    onItemDeleteClick: (VegetableFolderEntity) -> Unit,
    onFolderClick: (VegetableFolderEntity) -> Unit,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        onClick = { onFolderClick(vegetableFolder) }
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .sizeIn(minHeight = 40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            VegetableCategoryIcon(category = vegetableFolder.vegetableCategory)
            Spacer(modifier = Modifier.width(8.dp))
            Text(vegetableFolder.folderName)
            Spacer(modifier = Modifier.weight(1f))
            VegetableEditMenu(
                selectMenu = selectMenu,
                onItemDeleteClick = { onItemDeleteClick(vegetableFolder) }
            )
        }
    }
}

@DarkLightPreview
@Composable
fun VegeFolderCardPreview() {
    VegegrowthAppTheme {
        VegeFolderCard(
            vegetableFolder = VegetableFolderEntity(
                folderName = "テストフォルダー",
                folderNumber = 0,
                id = 0,
                vegetableCategory = VegeCategory.Leaf
            ),
            selectMenu = SelectMenu.Delete,
            onItemDeleteClick = {},
            onFolderClick = {}
        )
    }
}
