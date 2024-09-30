package com.moritoui.vegegrowthapp.usecases

import androidx.datastore.core.DataStore
import com.moritoui.vegegrowthapp.data.datastores.appsettings.AppSettingsPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * 登録するときに日付をユーザが指定するかどうか
 */
class IsRegisterSelectDateUseCase @Inject constructor(private val appSettingsPreferences: DataStore<AppSettingsPreferences>) {
    operator fun invoke(): Flow<Boolean> = appSettingsPreferences.data.map {
        it.isRegisterSelectDate
    }
}
