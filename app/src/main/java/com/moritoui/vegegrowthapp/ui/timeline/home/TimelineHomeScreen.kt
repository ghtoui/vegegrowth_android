package com.moritoui.vegegrowthapp.ui.timeline.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.navigation.BaseLayout
import com.moritoui.vegegrowthapp.navigation.NavigationBarItems
import com.moritoui.vegegrowthapp.navigation.VegeGrowthBottomNavigationBar
import com.moritoui.vegegrowthapp.ui.home.navigateToHome
import com.moritoui.vegegrowthapp.ui.theme.VegegrowthAppTheme
import com.moritoui.vegegrowthapp.ui.timeline.home.components.TimelineHomeListItem
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

/**
 * タイムライン
 */
@Composable
fun TimelineHomeScreen(navController: NavController, viewModel: TimelineHomeViewModel = hiltViewModel()) {
    val vegetablesState = viewModel.uiState.map {
        it.vegetables
    }.collectAsLazyPagingItems()

    val lazyListState = rememberLazyListState()

    TimelineHomeScreen(
        modifier = Modifier,
        vegetablesState = vegetablesState,
        lazyListState = lazyListState,
        onClickHome = navController::navigateToHome,
        onClickTimeline = {},
        currentSelectItem = NavigationBarItems.Timeline
    )
}

@Composable
private fun TimelineHomeScreen(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState,
    vegetablesState: LazyPagingItems<VegeItem>,
    currentSelectItem: NavigationBarItems,
    onClickHome: () -> Unit,
    onClickTimeline: () -> Unit,
) {
    BaseLayout(
        bottomBar = {
            VegeGrowthBottomNavigationBar(
                currentSelectItem = currentSelectItem,
                onClickHome = onClickHome,
                onClickTimeline = onClickTimeline
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .padding(bottom = innerPadding.calculateBottomPadding()),
            state = lazyListState,
            contentPadding = PaddingValues(horizontal = 24.dp)
        ) {
            items(vegetablesState.itemCount, key = vegetablesState.itemKey { it.id }) {
                Column {
                    vegetablesState[it]?.let {
                        TimelineHomeListItem(vegeItem = it)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (vegetablesState.loadState.append is LoadState.Loading) {
                        Text(
                            text = "loading中..."
                        )
                    } else if (vegetablesState.loadState.append is LoadState.Error) {
                        Text(
                            text = "paging エラー"
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun TimelineHomeScreenPreview() {
    VegegrowthAppTheme {
        Surface {
            TimelineHomeScreen(
                onClickHome = {},
                onClickTimeline = {},
                lazyListState = rememberLazyListState(),
                currentSelectItem = NavigationBarItems.Timeline,
                vegetablesState = flowOf(PagingData.empty<VegeItem>()).collectAsLazyPagingItems()
            )
        }
    }
}
