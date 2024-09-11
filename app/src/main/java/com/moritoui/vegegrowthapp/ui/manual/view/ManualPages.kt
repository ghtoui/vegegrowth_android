package com.moritoui.vegegrowthapp.ui.manual.view

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moritoui.vegegrowthapp.R
import com.moritoui.vegegrowthapp.model.VegeItemDetail
import com.moritoui.vegegrowthapp.navigation.HomeAddItem
import com.moritoui.vegegrowthapp.ui.manage.DetailData
import com.moritoui.vegegrowthapp.ui.manage.view.DrawLineChart
import com.moritoui.vegegrowthapp.ui.theme.VegegrowthAppTheme
import java.time.LocalDateTime
import kotlinx.coroutines.launch

@Composable
fun FirstPage(modifier: Modifier = Modifier) {
    Column {
        CircleNumber(
            text = stringResource(id = R.string.manual_add_button),
            numberText = stringResource(id = R.string.first_number)
        )
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = stringResource(R.string.home_screen_title),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.weight(1f))
                HomeAddItem(
                    onFolderAddClick = {},
                    onAddClick = {}
                )
            }
            Icon(
                modifier = Modifier
                    .padding(top = 60.dp, end = 2.dp, bottom = 10.dp)
                    .size(70.dp)
                    .align(Alignment.CenterEnd),
                painter = painterResource(id = R.drawable.ic_touch),
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
private fun FirstPagePreview() {
    VegegrowthAppTheme {
        Surface {
            FirstPage()
        }
    }
}

@Composable
fun SecondPage(modifier: Modifier = Modifier) {
    Column {
        CircleNumber(
            text = stringResource(id = R.string.manual_tap_item),
            numberText = stringResource(id = R.string.second_number)
        )
        ListItemManual()
    }
}

@Preview
@Composable
private fun SecondPagePreview() {
    VegegrowthAppTheme {
        Surface {
            SecondPage()
        }
    }
}

@Composable
private fun ListItemManual(modifier: Modifier = Modifier) {
    Box {
        Card(
            modifier = modifier
                .padding(6.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            onClick = { },
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Row(
                modifier = modifier
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(70.dp),
                    painter = painterResource(id = R.drawable.flower_sample_image),
                    contentDescription = null,
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.flower),
                            contentDescription = null,
                            tint = Color.Red
                        )
                        Text(stringResource(id = R.string.manual_daria_name))
                    }
                    Text(
                        "最後の登録日 ${LocalDateTime.now().toString().take(10)}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
        Icon(
            modifier = Modifier
                .padding(top = 60.dp, bottom = 10.dp)
                .size(70.dp)
                .align(Alignment.BottomCenter),
            painter = painterResource(id = R.drawable.ic_touch),
            contentDescription = null
        )
    }
}

@Composable
fun ThirdPage(modifier: Modifier = Modifier) {
    Column {
        CircleNumber(
            text = stringResource(id = R.string.manual_take_picture),
            numberText = stringResource(id = R.string.third_number)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.height(200.dp),
                painter = painterResource(id = R.drawable.flower_sample_image),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {},
                enabled = false
            ) {
                Text(stringResource(id = R.string.take_picture_button))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box {
                Button(onClick = {}) {
                    Text(stringResource(id = R.string.register_button))
                }
                Icon(
                    modifier = Modifier
                        .padding(top = 30.dp, bottom = 10.dp)
                        .size(70.dp)
                        .align(Alignment.BottomCenter),
                    painter = painterResource(id = R.drawable.ic_touch),
                    contentDescription = null
                )
            }
        }
    }
}

@Preview
@Composable
private fun ThirdPagePreview() {
    VegegrowthAppTheme {
        Surface {
            ThirdPage()
        }
    }
}

@Composable
fun FourthPage(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        CircleNumber(
            text = stringResource(id = R.string.manual_manage_description),
            numberText = stringResource(id = R.string.fourth_number)
        )
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = stringResource(id = R.string.manual_manage_size)
                )
                DrawLineChart(
                    modifier = Modifier.height(150.dp),
                    data = getManualVegeDetailList(),
                    currentIndex = 0
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Text(
                    modifier = Modifier.padding(top = 12.dp),
                    text = stringResource(id = R.string.manual_manage_picture)
                )
                ManualImageCarousel(
                    modifier = Modifier.height(200.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Text(
                    modifier = Modifier
                        .width((LocalConfiguration.current.screenWidthDp / 3).dp),
                    text = stringResource(id = R.string.manual_manage_memo)
                )
                DetailData(
                    modifier = Modifier
                        .height(150.dp),
                    memoData = "肥料をあげました．",
                    onEditClick = {}
                )
            }
        }
    }
}

@Preview
@Composable
private fun FourthPagePreview() {
    VegegrowthAppTheme {
        Surface {
            FourthPage()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ManualImageCarousel(modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        HorizontalPager(
            modifier =
            Modifier
                .weight(1f),
            state = pagerState,
            contentPadding = PaddingValues(start = 24.dp, top = 12.dp, end = 24.dp),
            pageSpacing = 8.dp
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(0.1f))
                    .border(
                        width = 4.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(0.05f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier
                        .aspectRatio(1f / 1f)
                        .padding(8.dp),
                    painter = painterResource(id = R.drawable.flower_sample_image),
                    contentDescription = null
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(3) {
                val backGroundColor =
                    when (it) {
                        pagerState.currentPage -> Color.Blue
                        else -> Color.LightGray
                    }
                Box(
                    // タップで画面遷移できるようにする
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .background(backGroundColor)
                        .clickable(
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(it)
                                }
                            }
                        )
                )
            }
        }
    }
}

@Composable
private fun CircleNumber(numberText: String, text: String) {
    Row(
        modifier = Modifier
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .border(
                    BorderStroke(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(numberText)
        }
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = text
        )
    }
}

private fun getManualVegeDetailList(): List<VegeItemDetail> {
    val baseVegeItemDetail = VegeItemDetail(
        id = 0,
        itemUuid = "",
        date = "2024-09-01",
        size = 3.0,
        memo = "",
        name = "",
        uuid = "",
        imagePath = "",
        vegeItemId = 0
    )
    val sizeList: List<Pair<Double, String>> = listOf(
        Pair(3.0, "2024-09-01 10:10:10"),
        Pair(4.0, "2024-09-02 10:10:10"),
        Pair(6.0, "2024-09-03 10:10:10"),
        Pair(6.0, "2024-09-04 10:10:10"),
        Pair(7.0, "2024-09-05 10:10:10"),
        Pair(10.0, "2024-09-06 10:10:10"),
        Pair(12.0, "2024-09-07 10:10:10"),
        Pair(12.5, "2024-09-08 10:10:10"),
        Pair(15.0, "2024-09-10 10:10:10"),
        Pair(18.0, "2024-09-11 10:10:10")
    )
    return sizeList.map {
        baseVegeItemDetail.copy(
            size = it.first,
            date = it.second
        )
    }
}
