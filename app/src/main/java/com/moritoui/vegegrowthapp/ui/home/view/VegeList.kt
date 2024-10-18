package com.moritoui.vegegrowthapp.ui.home.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moritoui.vegegrowthapp.R
import com.moritoui.vegegrowthapp.data.room.model.VegetableFolderEntity
import com.moritoui.vegegrowthapp.model.SelectMenu
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegeItemDetail

@Composable
fun VegeList(
    modifier: Modifier = Modifier,
    folders: List<VegetableFolderEntity>,
    vegetables: List<VegeItem>,
    vegetableDetails: List<VegeItemDetail?>,
    onFolderClick: (Int) -> Unit,
    openDeleteDialog: (VegeItem) -> Unit,
    onDeleteFolderItem: (VegetableFolderEntity) -> Unit,
    onSelectVegeStatus: (VegeItem) -> Unit,
    onVegeItemClick: (Int) -> Unit,
    onSelectMoveFolder: (VegeItem) -> Unit,
    selectMenu: SelectMenu,
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (folders.isNotEmpty()) {
            item(
                span = { GridItemSpan(maxLineSpan) }
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_folder),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(stringResource(id = R.string.home_folder))
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 3.dp
                    )
                }
            }
        }
        items(folders) { folder ->
            VegeFolderCard(
                vegetableFolder = folder,
                onFolderClick = { onFolderClick(folder.id) },
                selectMenu = selectMenu,
                onItemDeleteClick = { onDeleteFolderItem(it) }
            )
        }
        if (vegetables.isNotEmpty()) {
            item(
                span = { GridItemSpan(maxLineSpan) }
            ) {
                Column {
                    Text(stringResource(id = R.string.home_item_not_classified))
                    Spacer(modifier = Modifier.height(2.dp))
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 3.dp
                    )
                }
            }
        }
        vegetables.zip(vegetableDetails).forEach { vegetable ->
            item(
                key = "${vegetable.first.id}, ${vegetable.first.name}",
                span = { GridItemSpan(maxLineSpan) }
            ) {
                VegeItemListCard(
                    vegetable = vegetable.first,
                    vegetableDetail = vegetable.second,
                    onVegeItemClick = { onVegeItemClick(it) },
                    selectMenu = selectMenu,
                    onItemDeleteClick = { item ->
                        openDeleteDialog(item)
                    },
                    onSelectVegeStatus = onSelectVegeStatus,
                    onSelectMoveFolder = { item ->
                        onSelectMoveFolder(item)
                    }
                )
            }
        }
    }
}
