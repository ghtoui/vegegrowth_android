package com.moritoui.vegegrowthapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moritoui.vegegrowthapp.R
import com.moritoui.vegegrowthapp.model.VegeCategory
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegeItemList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirstAppTopBar(onClick: () -> Unit, modifier: Modifier = Modifier) {
    TopAppBar(
        title = { Text(
            text  = "管理画面",
            textAlign = TextAlign.Center,
            modifier = modifier
        ) },
        actions = {
            IconButton(onClick = { onClick() }) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "追加"
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirstScreen() {
    var isOpenDialog by rememberSaveable { mutableStateOf(false) }
    var inputText by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            FirstAppTopBar(onClick = { inputText = ""; isOpenDialog = true }) }
    ) { it ->
        Box(modifier = Modifier.padding(it)) {
            ScrollView()
        }
    }
    AddAlertWindow(
        isOpenDialog = isOpenDialog,
        inputText = inputText,
        onValueChange = { inputText = it },
        onConfirmClick = { isOpenDialog = false },
        onDismissClick = { isOpenDialog = false }
        )
}

@Composable
fun ScrollView(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 24.dp, top = 48.dp, end = 24.dp)
    ) {
        items(VegeItemList.getVegeList()) { item ->
            VegeItem(item = item)
        }
    }
}

@Composable
fun VegeItem(item: VegeItem, onClick: () -> Unit = { } ,modifier: Modifier = Modifier) {
    val categoryIcon = when(item.category) {
        VegeCategory.leaf -> painterResource(id = R.drawable.leaf)
        VegeCategory.flower -> painterResource(id = R.drawable.flower)
    }
    val iconTint = when(item.category) {
        VegeCategory.leaf -> Color.Green
        VegeCategory.flower -> Color.Magenta
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable(onClick = { onClick }),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = categoryIcon,
            tint = iconTint,
            contentDescription = null
        )
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
    ) {}
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
                Text(text = "追加したい野菜の名前を入力してください")
            },
            text = {
                TextField(
                    value = inputText,
                    onValueChange = { onValueChange(it) },
                    label = { "名前入力" },
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
            })
    }
}

@Preview
@Composable
fun FirstScreenPreview() {
    FirstScreen()
}
