package com.moritoui.vegegrowthapp.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.moritoui.vegegrowthapp.R
import com.moritoui.vegegrowthapp.model.DateFormatter
import com.moritoui.vegegrowthapp.model.VegetableRepositoryList
import com.moritoui.vegegrowthapp.navigation.NavigationAppTopBar
import com.moritoui.vegegrowthapp.ui.theme.VegegrowthAppTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ManageScreen(
    navController: NavHostController,
    viewModel: ManageViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            NavigationAppTopBar(
                navController = navController,
                title = "管理画面"
            ) {
            }
        }
    ) { it ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(0.1f))
                    .border(
                        width = 4.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(0.05f)
                    )
            ) {
                DrawLineChart(
                    currentIndex = uiState.pagerState.currentPage,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
            }
            ImageCarousel(
                pagerCount = uiState.pagerCount,
                pagerState = uiState.pagerState,
                modifier = Modifier.weight(1f),
                currentImageBarHeight = 5,
                imageList = viewModel.takePicList,
                onImageClick = { viewModel.changeOpenImageBottomSheet() },
                // ボトムバークリックでも画像遷移できるように -> Coroutineが必要
                onImageBottomBarClick = { scope.launch { viewModel.moveImage(it) } },
                currentImageBarModifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 72.dp, top = 12.dp, end = 72.dp, bottom = 8.dp)
            )
            DetailData(
                memoData = uiState.vegeRepositoryList[uiState.pagerState.currentPage].memo,
                modifier = Modifier.weight(0.7f)
            )
        }
    }

    if (uiState.isOpenImageBottomSheet) {
        ImageBottomSheet(
            pagerCount = uiState.pagerCount,
//            pagerState = uiState.pagerState,
            currentImageBarHeight = 5,
            modifier = Modifier.padding(top = 16.dp, bottom = 48.dp),
            imageList = viewModel.takePicList,
            onDismissRequest = { viewModel.changeOpenImageBottomSheet() },
            // ボトムバークリックでも画像遷移できるように -> Coroutineが必要
            onImageBottomBarClick = { scope.launch { viewModel.moveImage(it) } },
            currentImageBarModifier = Modifier
                .fillMaxWidth()
                .padding(start = 72.dp, top = 12.dp, end = 72.dp, bottom = 12.dp)
        )
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun DrawLineChart(
    currentIndex: Int,
    modifier: Modifier = Modifier
) {
    val formatter by remember { mutableStateOf(DateFormatter()) }
    val textMeasurer = rememberTextMeasurer()
    Canvas(
        modifier = modifier
    ) {
        val data = VegetableRepositoryList.getVegeRepositoryList()
        var pointX: Float? = null
        var pointY: Float? = null
        var detailData = ""

        // 初期化
        val minX = data.minOf { formatter.stringToEpochTime(it.date) }
        val maxX = data.maxOf { formatter.stringToEpochTime(it.date) }
        val minY = data.minOf { it.size }
        val maxY = data.maxOf { it.size }
        val scaleX = size.width / (maxX - minX)
        val scaleY = size.height / (maxY - minY)
        val path = Path()

        data.forEachIndexed { index, item ->
            val x = (formatter.stringToEpochTime(item.date) - minX) * scaleX
            val y = (size.height - (item.size - minY) * scaleY).toFloat()
            if (currentIndex == index) {
                pointX = x
                pointY = y
                detailData = "${item.date}\n${item.getDiffDatetime(data.first().date)}日目\n${item.size}cm"
            }

            // 最初ならパスの開始点, それ以外は直線を描画していく
            when (index) {
                0 -> path.moveTo(x, y)
                else -> path.lineTo(x, y)
            }
        }

        // パスの描画
        drawPath(
            path = path,
            color = Color.Black,
            style = Stroke(width = 4f)
        )

        if (pointX != null) {
            // 点の描画
            drawCircle(
                color = Color.Red,
                radius = 16f,
                center = Offset(pointX!!, pointY!!)
            )
            drawText(
                textMeasurer = textMeasurer,
                text = detailData,
                topLeft = Offset(x = pointX!!, y = pointY!!)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageCarousel(
    pagerCount: Int,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    currentImageBarHeight: Int,
    onImageClick: () -> Unit,
    onImageBottomBarClick: (Int) -> Unit,
    currentImageBarModifier: Modifier = Modifier,
    imageList: List<Bitmap?>
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        HorizontalPager(
            modifier = Modifier
                .weight(1f),
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
                            .clickable { onImageClick() }
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
                        .clickable(onClick = { onImageBottomBarClick(it) })
                )
            }
        }
    }
}

@Composable
fun DetailData(
    memoData: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(start = 24.dp, end = 24.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MemoData(
            memoData = memoData,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.onSurface.copy(0.05f))
                .padding(start = 12.dp, end = 12.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoData(
    memoData: String,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            MemoTopBar(
                modifier = modifier,
                onEditClick = { }
            )
        }
    ) {
        LazyColumn(
            modifier = modifier
                .padding(it)
                .fillMaxSize()
        ) {
            items(1) {
                Text(
                    text = memoData,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun MemoTopBar(
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "メモ",
            fontSize = 24.sp
        )
        Spacer(Modifier.weight(1f))
        IconButton(
            modifier = Modifier.width(96.dp),
            onClick = { onEditClick() }
        ) {
            Row(
                modifier = Modifier.padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = "メモ編集"
                )
                Text(
                    text = "編集",
                    fontSize = 24.sp
                )
            }
        }
    }
}

@Composable
@Preview
fun ManageScreenPreview() {
    VegegrowthAppTheme {
        val navController = rememberNavController()
        val imageList: List<Painter> = listOf(
            painterResource(id = R.drawable.leaf),
            painterResource(id = R.drawable.flower),
            painterResource(id = R.drawable.ic_launcher_background),
            painterResource(id = R.drawable.ic_launcher_foreground),
            painterResource(id = R.drawable.leaf),
            painterResource(id = R.drawable.flower),
            painterResource(id = R.drawable.ic_launcher_background),
            painterResource(id = R.drawable.ic_launcher_foreground),
            painterResource(id = R.drawable.ic_launcher_background),
            painterResource(id = R.drawable.ic_launcher_foreground),
        )
        ManageScreen(
            navController = navController,
            viewModel = TestManageViewModel(
                index = 1
            )
        )
    }
}
