package com.moritoui.vegegrowthapp.ui.home.model

sealed class AddDialogType {
    object AddVegeItem : AddDialogType()
    object AddFolder : AddDialogType()
    object NotOpenDialog : AddDialogType()
}
