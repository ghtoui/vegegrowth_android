package com.moritoui.vegegrowthapp.ui.community.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.ui.community.home.components.CommunityListItem

/**
 * 掲示板のホーム
 */
@Composable
fun CommunityHomeScreen(
    navController: NavController,
    viewModel: CommunityHomeViewModel = hiltViewModel(),
) {
    val list by viewModel.listState.collectAsStateWithLifecycle()
    CommunityHomeScreen(
        modifier = Modifier,
        list = list,
        onReloadClick = viewModel::getList
    )
}

@Composable
private fun CommunityHomeScreen(
    modifier: Modifier = Modifier,
    list: List<VegeItem>,
    onReloadClick: () -> Unit,
) {
    Column {
        Button(
            modifier = Modifier
                .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()),
            onClick = onReloadClick
        ) {
            Text("reload")
        }
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(horizontal = 24.dp),
        ) {
            items(list) {
                Column {
                    CommunityListItem(vegeItem = it)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Preview
@Composable
private fun CommunityHomeScreenPreview() {

}
