package com.moritoui.vegegrowthapp.ui

import android.graphics.Bitmap
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ImageBottomSheet(
    pagerCount: Int,
    currentImageBarHeight: Int,
    onImageBottomBarClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    currentImageBarModifier: Modifier = Modifier,
    imageList: List<Bitmap?>,
    onDismissRequest: () -> Unit
) {

    val skipPartiallyExpanded by remember { mutableStateOf(true) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )
    val pagerState = rememberPagerState(initialPage = 0)
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = { onDismissRequest() },
        sheetState = bottomSheetState
    ) {
        Column(
            modifier = modifier
        ) {
            HorizontalPager(
                state = pagerState,
                pageCount = pagerCount,
                contentPadding = PaddingValues(start = 24.dp, top = 12.dp, end = 24.dp),
                pageSpacing = 8.dp,
            ) {
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
                    if (imageList[pagerState.currentPage] != null) {
                        Image(
                            BitmapPainter(imageList[pagerState.currentPage]!!.asImageBitmap()),
                            contentDescription = null,
                            modifier = Modifier
                                .aspectRatio(1f / 1f)
                                .padding(8.dp)
                        )
                    }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoEditorBottomSheet(
    onDismissRequest: () -> Unit,
    inputText: String,
    onValueChange: (String) -> Unit,
    onCancelButtonClick: () -> Unit,
    onSaveButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val skipPartiallyExpanded by remember { mutableStateOf(true) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )

    ModalBottomSheet(
        onDismissRequest = { onDismissRequest() },
        sheetState = bottomSheetState
    ) {
        Column(
            modifier = modifier
                .padding(top = 16.dp, start = 24.dp, end = 24.dp, bottom = 48.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = inputText,
                onValueChange = { onValueChange(it) },
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { onCancelButtonClick() },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("キャンセル")
                }

                Button(
                    onClick = { onSaveButtonClick() },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("保存")
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun BottomSheetPreview() {
    val scope = rememberCoroutineScope()
    val pagerState = PagerState()
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
