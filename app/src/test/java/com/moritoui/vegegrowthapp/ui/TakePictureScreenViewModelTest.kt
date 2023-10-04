package com.moritoui.vegegrowthapp.ui

import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test

class TakePictureScreenViewModelTest {

    @Test
    fun checkInputText() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val viewModel = TakePictureScreenViewModel(
            index = 0,
            sortText = "Flower",
            applicationContext = appContext
        )
    }
}
