package com.moritoui.vegegrowthapp.ui.startup

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moritoui.vegegrowthapp.R
import com.moritoui.vegegrowthapp.ui.startup.view.FirstPage
import com.moritoui.vegegrowthapp.ui.startup.view.PagerTopBar
import com.moritoui.vegegrowthapp.ui.theme.VegegrowthAppTheme

@Composable
fun StartupScreen() {
    StartupScreen(
        modifier = Modifier
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun StartupScreen(
    modifier: Modifier = Modifier
) {
    val pageList: List<@Composable () -> Unit> = listOf(
        { FirstPage()},
        { FirstPage()},
    )
    val pagerState = rememberPagerState(pageCount = {pageList.size})

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
        HorizontalPager(state = pagerState) { page ->
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                pageList[page].invoke()
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {}
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

