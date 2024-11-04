package com.moritoui.vegegrowthapp.ui.initialmanual

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.moritoui.vegegrowthapp.ui.analytics.SendScreenEvent
import com.moritoui.vegegrowthapp.ui.home.navigateToHome
import com.moritoui.vegegrowthapp.ui.manual.view.ManualContent
import com.moritoui.vegegrowthapp.ui.theme.VegegrowthAppTheme

@Composable
fun InitialManualScreen(navController: NavController, viewModel: InitialManualViewModel = hiltViewModel()) {
    InitialManualScreen(
        modifier = Modifier,
        navigateToHome = navController::navigateToHome,
        finishedLookingPage = viewModel::finishedLookingPage,
        finishReadManual = viewModel::finishReadManual
    )
}

@Composable
private fun InitialManualScreen(modifier: Modifier = Modifier, navigateToHome: () -> Unit, finishedLookingPage: (Int) -> Unit, finishReadManual: () -> Unit) {
    ManualContent(
        modifier = modifier
            .padding(top = 50.dp),
        finishedLookingPage = finishedLookingPage,
        finishReadManual = {
            finishReadManual()
            navigateToHome()
        }
    )

    SendScreenEvent(screen = InitialManualScreenRoute.SCREEN)
}

@Preview
@Composable
private fun InitialManualScreenPreview() {
    VegegrowthAppTheme {
        Surface {
            InitialManualScreen(
                modifier = Modifier,
                navigateToHome = {},
                finishedLookingPage = {},
                finishReadManual = {}
            )
        }
    }
}
