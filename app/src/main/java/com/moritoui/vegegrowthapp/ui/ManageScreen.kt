package com.moritoui.vegegrowthapp.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.moritoui.vegegrowthapp.R
import com.moritoui.vegegrowthapp.model.DateFormatter
import com.moritoui.vegegrowthapp.model.VegetableRepositoryList
import com.moritoui.vegegrowthapp.navigation.NavigationAppTopBar

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ManageScreen(
    navController: NavHostController
) {
    val pagerCount = 5
    val pagerState = rememberPagerState(initialPage = 0)

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
            DrawLineChart(
                currentIndex = pagerState.currentPage,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            ImageCarousel(
                pagerCount = pagerCount,
                pagerState = pagerState,
                modifier = Modifier.weight(1f),
                currentImageBarHeight = 5,
                currentImageBarModifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 72.dp, top = 12.dp, end = 72.dp)
            )
            DetailData(modifier = Modifier.weight(0.5f))
        }
    }
}

@Composable
fun DrawLineChart(currentIndex: Int, modifier: Modifier = Modifier) {
    val formatter by remember { mutableStateOf(DateFormatter()) }
    Canvas(
        modifier = modifier
            .padding(start = 24.dp, top = 16.dp, end = 24.dp, bottom = 8.dp)
    ) {
        val data = VegetableRepositoryList.getVegeRepositoryList()
        var pointX: Float? = null
        var pointY: Float? = null

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

        if (pointX != null && pointY != null) {
            // 点の描画
            drawCircle(
                color = Color.Red,
                radius = 16f,
                center = Offset(pointX!!, pointY!!)
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
    currentImageBarModifier: Modifier = Modifier
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
            pageSpacing = 8.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.leaf),
                    contentDescription = null,
                    modifier = Modifier
                        .aspectRatio(1f / 1f)
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
                    modifier = Modifier
                        .weight(1f)
                        .height(currentImageBarHeight.dp)
                        .background(backGroundColor)
                )
            }
        }
    }
}

@Composable
fun DetailData(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .padding(start = 24.dp, end = 24.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "2023-3-12 17時")
            Text(text = "1日目")
            Text(text = "10cm")
        }
        MemoData(modifier = Modifier.weight(1f))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoData(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = { MemoTopBar(
            onEditClick = { }
        )},
        modifier = modifier
    ) {
        LazyColumn(modifier = modifier.padding(it)) {
            items(1) {
                Text(
                    text = "aiueo\n".repeat(30),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun MemoTopBar(
    modifier: Modifier = Modifier.fillMaxWidth(),
    onEditClick: () -> Unit
) {
    Box(modifier = modifier,
        contentAlignment = Alignment.BottomEnd
    ) {
        IconButton(
            onClick = { onEditClick() }
        ) {
            Icon(
                Icons.Filled.Edit,
                contentDescription = "メモ編集",
            )
        }
    }
}

@Composable
@Preview
fun ManageScreenPreview() {
    val navController = rememberNavController()
    ManageScreen(navController = navController)
}