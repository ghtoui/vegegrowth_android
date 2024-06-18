package com.moritoui.vegegrowthapp.ui.admob

import android.content.Context
import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun AdmobBanner(modifier: Modifier = Modifier, banner: AdView, context: Context) {
    AndroidView(
        modifier =
        modifier
            .fillMaxWidth(),
        factory = {
            if (banner.parent != null) {
                (banner.parent as FrameLayout).removeAllViews()
            }
            FrameLayout(context).apply {
                minimumHeight = AdSize.BANNER.getHeightInPixels(context)
                addView(banner)
            }
        }
    )
}
