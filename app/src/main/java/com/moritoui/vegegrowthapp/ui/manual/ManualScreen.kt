package com.moritoui.vegegrowthapp.ui.manual

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.moritoui.vegegrowthapp.R
import com.moritoui.vegegrowthapp.ui.analytics.SendScreenEvent
import com.moritoui.vegegrowthapp.ui.manual.view.ManualContent
import com.moritoui.vegegrowthapp.ui.navigation.NavigationAppTopBar
import com.moritoui.vegegrowthapp.ui.theme.VegegrowthAppTheme

@Composable
fun ManualScreen(navController: NavController, viewModel: ManualViewModel = hiltViewModel()) {
    ManualScreen(
        modifier = Modifier,
        backNavigation = navController::popBackStack,
        finishedLookingPage = viewModel::finishedLookingPage,
        finishedReadManual = viewModel::finishedReadManual
    )
    SendScreenEvent(screen = ManualScreenRoute.SCREEN)
}

@Composable
private fun ManualScreen(modifier: Modifier = Modifier, backNavigation: () -> Unit, finishedLookingPage: (Int) -> Unit, finishedReadManual: () -> Unit) {
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
            finishedLookingPage = finishedLookingPage,
            finishReadManual = {
                finishedReadManual()
                backNavigation()
            }
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
                backNavigation = {},
                finishedLookingPage = {},
                finishedReadManual = {}
            )
        }
    }
}
