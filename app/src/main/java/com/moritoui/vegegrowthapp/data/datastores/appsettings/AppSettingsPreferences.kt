package com.moritoui.vegegrowthapp.data.datastores.appsettings

import kotlinx.serialization.Serializable

/**
 * アプリの設定
 *
 * @property isInitialStartApp 初回起動かどうか
 * @property isRegisterSelectDate 登録する日付を自分で指定するかどうか
 */
@Serializable
data class AppSettingsPreferences(
    val isInitialStartApp: Boolean = true,
    val isRegisterSelectDate: Boolean = false,
)
