package com.moritoui.vegegrowthapp.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.moritoui.vegegrowthapp.R

/**
 * ボトムナビゲーションバーのアイテム
 */
enum class NavigationBarItems(
    @DrawableRes val iconResId: Int,
    @DrawableRes val disableIconResId: Int,
    @StringRes val labelResId: Int,
) {
    /**
     * ホーム
     */
    Home(
        iconResId = R.drawable.ic_home,
        disableIconResId = R.drawable.ic_home_disable,
        labelResId = R.string.navigation_bar_home,
    ),

    /**
     * タイムライン
     */
    Timeline(
        iconResId = R.drawable.ic_timeline,
        disableIconResId = R.drawable.ic_timeline_disable,
        labelResId = R.string.navigation_bar_timeline,
    )
}
