package com.moritoui.vegegrowthapp.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.moritoui.vegegrowthapp.R

@Composable
fun VegeGrowthLoading(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.vegetable_loading))
    when (isLoading) {
        true -> {
            Box(
                modifier =
                    modifier
                        .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                LottieAnimation(
                    modifier = Modifier.size(100.dp),
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                )
            }
        }
        false -> return
    }
}

@PreviewLightDark
@Composable
private fun VegeGrowthLoadingPreview() {
    VegeGrowthLoading(isLoading = true)
}
