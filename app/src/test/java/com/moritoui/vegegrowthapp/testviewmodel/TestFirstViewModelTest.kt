package com.moritoui.vegegrowthapp.testviewmodel

import com.google.common.truth.Truth.assertThat
import com.moritoui.vegegrowthapp.model.SortStatus
import com.moritoui.vegegrowthapp.model.VegeCategory
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegeStatus
import org.junit.Test

class TestFirstViewModelTest {
    private val viewModel = TestFirstViewModel()
    @Test
    fun checkInputText() {
        val checkTest: Map<Boolean, Boolean> = mapOf(
            viewModel.checkInputText("test") to true,
            viewModel.checkInputText("") to false
        )
        checkTest.forEach { (test, ans) ->
            assertThat(test).isEqualTo(ans)
        }
    }

    @Test
    fun checkSortList() {
        val sortList = viewModel.sortList(
            sortStatus = SortStatus.Flower,
            itemList = viewModel.vegeItemList
        )
        val ansList: List<VegeItem> = listOf(
            VegeItem(
                name = "花1",
                category = VegeCategory.Flower,
                uuid = ("528bdecc-d6e4-4721-a853-db24d384fc75"),
                status = VegeStatus.Default
            ),
            VegeItem(
                name = "花2",
                category = VegeCategory.Flower,
                uuid = ("321bdecc-d6e4-4721-a853-db24d384fc75"),
                status = VegeStatus.Default
            ),
            VegeItem(
                name = "花3",
                category = VegeCategory.Flower,
                uuid = ("326bdecc-d6e4-4721-a853-db24d384fc75"),
                status = VegeStatus.Default
            )
        )

        assertThat(sortList).isEqualTo(ansList)
    }
}
