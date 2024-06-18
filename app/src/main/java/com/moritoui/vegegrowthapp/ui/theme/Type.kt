package com.moritoui.vegegrowthapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.moritoui.vegegrowthapp.R

// Set of Material typography styles to start with
@OptIn(ExperimentalTextApi::class)
val notoSansFont =
    FontFamily(
        (100..900 step 100).map { weight ->
            Font(
                resId = R.font.noto_sans_jp_google_font,
                weight = FontWeight(weight),
                variationSettings = FontVariation.Settings(FontWeight(weight), FontStyle.Normal)
            )
        }
    )
val Typography =
    Typography(
        bodyLarge =
        TextStyle(
            fontFamily = notoSansFont,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        )
    )
