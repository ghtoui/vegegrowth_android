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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.moritoui.vegegrowthapp.R
import com.moritoui.vegegrowthapp.navigation.NavigationAppTopBar
import com.moritoui.vegegrowthapp.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TakePicScreen(
    name: String,
    navController: NavHostController
) {
    var takePicImage by rememberSaveable { mutableStateOf<Bitmap?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        takePicImage = bitmap
    }

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
                RecordButton(onClick = {
                    navController.navigate(Screen.ManageVegeScreen.route) {
                        popUpTo(navController.graph.startDestinationId)
                    }
                })
            }
        }
    }
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

@Preview
@Composable
fun TakePicPreview() {
    TakePicScreen(name = "テスト", navController = rememberNavController())
}
