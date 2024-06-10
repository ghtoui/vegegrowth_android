package com.moritoui.vegegrowthapp.ui.takepicture

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.moritoui.vegegrowthapp.R
import com.moritoui.vegegrowthapp.navigation.NavigateItem
import com.moritoui.vegegrowthapp.navigation.NavigationAppTopBar
import com.moritoui.vegegrowthapp.ui.manage.navigateToManage
import com.moritoui.vegegrowthapp.ui.takepicture.view.CameraScreen

@Composable
fun TakePictureScreen(
    navController: NavController,
    viewModel: TakePictureScreenViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            NavigationAppTopBar(
                navController = navController,
                title = uiState.vegeName,
                isVisibleNavigationButton = uiState.isVisibleNavigateButton
            ) {
                NavigateItem {
                    navController.navigateToManage(viewModel.args)
                }
            }
        }
    ) { it ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(start = 24.dp, top = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            PictureView(image = uiState.takePicImage?.asImageBitmap())
            TakeButton(onClick = { viewModel.changeCameraOpenState() })
            if (uiState.takePicImage != null) {
                RecordButton(onClick = { viewModel.openRegisterDialog() })
            }
        }
    }

    if (uiState.isCameraOpen) {
        CameraScreen(
            onCloseCamera = { viewModel.changeCameraOpenState() },
            onTakePicClick = {
                viewModel.onTakePicture(it)
                viewModel.changeCameraOpenState()
            }
        )
    }

    RegisterAlertWindow(
        isOpenDialog = uiState.isOpenDialog,
        inputText = uiState.inputText,
        isSuccessInputText = uiState.isSuccessInputText,
        isBeforeInputText = uiState.isBeforeInputText,
        onValueChange = { viewModel.changeInputText(it) },
        onConfirmClick = { viewModel.registerVegeData() },
        onDismissClick = { viewModel.closeRegisterDialog() }
    )
}

@Composable
fun PictureView(
    image: ImageBitmap?,
    modifier: Modifier = Modifier.aspectRatio(1f / 1f)
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
            modifier = modifier
                .background(Color.LightGray),
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
                    // if文で空の要素を作りたいときは、modifierで高さとかを指定しとかないと表示されない時がある
                    Row(
                        modifier = Modifier
                            .height(24.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (!isSuccessInputText && !isBeforeInputText) {
                            Icon(
                                Icons.Sharp.Warning,
                                contentDescription = null,
                                tint = Color.Red,
                                modifier = Modifier
                            )
                            Text(
                                text = "正しい数値を入力してください",
                                color = Color.Red
                            )
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
    var isSuccessInputText by rememberSaveable { mutableStateOf(false) }
    var isBeforeInputText by rememberSaveable { mutableStateOf(true) }

//    TakePicScreen(
//        index = 1,
//        sortText = "All",
//        navController = rememberNavController(),
//        viewModel = Take
//    )

//    RegisterAlertWindow(
//        isOpenDialog = isOpenDialog,
//        inputText = inputText,
//        isSuccessInputText = isSuccessInputText,
//        isBeforeInputText = isBeforeInputText,
//        onValueChange = {
//            inputText = it
//            isSuccessInputText = when (inputText.toDoubleOrNull()) {
//                null -> false
//                else -> true
//            }
//            isBeforeInputText = false
//        },
//        onConfirmClick = { isOpenDialog = false },
//        onDismissClick = { isOpenDialog = false }
//    )
}
