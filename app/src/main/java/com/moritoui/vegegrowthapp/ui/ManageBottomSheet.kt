package com.moritoui.vegegrowthapp.ui

import android.graphics.Bitmap
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.moritoui.vegegrowthapp.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ImageBottomSheet(
    index: Int,
    pagerCount: Int,
    currentImageBarHeight: Int,
    modifier: Modifier = Modifier,
    currentImageBarModifier: Modifier = Modifier,
    imageFilePathList: List<String>,
    onDismissRequest: (Int) -> Unit
) {

    val skipPartiallyExpanded by remember { mutableStateOf(true) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )

    val pagerState = rememberPagerState(initialPage = index) { pagerCount }
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = { onDismissRequest(pagerState.currentPage) },
        sheetState = bottomSheetState
    ) {
        Column(
            modifier = modifier
        ) {
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(start = 24.dp, top = 12.dp, end = 24.dp),
                pageSpacing = 8.dp
            ) { page ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(0.1f))
                        .border(
                            width = 4.dp,
                            color = MaterialTheme.colorScheme.onSurface.copy(0.05f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = imageFilePathList[page],
                        contentDescription = null,
                        modifier = Modifier
                            .aspectRatio(1f / 1f)
                            .padding(8.dp),
                        error = painterResource(id = R.drawable.no_image)
                    )
                }
            }
            Row(
                modifier = currentImageBarModifier
                    .height(currentImageBarHeight.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pagerCount) {
                    val backGroundColor = when (it) {
                        pagerState.currentPage -> Color.Blue
                        else -> Color.LightGray
                    }
                    Box(
                        // タップで画面遷移できるようにする
                        modifier = Modifier
                            .weight(1f)
                            .height(currentImageBarHeight.dp)
                            .background(backGroundColor)
                            .clickable(onClick = { scope.launch { pagerState.animateScrollToPage(it) } })
                    )
                }
            }
        }
    }
}

@Composable
fun MemoEditorBottomSheet(
    inputText: String,
    onValueChange: (String) -> Unit,
    onCancelButtonClick: () -> Unit,
    onSaveButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = { },
        title = {
            Text(text = "メモを入力してください")
        },
        text = {
            TextField(
                value = inputText,
                onValueChange = { onValueChange(it) },
                singleLine = false,
                modifier = Modifier.height(96.dp)
            )
        },
        confirmButton = {
            Button(
                onClick = { onSaveButtonClick() },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("保存")
            }
        },
        dismissButton = {
            Button(
                onClick = { onCancelButtonClick() },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("キャンセル")
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun BottomSheetPreview() {
    val scope = rememberCoroutineScope()
    val imageList = listOf<Bitmap?>(
        null, null, null, null, null
    )
//    VegegrowthAppTheme {
//        ImageBottomSheet(
//            pagerCount = 5,
//            currentImageBarHeight = 5,
//            imageList = imageList,
//            // ボトムバークリックでも画像遷移できるように -> Coroutineが必要
//            onImageBottomBarClick = { scope.launch { pagerState.animateScrollToPage(it) } },
//            onDismissRequest = { },
//            modifier = Modifier.fillMaxSize()
//        )
//    }
}
