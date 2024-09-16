package com.moritoui.vegegrowthapp.ui.manual

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.moritoui.vegegrowthapp.R
import com.moritoui.vegegrowthapp.ui.manual.view.ManualContent
import com.moritoui.vegegrowthapp.ui.navigation.NavigationAppTopBar
import com.moritoui.vegegrowthapp.ui.theme.VegegrowthAppTheme

@Composable
fun ManualScreen(navController: NavController) {
    ManualScreen(
        modifier = Modifier,
        backNavigation = navController::popBackStack
    )
}

@Composable
private fun ManualScreen(modifier: Modifier = Modifier, backNavigation: () -> Unit) {
    Scaffold(
        topBar = {
            NavigationAppTopBar(
                title = stringResource(id = R.string.manual_title),
                onBackNavigationButtonClick = backNavigation
            )
        }
    ) { innerPadding ->
        ManualContent(
            modifier = modifier
                .padding(innerPadding)
                .padding(top = 10.dp),
            finishReadManual = backNavigation
        )
    }
}

@Preview
@Composable
private fun ManualScreenPreview() {
    VegegrowthAppTheme {
        Surface {
            ManualScreen(
                modifier = Modifier,
                backNavigation = {}
            )
        }
    }
}
