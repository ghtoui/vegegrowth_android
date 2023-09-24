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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.moritoui.vegegrowthapp.navigation.NavigationAppTopBar
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
            NavigationAppTopBar(
                navController = navController,
                title = "管理画面"
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
                itemsIndexed(uiState.vegeItemList) { index, item ->
                    VegeItemElement(item = item, onClick = {
                        navController.navigate("${Screen.TakePictureScreen.route}/$index") {
                            popUpTo(navController.graph.startDestinationId)
                        }
                    })
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
fun VegeItemElement(item: VegeItem, onClick: () -> Unit = { }, modifier: Modifier = Modifier) {
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
                modifier = Modifier.aspectRatio(1f / 1f)
            )
        }
        Text(
            text = item.name,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 24.dp)
        )
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
        item = VegeItem(
            name = "aiueo",
            category = VegeCategory.None,
            uuid = UUID.randomUUID().toString()
        ),
        modifier = Modifier.background(Color.White)
    )
}
