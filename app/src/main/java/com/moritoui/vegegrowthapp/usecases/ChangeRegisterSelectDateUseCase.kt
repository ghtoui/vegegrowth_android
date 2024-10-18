package com.moritoui.vegegrowthapp.usecases

import androidx.datastore.core.DataStore
import com.moritoui.vegegrowthapp.data.datastores.appsettings.AppSettingsPreferences
import javax.inject.Inject

/**
 * 登録するときに日付をユーザが指定するかどうかを変更する
 */
class ChangeRegisterSelectDateUseCase @Inject constructor(private val appSettingsPreferences: DataStore<AppSettingsPreferences>) {
    suspend operator fun invoke(isRegisterSelectDate: Boolean) {
        appSettingsPreferences.updateData {
            it.copy(
                isRegisterSelectDate = isRegisterSelectDate
            )
        }
    }
}
