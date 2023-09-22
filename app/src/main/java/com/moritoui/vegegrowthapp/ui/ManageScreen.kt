package com.moritoui.vegegrowthapp.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.moritoui.vegegrowthapp.R
import com.moritoui.vegegrowthapp.navigation.NavigationAppTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageScreen(
    navController: NavHostController
) {
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
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DrawGraph(modifier = Modifier
                .fillMaxWidth()
                .weight(1f))
            ImageCarousel(
                modifier = Modifier.weight(1f),
                currentImageBarHeight = 5,
                currentImageBarModifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 72.dp, top = 12.dp, end = 72.dp)
            )
            DetailData(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun DrawGraph(modifier: Modifier = Modifier){
    Box(modifier = modifier
        .background(color = Color.Red)
    ) {
        Text(text = "ここにグラフ")
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageCarousel(
    modifier: Modifier = Modifier,
    currentImageBarHeight: Int,
    currentImageBarModifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        val pagerCount = 5
        val pagerState = rememberPagerState(initialPage = 0)
        HorizontalPager(
            state = pagerState,
            pageCount = pagerCount
        ) {
            Image(
                painter = painterResource(id = R.drawable.leaf),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
            )
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
        memoData(modifier = Modifier.weight(1f))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun memoData(modifier: Modifier = Modifier) {
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