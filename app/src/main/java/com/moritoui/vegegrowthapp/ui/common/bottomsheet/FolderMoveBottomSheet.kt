package com.moritoui.vegegrowthapp.ui.common.bottomsheet

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moritoui.vegegrowthapp.R
import com.moritoui.vegegrowthapp.data.room.model.VegetableFolderEntity
import com.moritoui.vegegrowthapp.dummies.HomeScreenDummy
import com.moritoui.vegegrowthapp.previews.DarkLightPreview
import com.moritoui.vegegrowthapp.ui.theme.VegegrowthAppTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FolderMoveBottomSheet(
    modifier: Modifier = Modifier,
    folders: List<VegetableFolderEntity>,
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    onFolderItemClick: (Int?) -> Unit,
    onCancelClick: () -> Unit,
) {
    ModalBottomSheet(
        modifier = Modifier.sizeIn(maxHeight = (LocalConfiguration.current.screenHeightDp / 2).dp),
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    ) {
        LazyColumn(
            modifier = modifier
                .padding(horizontal = 24.dp)
                .padding(bottom = 8.dp),
        ) {
            stickyHeader {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        stringResource(id = R.string.folder_move_title)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = onCancelClick) {
                        Text(stringResource(id = R.string.cancel_text))
                    }
                }
            }
            item {
                FolderItem(
                    folderName = stringResource(id = R.string.folder_not_classified_label),
                    onFolderItemClick = { onFolderItemClick(null) }
                )
            }
            items(folders, key = { folder -> folder.id }) { folder ->
                FolderItem(
                    folderName = folder.folderName,
                    onFolderItemClick = { onFolderItemClick(folder.id) }
                )
            }
        }
    }
}

@Composable
private fun FolderItem(
    modifier: Modifier = Modifier,
    folderName: String,
    onFolderItemClick: () -> Unit,
) {
    Column(
        modifier = modifier.clickable {
            onFolderItemClick()
        }
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_folder),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = folderName)
        }
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 2.dp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@DarkLightPreview
@Composable
fun FolderMoveBottomSheetPreview() {
    VegegrowthAppTheme {
        FolderMoveBottomSheet(
            modifier = Modifier,
            folders = HomeScreenDummy.vegeFolderList(),
            sheetState = rememberModalBottomSheetState(),
            onDismissRequest = {},
            onFolderItemClick = {},
            onCancelClick = {}
        )
    }
}
