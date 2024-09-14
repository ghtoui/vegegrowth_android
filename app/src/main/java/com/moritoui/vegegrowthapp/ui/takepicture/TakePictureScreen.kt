package com.moritoui.vegegrowthapp.ui.takepicture

import androidx.camera.core.ImageProxy
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.moritoui.vegegrowthapp.R
import com.moritoui.vegegrowthapp.navigation.GoToManageItem
import com.moritoui.vegegrowthapp.ui.manage.navigateToManage
import com.moritoui.vegegrowthapp.ui.navigation.NavigationAppTopBar
import com.moritoui.vegegrowthapp.ui.takepicture.model.TakePictureScreenUiState
import com.moritoui.vegegrowthapp.ui.takepicture.view.CameraScreen
import com.moritoui.vegegrowthapp.ui.theme.VegegrowthAppTheme

@Composable
fun TakePictureScreen(navController: NavController, viewModel: TakePictureScreenViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TakePictureScreen(
        uiState = uiState,
        onGoToManageClick = { navController.navigateToManage(viewModel.args) },
        onNavigationIconClick = { navController.navigateUp() },
        goToCameraButtonClick = { viewModel.changeCameraOpenState() },
        onRegisterButtonClick = { viewModel.openRegisterDialog() },
        onCloseCameraClick = { viewModel.changeCameraOpenState() },
        onTakePictureButtonClick = {
            viewModel.onTakePicture(it)
            viewModel.changeCameraOpenState()
        },
        onSizeTextChange = { viewModel.changeInputText(it) },
        onConfirmClick = { viewModel.registerVegeData() },
        onDismissClick = { viewModel.closeRegisterDialog() }
    )
}

@Composable
private fun TakePictureScreen(
    uiState: TakePictureScreenUiState,
    onGoToManageClick: () -> Unit,
    onNavigationIconClick: () -> Unit,
    goToCameraButtonClick: () -> Unit,
    onRegisterButtonClick: () -> Unit,
    onCloseCameraClick: () -> Unit,
    onTakePictureButtonClick: (ImageProxy?) -> Unit,
    onSizeTextChange: (String) -> Unit,
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            NavigationAppTopBar(
                onBackNavigationButtonClick = onNavigationIconClick,
                title = uiState.vegeName,
                actions = {
                    if (uiState.isVisibleNavigateButton) {
                        GoToManageItem(
                            onNavigateClick = onGoToManageClick
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(start = 24.dp, top = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            PictureView(image = uiState.takePicImage?.asImageBitmap())
            TakeButton(
                isTakenPicture = uiState.takePicImage == null,
                onClick = goToCameraButtonClick
            )
            if (uiState.takePicImage != null) {
                RegisterButton(onClick = onRegisterButtonClick)
            }
        }
    }

    if (uiState.isCameraOpen) {
        CameraScreen(
            onCloseCamera = onCloseCameraClick,
            onTakePicClick = onTakePictureButtonClick
        )
    }

    RegisterAlertWindow(
        isOpenDialog = uiState.isOpenDialog,
        inputText = uiState.inputText,
        lastSavedSize = uiState.lastSavedSize,
        isSuccessInputText = uiState.isSuccessInputText,
        isBeforeInputText = uiState.isBeforeInputText,
        onValueChange = onSizeTextChange,
        onConfirmClick = onConfirmClick,
        onDismissClick = onDismissClick
    )
}

@Composable
fun PictureView(image: ImageBitmap?, modifier: Modifier = Modifier.aspectRatio(1f / 1f)) {
    if (image != null) {
        Image(
            image,
            contentDescription = stringResource(R.string.picture_description),
            modifier = modifier,
            contentScale = ContentScale.Fit
        )
    } else {
        Box(
            modifier =
            modifier
                .background(MaterialTheme.colorScheme.surfaceVariant),
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
fun TakeButton(isTakenPicture: Boolean, onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        colors = when (isTakenPicture) {
            false -> ButtonDefaults.filledTonalButtonColors()
            true -> ButtonDefaults.buttonColors()
        }
    ) {
        Text(
            text = stringResource(R.string.take_picture_button)
        )
    }
}

@Composable
fun RegisterButton(onClick: () -> Unit) {
    Button(
        onClick = { onClick() }
    ) {
        Text(
            text = stringResource(R.string.register_button)
        )
    }
}

@Composable
fun RegisterAlertWindow(
    isOpenDialog: Boolean,
    inputText: String,
    lastSavedSize: Double?,
    isSuccessInputText: Boolean,
    isBeforeInputText: Boolean,
    onValueChange: (String) -> Unit,
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit,
) {
    if (isOpenDialog) {
        AlertDialog(
            // ここが空だとウィンドウ外をタップしても何も起こらない
            onDismissRequest = { },
            title = {
                Text(text = stringResource(R.string.register_text_field_describe))
            },
            text = {
                Column {
                    if (lastSavedSize != null) {
                        Text(
                            stringResource(
                                R.string.take_picture_last_saved_vege_size,
                                lastSavedSize
                            ) + stringResource(id = R.string.common_cm_unit)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    TextField(
                        value = inputText,
                        onValueChange = { onValueChange(it) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = {
                            Text(
                                text = stringResource(R.string.common_cm_unit),
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    )
                    // if文で空の要素を作りたいときは、modifierで高さとかを指定しとかないと表示されない時がある
                    Row(
                        modifier =
                        Modifier
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
                                text = stringResource(
                                    R.string.take_picture_error_enter_collect_number
                                ),
                                color = Color.Red
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        when (isSuccessInputText && !isBeforeInputText) {
                            true -> onConfirmClick()
                            false -> {}
                        }
                    },
                    colors = when (isSuccessInputText && !isBeforeInputText) {
                        true -> ButtonDefaults.textButtonColors()
                        false -> ButtonDefaults.textButtonColors().copy(
                            contentColor = Color.Gray
                        )
                    }
                ) {
                    Text(stringResource(id = R.string.register_button))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onDismissClick() }
                ) {
                    Text(stringResource(id = R.string.take_picture_back))
                }
            }
        )
    }
}

@PreviewLightDark
@Composable
fun TakePicPreview(@PreviewParameter(TakePictureScreenPreviewParameterProvider::class) params: TakePictureScreenPreviewParameterProvider.Params) {
    VegegrowthAppTheme {
        TakePictureScreen(
            uiState = params.uiState,
            onSizeTextChange = {},
            onConfirmClick = {},
            onRegisterButtonClick = {},
            onDismissClick = {},
            onCloseCameraClick = {},
            onTakePictureButtonClick = {},
            onNavigationIconClick = {},
            goToCameraButtonClick = {},
            onGoToManageClick = {}
        )
    }
}

class TakePictureScreenPreviewParameterProvider : PreviewParameterProvider<TakePictureScreenPreviewParameterProvider.Params> {
    override val values =
        sequenceOf(
            Params(
                uiState = TakePictureScreenUiState.initialState()
            ),
            Params(
                uiState =
                TakePictureScreenUiState.initialState().copy(
                    isVisibleNavigateButton = true
                )
            )
        )

    data class Params(val uiState: TakePictureScreenUiState)
}
