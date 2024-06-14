package com.moritoui.vegegrowthapp.testviewmodel

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class TestTakePictureViewModelTest {
    @Test
    fun checkInputText() {
        val viewModel = TestTakePictureViewModel()
        val checkTest: Map<Boolean, Boolean> =
            mapOf(
                viewModel.checkInputText("test") to false,
                viewModel.checkInputText("12.3") to true,
                viewModel.checkInputText("12") to true,
                viewModel.checkInputText("") to false,
                viewModel.checkInputText("0") to true,
            )

        checkTest.forEach { (test, ans) ->
            assertThat(test).isEqualTo(ans)
        }
    }
}
