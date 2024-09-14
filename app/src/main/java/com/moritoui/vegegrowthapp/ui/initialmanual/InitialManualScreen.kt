package com.moritoui.vegegrowthapp.ui.initialmanual

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.moritoui.vegegrowthapp.ui.home.navigateToHome
import com.moritoui.vegegrowthapp.ui.manual.view.ManualContent
import com.moritoui.vegegrowthapp.ui.theme.VegegrowthAppTheme

@Composable
fun InitialManualScreen(
    navController: NavController,
    viewModel: InitialManualViewModel = hiltViewModel(),
) {
    InitialManualScreen(
        modifier = Modifier,
        navigateToHome = navController::navigateToHome,
        finishReadManual = viewModel::finishReadManual
    )
}

@Composable
private fun InitialManualScreen(
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit,
    finishReadManual: () -> Unit,
) {
    ManualContent(
        modifier = modifier,
        finishReadManual = {
            navigateToHome()
            finishReadManual()
        }
    )
}

@Preview
@Composable
private fun InitialManualScreenPreview() {
    VegegrowthAppTheme {
        Surface {
            InitialManualScreen(
                modifier = Modifier,
                navigateToHome = {},
                finishReadManual = {}
            )
        }
    }
}
