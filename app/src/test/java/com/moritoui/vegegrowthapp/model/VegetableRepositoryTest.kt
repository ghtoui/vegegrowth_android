package com.moritoui.vegegrowthapp.model

import com.google.common.truth.Truth.assertThat
import java.util.UUID
import org.junit.Test

class VegetableRepositoryTest {

    @Test
    fun getDiffDatetime() {
        val vegetableRepository = VegetableRepository(
            itemUuid = UUID.randomUUID().toString(),
            uuid = UUID.randomUUID().toString(),
            name = "name",
            size = 4.3,
            memo = "",
            date = "2023-09-29 20:30:14"
        )
        val diffDate = vegetableRepository.getDiffDatetime("2023-09-25 20:30:14")
        val ansDiffDate = "4"

        assertThat(ansDiffDate).isEqualTo(diffDate)
    }
}
