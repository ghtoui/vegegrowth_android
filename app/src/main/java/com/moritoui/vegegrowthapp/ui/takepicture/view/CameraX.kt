package com.moritoui.vegegrowthapp.ui.takepicture.view

import android.content.Context
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(onCloseCamera: () -> Unit, onTakePicClick: (ImageProxy?) -> Unit) {
    val cameraPermissionState: PermissionState =
        rememberPermissionState(android.Manifest.permission.CAMERA)

    // カメラ使用許可を得る
    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted &&
            !cameraPermissionState.status.shouldShowRationale
        ) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    if (cameraPermissionState.status.isGranted) {
        CameraXContent(
            onCloseCamera = onCloseCamera,
            onTakePicClick = onTakePicClick
        )
    } else {
        // 使用許可がない場合は、もう一度聞く
        LaunchedEffect(key1 = Unit) {
            cameraPermissionState.launchPermissionRequest()
        }
    }
}

@Composable
private fun CameraXContent(onCloseCamera: () -> Unit, onTakePicClick: (ImageProxy?) -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val previewView =
        remember {
            PreviewView(context)
        }
    val preview =
        androidx.camera.core.Preview
            .Builder()
            .build()

    val lensFacing = CameraSelector.LENS_FACING_BACK
    val cameraxSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
    val imageCapture =
        remember {
            ImageCapture
                .Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .build()
        }
    LaunchedEffect(lensFacing) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(lifecycleOwner, cameraxSelector, preview, imageCapture)
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }
    Box {
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )
        CameraBottomBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            onTakePicClick = {
                captureImage(imageCapture, context) { caputuredImage ->
                    if (caputuredImage != null) {
                        onTakePicClick(caputuredImage)
                    }
                }
            },
            onCancelClick = onCloseCamera
        )
    }
}

@Composable
private fun CameraBottomBar(modifier: Modifier = Modifier, onTakePicClick: () -> Unit, onCancelClick: () -> Unit) {
    Row(
        modifier =
        modifier
            .fillMaxWidth()
            .padding(WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
            .height(LocalConfiguration.current.screenHeightDp.dp / 10),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IconButton(
            modifier = Modifier.aspectRatio(1f / 1f),
            onClick = onCancelClick
        ) {
            Icon(
                modifier = Modifier.aspectRatio(1f / 0.5f),
                imageVector = Icons.Filled.Close,
                contentDescription = "キャンセル",
                tint = Color.White
            )
        }
        Surface(
            modifier = Modifier.aspectRatio(1f / 1f),
            onClick = onTakePicClick,
            shape = CircleShape,
            border =
            BorderStroke(
                width = 2.dp,
                color = Color.White
            ),
            color = Color.Transparent
        ) {
            Surface(
                modifier = Modifier.padding(6.dp),
                shape = CircleShape,
                color = Color.White
            ) {}
        }
        // surfaceを真ん中にするためにアイコンと同じ大きさを用意
        Spacer(
            modifier = Modifier.aspectRatio(1f / 1f)
        )
    }
}

@PreviewLightDark
@Composable
fun CameraScreenPreview() {
    CameraBottomBar(
        onTakePicClick = { },
        onCancelClick = { }
    )
}

private fun captureImage(imageCapture: ImageCapture, context: Context, onImageCaptured: (ImageProxy?) -> Unit) {
    imageCapture.takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onError(exception: ImageCaptureException) {
                Log.e("error", "キャプチャに失敗しました. ${exception.message}")
                onImageCaptured(null)
            }

            override fun onCaptureSuccess(image: ImageProxy) {
                onImageCaptured(image)
            }
        }
    )
}

private suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { cameraProvider ->
        cameraProvider.addListener({
            continuation.resume(cameraProvider.get())
        }, ContextCompat.getMainExecutor(this))
    }
}
