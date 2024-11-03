package com.moritoui.vegegrowthapp.ui.community.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
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
        LazyColumn(modifier = modifier) {
            items(list) {
                Column {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        Text(text = it.name)
                        Spacer(modifier = Modifier.weight(1f))
                        Text(text = it.category.name)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = it.status.name)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CommunityHomeScreenPreview() {

}
