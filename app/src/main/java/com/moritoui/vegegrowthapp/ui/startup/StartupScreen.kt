package com.moritoui.vegegrowthapp.ui.startup

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moritoui.vegegrowthapp.R
import com.moritoui.vegegrowthapp.ui.common.scrollbar.VerticalScrollbar
import com.moritoui.vegegrowthapp.ui.common.scrollbar.rememberScrollbarAdapter
import com.moritoui.vegegrowthapp.ui.startup.view.FirstPage
import com.moritoui.vegegrowthapp.ui.startup.view.FourthPage
import com.moritoui.vegegrowthapp.ui.startup.view.PagerTopBar
import com.moritoui.vegegrowthapp.ui.startup.view.SecondPage
import com.moritoui.vegegrowthapp.ui.startup.view.ThirdPage
import com.moritoui.vegegrowthapp.ui.theme.VegegrowthAppTheme
import kotlinx.coroutines.launch

@Composable
fun StartupScreen() {
    StartupScreen(
        modifier = Modifier
    )
}

@Composable
private fun StartupScreen(
    modifier: Modifier = Modifier
) {
    val pageList: List<@Composable () -> Unit> = listOf(
        { FirstPage()},
        { SecondPage() },
        { ThirdPage() },
        { FourthPage() },
    )
    val pagerState = rememberPagerState(pageCount = {pageList.size})
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val scrollable = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit, pagerState.currentPage) {
        scrollable.value = scrollState.canScrollForward
    }

    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        PagerTopBar(
            currentPage = pagerState.currentPage,
            pageCount = pagerState.pageCount,
        )
        Spacer(modifier = Modifier.height(20.dp))
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
        ) { page ->
            Box(
                modifier = Modifier
                    .sizeIn(
                        maxHeight = (LocalConfiguration.current.screenHeightDp / 1.5).dp
                    )
            ) {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .verticalScroll(scrollState)
                ) {
                    pageList[page].invoke()
                }
                if (scrollable.value) {
                    VerticalScrollbar(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 4.dp),
                        adapter = rememberScrollbarAdapter(scrollState = scrollState),
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            }
        ) {
            Text(
                text = stringResource(id = R.string.manual_next_button)
            )
        }
    }
}

@Preview
@Composable
private fun StartupScreenPreview() {
    VegegrowthAppTheme {
        Surface {
            StartupScreen(
                modifier = Modifier
            )
        }
    }
}

