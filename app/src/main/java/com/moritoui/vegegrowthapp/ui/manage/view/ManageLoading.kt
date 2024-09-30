package com.moritoui.vegegrowthapp.ui.manage.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.moritoui.vegegrowthapp.ui.common.EmptyListVegeGrowthLoading
import com.moritoui.vegegrowthapp.ui.common.VegeGrowthLoading

@Composable
fun ManageLoading(isLoaded: Boolean, onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    if (isLoaded) {
        EmptyListVegeGrowthLoading(
            modifier = modifier
                .fillMaxSize(),
            onBackClick = onBackClick
        )
    } else {
        VegeGrowthLoading(
            modifier = modifier
                .fillMaxSize()
        )
    }
}
