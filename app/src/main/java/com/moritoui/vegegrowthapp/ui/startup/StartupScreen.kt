package com.moritoui.vegegrowthapp.ui.startup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moritoui.vegegrowthapp.R
import com.moritoui.vegegrowthapp.navigation.HomeAddItem
import com.moritoui.vegegrowthapp.navigation.NavigationAppTopBar
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

@Composable
private fun FirstPage(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(24.dp),
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .border(
                            BorderStroke(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary,
                            ),
                            shape = CircleShape,
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(id = R.string.one_number))
                }
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    text = stringResource(id = R.string.manual_add_button),
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
            Box {
                NavigationAppTopBar(
                    modifier = Modifier.padding(24.dp),
                    title = stringResource(R.string.home_screen_title),
                    actions = {
                        HomeAddItem(
                            onFolderAddClick = {},
                            onAddClick = {}
                        )
                    },
                    isVisibleBackButton = false
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(100.dp)
                        .offset(x = -25.dp)
                        .border(
                            BorderStroke(
                                width = 4.dp,
                                color = MaterialTheme.colorScheme.primary,
                            ),
                            shape = CircleShape,
                        )
                )
            }
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

@Preview
@Composable
private fun FirstPagePreview() {
    VegegrowthAppTheme {
        Surface {
            FirstPage()
        }
    }
}
