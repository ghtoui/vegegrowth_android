package com.moritoui.vegegrowthapp.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.util.UUID

class VegeItemDetailTest {
    @Test
    fun getDiffDatetime() {
        val vegeItemDetail =
            VegeItemDetail(
                itemUuid = UUID.randomUUID().toString(),
                uuid = UUID.randomUUID().toString(),
                name = "name",
                size = 4.3,
                memo = "",
                date = "2023-09-29 20:30:14",
            )
        val diffDate = vegeItemDetail.getDiffDatetime("2023-09-25 20:30:14")
        val ansDiffDate = "4"

        assertThat(ansDiffDate).isEqualTo(diffDate)
    }
}
