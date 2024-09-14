package com.moritoui.vegegrowthapp.ui.common.drawer

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.moritoui.vegegrowthapp.ui.theme.VegegrowthAppTheme

@Composable
fun VegeGrowthNavigationDrawer(modifier: Modifier = Modifier, drawerState: DrawerState, onManualClick: () -> Unit, content: @Composable () -> Unit) {
    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        drawerContent = {
            DrawerContents(
                modifier = Modifier,
                onManualClick = onManualClick
            )
        },
        scrimColor = MaterialTheme.colorScheme.surfaceContainer,
        content = content
    )
}

@Preview
@Composable
private fun VegeGrowthNavigationDrawerPreview() {
    VegegrowthAppTheme {
        Surface {
            VegeGrowthNavigationDrawer(
                modifier = Modifier,
                drawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
                onManualClick = {},
                content = {}
            )
        }
    }
}
