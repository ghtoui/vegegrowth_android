package com.moritoui.vegegrowthapp.navigation

import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
data class Folder(
    val folderId: Int,
)

@Serializable
data class TakePicture(
    val vegetableId: Int,
)

@Serializable
data class Manage(
    val vegetableId: Int,
)
