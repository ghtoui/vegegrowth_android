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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.moritoui.vegegrowthapp.model.PagingListState
import com.moritoui.vegegrowthapp.navigation.BaseLayout
import com.moritoui.vegegrowthapp.navigation.NavigationBarItems
import com.moritoui.vegegrowthapp.navigation.VegeGrowthBottomNavigationBar
import com.moritoui.vegegrowthapp.ui.home.navigateToHome
import com.moritoui.vegegrowthapp.ui.theme.VegegrowthAppTheme
import com.moritoui.vegegrowthapp.ui.timeline.home.components.TimelineHomeListItem
import com.moritoui.vegegrowthapp.ui.timeline.home.model.TimelineHomeState
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

/**
 * タイムライン
 */
@Composable
fun TimelineHomeScreen(navController: NavController, viewModel: TimelineHomeViewModel = hiltViewModel()) {
    val vegetablesState by viewModel.uiState.collectAsStateWithLifecycle()

    val lazyListState = rememberLazyListState()

    TimelineHomeScreen(
        modifier = Modifier,
        vegetablesState = vegetablesState,
        lazyListState = lazyListState,
        onClickHome = navController::navigateToHome,
        onClickTimeline = {},
        currentSelectItem = NavigationBarItems.Timeline,
        addPage = viewModel::pageAdd
    )
}

@Composable
private fun TimelineHomeScreen(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState,
    vegetablesState: TimelineHomeState,
    currentSelectItem: NavigationBarItems,
    onClickHome: () -> Unit,
    onClickTimeline: () -> Unit,
    addPage: () -> Unit,
) {
    LaunchedEffect(
        lazyListState,
        vegetablesState,
    ) {
        combine(
            snapshotFlow { lazyListState.canScrollForward },
            snapshotFlow { vegetablesState.pagingListState },
        ) { canScrollForward, pagingListState ->
            !canScrollForward && pagingListState == PagingListState.Success
        }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                addPage()
            }
    }

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
            items(vegetablesState.vegetables, key = { it.uuid }) {
                Column {
                    TimelineHomeListItem(vegeItem = it)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    when (vegetablesState.pagingListState) {
                        PagingListState.Paginating -> {
                            Text(
                                text = "loading中..."
                            )
                        }
                        PagingListState.PaginateError -> {
                            Text(
                                text = "paging エラー"
                            )
                        }
                        else -> Unit
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
                vegetablesState = TimelineHomeState.initial(),
                addPage = {},
            )
        }
    }
}
