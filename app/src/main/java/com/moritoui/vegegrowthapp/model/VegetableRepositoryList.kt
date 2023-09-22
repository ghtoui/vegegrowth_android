package com.moritoui.vegegrowthapp.model

import java.util.UUID

object VegetableRepositoryList {
    fun getVegeRepositoryList(): List<VegetableRepository> {
        val itemUuid = UUID.fromString("9600ce0b-adc9-48c1-9384-098c59976eb5").toString()
        return listOf(
            VegetableRepository(
                itemUuid = itemUuid,
                uuid = UUID.randomUUID().toString(),
                name = "トマト",
                size = 0.0,
                memo = "トマトの種を土に植えました。水をあげました"
            ),
            VegetableRepository(
                itemUuid = itemUuid,
                uuid = UUID.randomUUID().toString(),
                name = "トマト",
                size = 2.0,
                memo = "小さな苗ができました。"
            ),
            VegetableRepository(
                itemUuid = itemUuid,
                uuid = UUID.randomUUID().toString(),
                name = "トマト",
                size = 3.0,
                memo = "苗が少し大きくなりました。"
            ),
            VegetableRepository(
                itemUuid = itemUuid,
                uuid = UUID.randomUUID().toString(),
                name = "トマト",
                size = 4.0,
                memo = "葉っぱが生えて大きくなりました。"
            ),
            VegetableRepository(
                itemUuid = itemUuid,
                uuid = UUID.randomUUID().toString(),
                name = "トマト",
                size = 5.0,
                memo = "苗がさらに大きくなったので、支柱を立てました。"
            ),
            VegetableRepository(
                itemUuid = itemUuid,
                uuid = UUID.randomUUID().toString(),
                name = "トマト",
                size = 6.0,
                memo = "蕾ができました。"
            ),
            VegetableRepository(
                itemUuid = itemUuid,
                uuid = UUID.randomUUID().toString(),
                name = "トマト",
                size = 7.0,
                memo = "蕾から花が咲きました。"
            ),
            VegetableRepository(
                itemUuid = itemUuid,
                uuid = UUID.randomUUID().toString(),
                name = "トマト",
                size = 8.0,
                memo = "緑の小さいトマトができました。"
            ),
            VegetableRepository(
                itemUuid = itemUuid,
                uuid = UUID.randomUUID().toString(),
                name = "トマト",
                size = 9.0,
                memo = "黄色く丸いトマトまで成長しました。"
            ),
            VegetableRepository(
                itemUuid = itemUuid,
                uuid = UUID.randomUUID().toString(),
                name = "トマト",
                size = 10.0,
                memo = "赤いトマトができたので、収穫しました。"
            )
        )
    }
}