package com.moritoui.vegegrowthapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.moritoui.vegegrowthapp.R
import com.moritoui.vegegrowthapp.model.VegeCategory
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.navigation.AddItem
import com.moritoui.vegegrowthapp.navigation.FirstNavigationAppTopBar
import com.moritoui.vegegrowthapp.navigation.Screen
import java.util.UUID

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
                title = "管理画面",
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, top = 48.dp, end = 24.dp)
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
    AddAlertWindow(
        isOpenDialog = uiState.isOpenDialog,
        inputText = uiState.inputText,
        onValueChange = { viewModel.changeInputText(inputText = it) },
        onConfirmClick = { viewModel.saveVegeItemListData() },
        onDismissClick = { viewModel.closeDialog() }
    )
}

@Composable
fun VegeItemElement(
    item: VegeItem,
    onDeleteClick: (VegeItem, Boolean) -> Unit,
    isDeleteMode: Boolean,
    onClick: () -> Unit = { },
    modifier: Modifier = Modifier
) {
    val categoryIcon = when (item.category) {
        VegeCategory.Leaf -> painterResource(id = R.drawable.leaf)
        VegeCategory.Flower -> painterResource(id = R.drawable.flower)
        else -> null
    }
    val iconTint = when (item.category) {
        VegeCategory.Leaf -> Color.Green
        VegeCategory.Flower -> Color.Magenta
        else -> null
    }
    var isDelete by rememberSaveable { mutableStateOf(false) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable(onClick = { onClick() }),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (categoryIcon != null && iconTint != null) {
            Icon(
                painter = categoryIcon,
                tint = iconTint,
                contentDescription = null
            )
        } else {
            Icon(
                Icons.Filled.Info,
                contentDescription = null,
                tint = Color.LightGray,
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
                        contentDescription = "削除",
                        tint = Color.Red,
                        modifier = Modifier.aspectRatio(1f / 1f)
                    )
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = "済み",
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
    isOpenDialog: Boolean,
    inputText: String,
    onValueChange: (String) -> Unit,
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit
) {
    if (isOpenDialog) {
        AlertDialog(
            // ここが空だとウィンドウ外をタップしても何も起こらない
            onDismissRequest = { },
            title = {
                Text(text = stringResource(R.string.addtextfield_describe))
            },
            text = {
                TextField(
                    value = inputText,
                    onValueChange = { onValueChange(it) },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { onConfirmClick() }
                ) {
                    Text("追加")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onDismissClick() }
                ) {
                    Text("キャンセル")
                }
            }
        )
    }
}

@Preview
@Composable
fun FirstScreenPreview() {
//    FirstScreen(navController = rememberNavController())
    VegeItemElement(
        onDeleteClick = { item, isDelete -> },
        isDeleteMode = true,
        item = VegeItem(
            name = "aiueo",
            category = VegeCategory.None,
            uuid = UUID.randomUUID().toString()
        ),
        modifier = Modifier.background(Color.White)
    )
}
