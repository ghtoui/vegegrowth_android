package com.moritoui.vegegrowthapp.ui.manual.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moritoui.vegegrowthapp.R
import com.moritoui.vegegrowthapp.ui.theme.VegegrowthAppTheme
import kotlinx.coroutines.launch

@Composable
fun ManualContent(modifier: Modifier = Modifier, finishedLookingPage: (Int) -> Unit, finishReadManual: () -> Unit) {
    val pageList: List<@Composable () -> Unit> = listOf(
        { FirstPage() },
        { SecondPage() },
        { ThirdPage() },
        { FourthPage() }
    )
    val pagerState = rememberPagerState(pageCount = { pageList.size })
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PagerTopBar(
            currentPage = pagerState.currentPage,
            pageCount = pagerState.pageCount
        )
        Spacer(modifier = Modifier.height(20.dp))
        ElevatedCard(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            HorizontalPager(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .weight(1f),
                state = pagerState,
                userScrollEnabled = false
            ) { page ->
                pageList[page].invoke()
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row {
            if (pagerState.currentPage != 0) {
                Button(
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.manual_previous_button)
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
            }
            Button(
                onClick = {
                    if (pagerState.currentPage == pagerState.pageCount - 1) {
                        finishReadManual()
                    }
                    finishedLookingPage(pagerState.currentPage)

                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            ) {
                Text(
                    text = if (pagerState.currentPage == pagerState.pageCount - 1) {
                        stringResource(id = R.string.manual_finish_button)
                    } else {
                        stringResource(id = R.string.manual_next_button)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun ManualContentPreview() {
    VegegrowthAppTheme {
        Surface {
            ManualContent(
                finishedLookingPage = {},
                finishReadManual = {}
            )
        }
    }
}
