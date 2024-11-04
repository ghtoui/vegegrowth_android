package com.moritoui.vegegrowthapp.ui.community.home

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
import com.moritoui.vegegrowthapp.navigation.BaseLayout
import com.moritoui.vegegrowthapp.navigation.NavigationBarItems
import com.moritoui.vegegrowthapp.navigation.VegeGrowthBottomNavigationBar
import com.moritoui.vegegrowthapp.ui.community.home.components.CommunityListItem
import com.moritoui.vegegrowthapp.ui.community.home.model.CommunityHomeState
import com.moritoui.vegegrowthapp.ui.theme.VegegrowthAppTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

/**
 * 掲示板のホーム
 */
@Composable
fun CommunityHomeScreen(
    navController: NavController,
    viewModel: CommunityHomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lazyListState = rememberLazyListState()

    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.canScrollForward }
            .distinctUntilChanged()
            .filter { it }
            .collect { viewModel.autoAppend() }
    }

    CommunityHomeScreen(
        modifier = Modifier,
        uiState = uiState,
        lazyListState = lazyListState,
        onClickHome = {},
        onClickTimeline = {},
        currentSelectItem = NavigationBarItems.Timeline,
    )
}

@Composable
private fun CommunityHomeScreen(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState,
    uiState: CommunityHomeState,
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
            contentPadding = PaddingValues(horizontal = 24.dp),
        ) {
            items(uiState.datas, key = {it.id}) {
                Column {
                    CommunityListItem(vegeItem = it)
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
                    if (uiState.isAutoAppendLoading) {
                        Text(
                            text = "loading中..."
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CommunityHomeScreenPreview() {
    VegegrowthAppTheme {
        Surface {
            CommunityHomeScreen(
                onClickHome = {},
                onClickTimeline = {},
                lazyListState = rememberLazyListState(),
                currentSelectItem = NavigationBarItems.Timeline,
                uiState = CommunityHomeState.initial()
            )
        }
    }
}
