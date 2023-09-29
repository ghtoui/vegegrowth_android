package com.moritoui.vegegrowthapp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.moritoui.vegegrowthapp.R
import com.moritoui.vegegrowthapp.model.SelectMenu
import com.moritoui.vegegrowthapp.model.VegeCategory
import com.moritoui.vegegrowthapp.model.VegeCategoryMethod
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.navigation.AddItem
import com.moritoui.vegegrowthapp.navigation.FirstNavigationAppTopBar
import com.moritoui.vegegrowthapp.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirstScreen(
    viewModel: FirstScreenViewModel = viewModel(
        factory = FirstScreenViewModel.FirstScreenFactory(applicationContext = LocalContext.current.applicationContext)
    ),
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            FirstNavigationAppTopBar(
                title = "一覧画面",
                onNavigationIconClick = { viewModel.changeDeleteMode() },
                isDeleteMode = uiState.isDeleteMode,
                onCanselIconClick = { viewModel.resetDeleteItem() }
            ) {
                AddItem(onAddClick = {
                    viewModel.openDialog()
                })
            }
        }
    ) { it ->
        Box(modifier = Modifier.padding(it)) {
            Scaffold(
                topBar = {
                    ItemListTopBar(
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
                            isDeleteMode = uiState.isDeleteMode,
                            onDeleteClick = { item, isDelete ->
                                viewModel.deleteItem(item = item, isDelete = isDelete)
                            },
                            onClick = {
                                navController.navigate("${Screen.TakePictureScreen.route}/$index") {
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
        isOpenDialog = uiState.isOpenDialog,
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
    isDeleteMode: Boolean,
    onClick: () -> Unit = { },
    modifier: Modifier = Modifier
) {
    val categoryIcon = VegeCategoryMethod.getIcon(selectCategory = item.category)
    val iconTint = VegeCategoryMethod.getTint(selectCategory = item.category)

    var isDelete by rememberSaveable { mutableStateOf(false) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable(onClick = { onClick() }),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
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
                .weight(1f)
                .padding(start = 16.dp, end = 24.dp)
        )
        if (isDeleteMode) {
            Box() {
                IconButton(onClick = {
                    onDeleteClick(item, isDelete)
                    isDelete = !isDelete
                }) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = stringResource(id = R.string.delete_text),
                        tint = Color.Red,
                        modifier = Modifier.aspectRatio(1f / 1f)
                    )
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = stringResource(R.string.done_text),
                        tint = when (isDelete) {
                            false -> Color.Transparent
                            true -> Color.Black
                        },
                        modifier = Modifier.aspectRatio(1f / 1f)
                    )
                }
            }
        } else {
            isDelete = false
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .height(0.5.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAlertWindow(
    selectCategory: VegeCategory,
    isOpenDialog: Boolean,
    inputText: String,
    isAddAble: Boolean,
    onValueChange: (String) -> Unit,
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit,
    onDropDownMenuClick: (VegeCategory) -> Unit
) {
    if (isOpenDialog) {
        AlertDialog(
            // ここが空だとウィンドウ外をタップしても何も起こらない
            onDismissRequest = { },
            title = {
                Text(text = stringResource(R.string.addtextfield_describe))
            },
            text = {
                Column {
                    TextField(
                        value = inputText,
                        onValueChange = { onValueChange(it) },
                        singleLine = true
                    )
                    CategoryDropMenu(
                        selectCategory = selectCategory,
                        onDropDownMenuClick = onDropDownMenuClick,
                        modifier = Modifier
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
                    onClick = { onDismissClick() }
                ) {
                    Text(stringResource(R.string.cancel_text))
                }
            }
        )
    }
}

@Composable
fun CategoryDropMenu(
    selectCategory: VegeCategory,
    onDropDownMenuClick: (VegeCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    val categoryText = VegeCategoryMethod.getText(selectCategory = selectCategory)
    val categoryIcon = VegeCategoryMethod.getIcon(selectCategory = selectCategory)

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
        if (categoryIcon != null) {
            Icon(
                painter = painterResource(id = categoryIcon),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        Text(categoryText)
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
                        text = { Text(text = VegeCategoryMethod.getText(category)) }
                    )
                }
            }
        }
    }
}

@Composable
fun ItemListTopBar(modifier: Modifier = Modifier) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectIcon by rememberSaveable { mutableStateOf(SelectMenu.None) }

    val menuIcon = when (selectIcon) {
        SelectMenu.None -> Icons.Filled.MoreVert
        SelectMenu.Delete -> Icons.Filled.Delete
        SelectMenu.Edit -> Icons.Filled.Edit
        SelectMenu.Favorite -> Icons.Filled.Favorite
    }

    Row(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopEnd)
        ) {
            IconButton(
                onClick = { expanded = true }
            ) {
                Icon(
                    menuIcon,
                    contentDescription = stringResource(R.string.drop_down_menu_button)
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                    selectIcon = SelectMenu.None
                },
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                ItemListDropDownMenuItem(
                    icon = Icons.Filled.Delete,
                    text = stringResource(R.string.delete_text),
                    onClick = { selectIcon = SelectMenu.Delete }
                )
                ItemListDropDownMenuItem(
                    icon = Icons.Filled.Edit,
                    text = stringResource(R.string.edit_button),
                    onClick = { selectIcon = SelectMenu.Edit }
                )
                ItemListDropDownMenuItem(
                    icon = Icons.Filled.Favorite,
                    text = stringResource(R.string.favorite_button),
                    onClick = { selectIcon = SelectMenu.Favorite }
                )
            }
        }
    }
}

@Composable
fun ItemListDropDownMenuItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    DropdownMenuItem(
        text = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null)
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
        ItemListTopBar(modifier = Modifier.background(Color.White))
//    FirstScreen(navController = rememberNavController())

//    VegeItemElement(
//        onDeleteClick = { item, isDelete -> },
//        isDeleteMode = true,
//        item = VegeItem(
//            name = "aiueo",
//            category = VegeCategory.None,
//            uuid = UUID.randomUUID().toString()
//        ),
//        modifier = Modifier.background(Color.White)
//    )
    }
}
