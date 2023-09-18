package com.moritoui.vegegrowthapp.ui

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.moritoui.vegegrowthapp.R
import com.moritoui.vegegrowthapp.navigation.NavigationAppTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TakePicScreen(
    name: String,
    navController: NavHostController,
    viewModel: TakePictureScreenViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var takePicImage by rememberSaveable { mutableStateOf<Bitmap?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        takePicImage = bitmap
    }
    var isOpenDialog by rememberSaveable { mutableStateOf(false) }
    var inputText by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            NavigationAppTopBar(
                navController = navController,
                title = name,
            ) {
            }
        }
    ) { it ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            PictureView(image = takePicImage?.asImageBitmap())
            TakeButton(onClick = { cameraLauncher.launch() })
            if (takePicImage != null) {
                RecordButton(onClick = { viewModel.openRegisterDialog() })
            }
        }
    }

    RegisterAlertWindow(
        isOpenDialog = uiState.isOpenDialog,
        inputText = uiState.inputText,
        isSuccessInputText = uiState.isSuccessInputText,
        isBeforeInputText = uiState.isBeforeInputText,
        onValueChange = { viewModel.checkInputText(it) },
        onConfirmClick = { viewModel.closeRegisterDialog() },
        onDismissClick = { viewModel.closeRegisterDialog() }
    )
}

@Composable
fun PictureView(
    image: ImageBitmap?,
    modifier: Modifier = Modifier.size(300.dp)
) {
    if (image != null) {
        Image(
            image,
            contentDescription = stringResource(R.string.picture_description),
            modifier = modifier,
            contentScale = ContentScale.Fit
        )
    } else {
        Box(
            modifier = modifier.background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.picture_none_message),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
        }
    }
}

@Composable
fun TakeButton(
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick() }
    ) {
        Text(
            text = stringResource(R.string.take_picture_button)
        )
    }
}

@Composable
fun RecordButton(
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick() }
    ) {
        Text(
            text = stringResource(R.string.register_button)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterAlertWindow(
    isOpenDialog: Boolean,
    inputText: String,
    isSuccessInputText: Boolean,
    isBeforeInputText: Boolean,
    onValueChange: (String) -> Unit,
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit
) {
    if (isOpenDialog) {
        AlertDialog(
            // ここが空だとウィンドウ外をタップしても何も起こらない
            onDismissRequest = { },
            title = {
                Text(text = stringResource(R.string.register_text_field_describe))
            },
            text = {
                Column() {
                    TextField(
                        value = inputText,
                        onValueChange = { onValueChange(it) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = {
                            Text(
                                text = "cm",
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (!isSuccessInputText && !isBeforeInputText) {
                            Icon(
                                Icons.Sharp.Warning,
                                contentDescription = null,
                                tint = Color.Red
                            )
                            Text(
                                text = "正しい数値を入力してください",
                                color = Color.Red
                            )
                        } else {
                            // elseにこれをしないと上が表示できない
                            // recompose の関係？わからん
                            Text("")
                        }
                    }
                }
            },
            confirmButton = {
                if (isSuccessInputText && !isBeforeInputText) {
                    TextButton(
                        onClick = { onConfirmClick() }
                    ) {
                        Text("登録", color = Color.Blue)
                    }
                } else {
                    TextButton(
                        onClick = { }
                    ) {
                        Text(
                            text = "登録",
                            color = Color.Gray
                        )
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onDismissClick() }
                ) {
                    Text("戻る", color = Color.Blue)
                }
            }
        )
    }
}

@Preview
@Composable
fun TakePicPreview() {
    var isOpenDialog by rememberSaveable { mutableStateOf(true) }
    var inputText by rememberSaveable { mutableStateOf("") }
    var isSuccessInputText  by rememberSaveable { mutableStateOf(false) }
    var isBeforeInputText  by rememberSaveable { mutableStateOf(true) }

    RegisterAlertWindow(
        isOpenDialog = isOpenDialog,
        inputText = inputText,
        isSuccessInputText = isSuccessInputText,
        isBeforeInputText = isBeforeInputText,
        onValueChange = {
            inputText = it
            isSuccessInputText = when (inputText.toDoubleOrNull()) {
                null -> false
                else -> true
            }
            isBeforeInputText = false
        },
        onConfirmClick = { isOpenDialog = false },
        onDismissClick = { isOpenDialog = false }
    )
}
