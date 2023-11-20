package com.moritoui.vegegrowthapp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.moritoui.vegegrowthapp.R
import com.moritoui.vegegrowthapp.model.SelectMenu
import com.moritoui.vegegrowthapp.model.SelectMenuMethod
import com.moritoui.vegegrowthapp.model.SortStatus
import com.moritoui.vegegrowthapp.model.VegeCategory
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegeStatus
import com.moritoui.vegegrowthapp.model.VegeStatusMethod
import com.moritoui.vegegrowthapp.model.getIcon
import com.moritoui.vegegrowthapp.model.getText
import com.moritoui.vegegrowthapp.model.getTint
import com.moritoui.vegegrowthapp.navigation.AddItem
import com.moritoui.vegegrowthapp.navigation.FirstNavigationAppTopBar
import com.moritoui.vegegrowthapp.navigation.Screen

@Composable
fun FirstScreen(
    viewModel: FirstScreenViewModel,
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            FirstNavigationAppTopBar(
                title = stringResource(R.string.first_screen_title)
            ) {
                AddItem(onAddClick = {
                    viewModel.openAddDialog()
                })
            }
        }
    ) { it ->
        Box(modifier = Modifier.padding(it)) {
            Scaffold(
                topBar = {
                    ItemListTopBar(
                        onCancelClick = { viewModel.cancelMenu() },
                        onDeleteIconClick = { viewModel.changeDeleteMode() },
                        onEditIconClick = { viewModel.changeEditMode() },
                        selectMenu = uiState.selectMenu,
                        onSortItemClick = { viewModel.setSortItemList(it) },
                        modifier = Modifier
                            .padding(start = 24.dp, top = 16.dp, end = 8.dp)
                    )
                }
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(it)
                        .padding(start = 32.dp, end = 24.dp)
                ) {
                    itemsIndexed(viewModel.vegeItemList) { index, item ->
                        VegeItemElement(
                            item = item,
                            selectMenu = uiState.selectMenu,
                            onDeleteClick = { item, isDelete ->
                                viewModel.deleteItem(item = item, isDelete = isDelete)
                            },
                            onMenuItemIconClick = { viewModel.selectStatus() },
                            onClick = {
                                val sortIndex = uiState.sortStatus.toString()
                                navController.navigate("${Screen.TakePictureScreen.route}/$index/$sortIndex") {
                                    popUpTo(navController.graph.startDestinationId)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
    AddAlertWindow(
        selectCategory = uiState.selectCategory,
        isOpenDialog = uiState.isOpenAddDialog,
        inputText = uiState.inputText,
        isAddAble = uiState.isAddAble,
        onValueChange = { viewModel.changeInputText(inputText = it) },
        onConfirmClick = { viewModel.saveVegeItemListData() },
        onDismissClick = { viewModel.closeDialog() },
        onDropDownMenuClick = { viewModel.selectCategory(it) }
    )
}

@SuppressLint("ResourceType")
@Composable
fun VegeItemElement(
    item: VegeItem,
    onDeleteClick: (VegeItem, Boolean) -> Unit,
    selectMenu: SelectMenu,
    onClick: () -> Unit = { },
    onMenuItemIconClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var isCheck by rememberSaveable { mutableStateOf(false) }
    var showStatus by rememberSaveable { mutableStateOf(item.status) }
    showStatus = item.status

    val statusIcon = VegeStatusMethod.getIcon(showStatus)
    val statusIconTint = VegeStatusMethod.getIconTint(vegeStatus = showStatus)
    val categoryIcon = item.category.getIcon()
    val iconTint = item.category.getTint()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable(onClick = { onClick() }),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (categoryIcon != null) {
            Icon(
                painter = painterResource(id = categoryIcon),
                tint = iconTint,
                contentDescription = null
            )
        } else {
            Icon(
                Icons.Filled.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.background,
                modifier = Modifier.aspectRatio(1f / 1f)
            )
        }
        Text(
            text = item.name,
            modifier = Modifier
                .padding(start = 16.dp, end = 24.dp)
        )
        Icon(
            statusIcon,
            contentDescription = null,
            tint = statusIconTint
        )
        if (selectMenu != SelectMenu.None) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.BottomEnd
            ) {
                IconButton(onClick = {
                    onDeleteClick(item, isCheck)
                    isCheck = !isCheck
                }) {
                    if (selectMenu == SelectMenu.Delete) {
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = stringResource(id = R.string.delete_text),
                            tint = when (isCheck) {
                                false -> Color.Red
                                true -> Color.Transparent
                            },
                            modifier = Modifier.aspectRatio(1f / 1f)
                        )
                    } else {
                        IconButton(onClick = { expanded = true }) {
                            Icon(
                                Icons.Filled.Edit,
                                contentDescription = stringResource(id = R.string.delete_text),
                                tint = when (isCheck) {
                                    false -> Color.Black
                                    true -> Color.Transparent
                                },
                                modifier = Modifier.aspectRatio(1f / 1f)
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
                                iconTint = VegeStatusMethod.getIconTint(status),
                                onClick = {
                                    showStatus = status
                                    item.status = status
                                    onMenuItemIconClick()
                                    expanded = false
                                }
                            )
                        }
                    }
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = stringResource(R.string.done_text),
                        tint = when (isCheck) {
                            false -> Color.Transparent
                            true -> Color.Black
                        },
                        modifier = Modifier.aspectRatio(1f / 1f)
                    )
                }
            }
        } else {
            isCheck = false
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .height(0.5.dp)
    )
}

@Composable
fun CategoryDropMenu(
    selectCategory: VegeCategory,
    onDropDownMenuClick: (VegeCategory) -> Unit,
    modifier: Modifier = Modifier
) {
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
            modifier = Modifier
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
fun ItemListTopBar(
    selectMenu: SelectMenu,
    onCancelClick: () -> Unit,
    onDeleteIconClick: () -> Unit,
    onEditIconClick: () -> Unit,
    onSortItemClick: (SortStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectMenuExpanded by rememberSaveable { mutableStateOf(false) }
    var sortMenuExpanded by rememberSaveable { mutableStateOf(false) }

    val menuIcon = SelectMenuMethod.getIcon(selectMenu)
    val menuIconTint = SelectMenuMethod.getIconTint(selectMenu)

    Row(
        modifier = modifier
            .wrapContentSize(Alignment.TopStart),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .clickable {
                    sortMenuExpanded = true
                }
        ) {
            Icon(
                Icons.Filled.List,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(R.string.sort_text)
            )
            DropdownMenu(
                expanded = sortMenuExpanded,
                onDismissRequest = { sortMenuExpanded = false }
            ) {
                SortStatus.values().forEach { sortStatus ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(id = sortStatus.getText())
                            )
                        },
                        onClick = {
                            onSortItemClick(sortStatus)
                            sortMenuExpanded = false
                        }
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopEnd)
        ) {
            Row() {
                if (selectMenu == SelectMenu.Delete) {
                    IconButton(
                        onClick = { onCancelClick() }
                    ) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = stringResource(id = R.string.cancel_text),
                        )
                    }
                }
                IconButton(
                    onClick = {
                        when (selectMenu) {
                            SelectMenu.Delete -> onDeleteIconClick()
                            SelectMenu.Edit -> onEditIconClick()
                            SelectMenu.None -> selectMenuExpanded = true
                        }
                    }
                ) {
                    Icon(
                        menuIcon,
                        contentDescription = stringResource(R.string.drop_down_menu_button),
                        tint = menuIconTint
                    )
                }
            }
            DropdownMenu(
                expanded = selectMenuExpanded,
                onDismissRequest = {
                    selectMenuExpanded = false
                },
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                ItemListDropDownMenuItem(
                    icon = Icons.Filled.Delete,
                    text = stringResource(R.string.delete_text),
                    iconTint = Color.Red,
                    onClick = {
                        onDeleteIconClick()
                        selectMenuExpanded = false
                    }
                )
                ItemListDropDownMenuItem(
                    icon = Icons.Filled.Edit,
                    text = stringResource(R.string.edit_button),
                    onClick = {
                        onEditIconClick()
                        selectMenuExpanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun ItemListDropDownMenuItem(
    icon: ImageVector,
    text: String,
    iconTint: Color = Color.Black,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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

@Preview
@Composable
fun FirstScreenPreview() {
    MaterialTheme {
//    FirstScreen(navController = rememberNavController())

//        VegeItemElement(
//            onDeleteClick = { item, isDelete -> },
//            onMenuItemIconClick = {},
//            selectMenu = SelectMenu.Edit,
//            item = VegeItem(
//                name = "aiueo",
//                category = VegeCategory.None,
//                uuid = UUID.randomUUID().toString(),
//                status = VegeStatus.Favorite
//            ),
//            onClick = {},
//            modifier = Modifier.background(Color.White)
//        )
        ItemListTopBar(
            selectMenu = SelectMenu.Edit,
            onCancelClick = { /*TODO*/ },
            onDeleteIconClick = { /*TODO*/ },
            onEditIconClick = { /*TODO*/ },
            onSortItemClick = { },
            modifier = Modifier.background(Color.White)
        )
    }
}
