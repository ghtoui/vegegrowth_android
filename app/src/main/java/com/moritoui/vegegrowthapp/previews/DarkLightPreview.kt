package com.moritoui.vegegrowthapp.previews

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
annotation class DarkLightPreview
