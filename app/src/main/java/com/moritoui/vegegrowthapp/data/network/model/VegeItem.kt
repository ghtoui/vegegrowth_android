package com.moritoui.vegegrowthapp.data.network.model

import com.moritoui.vegegrowthapp.model.VegeCategory
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegeStatus
import java.util.UUID
import kotlinx.serialization.Serializable

/**
 * API から取得する野菜の情報
 */
@Serializable
data class VegeItem(
    val id: Int,
    val name: String,
    val category: Int,
    val status: Int,
) {
    fun toDomain(): VegeItem = VegeItem(
        id = id,
        name = name,
        category = VegeCategory.entries.first { it.value == category },
        status = VegeStatus.entries.first { it.value == status },
        folderId = null,
        uuid = UUID.randomUUID().toString(),
    )
}
