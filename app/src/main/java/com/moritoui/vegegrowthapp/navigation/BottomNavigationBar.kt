package com.moritoui.vegegrowthapp.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.moritoui.vegegrowthapp.ui.theme.VegegrowthAppTheme

@Composable
fun VegeGrowthBottomNavigationBar(
    currentSelectItem: NavigationBarItems,
    onClickHome: () -> Unit,
    onClickTimeline: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier,
    ) {
        NavigationBarItems.entries.forEach {
            BottomBarItem(
                selected = it == currentSelectItem,
                onItemClick = when (it) {
                    NavigationBarItems.Home -> onClickHome
                    NavigationBarItems.Timeline -> onClickTimeline
                },
                iconResId = it.iconResId,
                disableIconResId = it.disableIconResId,
                labelResId = it.labelResId,
            )
        }
    }
}

@Composable
private fun RowScope.BottomBarItem(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onItemClick: () -> Unit,
    @DrawableRes iconResId: Int,
    @DrawableRes disableIconResId: Int,
    @StringRes labelResId: Int
) {
    NavigationBarItem(
        modifier = modifier,
        colors = NavigationBarItemDefaults.colors(
            indicatorColor = Color.Transparent
        ),
        selected = selected,
        onClick = onItemClick,
        icon = {
            Icon(
                painter = painterResource(
                    id =
                    when(selected) {
                        true -> iconResId
                        false -> disableIconResId
                    }
                ),
                contentDescription = null,
            )
        },
        label = {
            Text(text = stringResource(id = labelResId))
        }
    )
}

@Preview
@Composable
private fun BottomBarItemPreview(
    @PreviewParameter(BottomBarItemPPP::class) params: BottomBarItemPPP.Parameter
) {
    VegegrowthAppTheme {
        Surface {
            VegeGrowthBottomNavigationBar(
                currentSelectItem = params.currentSelectItem,
                onClickHome = {},
                onClickTimeline = {},
            )
        }
    }
}

private class BottomBarItemPPP : CollectionPreviewParameterProvider<BottomBarItemPPP.Parameter>(
    listOf(
        Parameter(NavigationBarItems.Home),
        Parameter(NavigationBarItems.Timeline),
    )
) {
    data class Parameter(
        val currentSelectItem: NavigationBarItems
    )
}
