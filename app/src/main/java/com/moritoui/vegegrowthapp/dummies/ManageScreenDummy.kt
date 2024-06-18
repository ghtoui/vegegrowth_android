package com.moritoui.vegegrowthapp.dummies

import com.moritoui.vegegrowthapp.model.VegeItemDetail
import java.util.UUID

object ManageScreenDummy {
    fun getVegetableDetailList(): List<VegeItemDetail> {
        val itemUuid = UUID.fromString("9600ce0b-adc9-48c1-9384-098c59976eb5").toString()
        return listOf(
            VegeItemDetail(
                itemUuid = itemUuid,
                uuid = UUID.randomUUID().toString(),
                name = "トマト",
                size = 0.0,
                memo = "トマトの種を土に植えました。水をあげました",
                date = "2023-09-22 20:30:14",
                imagePath = "",
                vegeItemId = 0,
                id = 0
            ),
            VegeItemDetail(
                itemUuid = itemUuid,
                uuid = UUID.randomUUID().toString(),
                name = "トマト",
                size = 2.0,
                memo = "小さな苗ができました。",
                date = "2023-09-23 20:30:14",
                imagePath = "",
                vegeItemId = 1,
                id = 1
            ),
            VegeItemDetail(
                itemUuid = itemUuid,
                name = "トマト",
                uuid = UUID.randomUUID().toString(),
                size = 3.0,
                memo = "苗が少し大きくなりました。",
                date = "2023-09-24 20:30:14",
                imagePath = "",
                vegeItemId = 2,
                id = 2
            ),
            VegeItemDetail(
                itemUuid = itemUuid,
                uuid = UUID.randomUUID().toString(),
                name = "トマト",
                size = 4.0,
                memo = "葉っぱが生えて大きくなりました。",
                date = "2023-09-25 20:30:14",
                imagePath = "",
                vegeItemId = 3,
                id = 3
            ),
            VegeItemDetail(
                itemUuid = itemUuid,
                uuid = UUID.randomUUID().toString(),
                name = "トマト",
                size = 5.0,
                memo = "苗がさらに大きくなったので、支柱を立てました。",
                date = "2023-09-26 20:30:14",
                imagePath = "",
                vegeItemId = 4,
                id = 4
            ),
            VegeItemDetail(
                itemUuid = itemUuid,
                uuid = UUID.randomUUID().toString(),
                name = "トマト",
                size = 6.0,
                memo = "蕾ができました。",
                date = "2023-09-27 20:30:14",
                imagePath = "",
                vegeItemId = 5,
                id = 5
            ),
            VegeItemDetail(
                itemUuid = itemUuid,
                uuid = UUID.randomUUID().toString(),
                name = "トマト",
                size = 7.0,
                memo = "蕾から花が咲きました。",
                date = "2023-09-28 20:30:14",
                imagePath = "",
                vegeItemId = 6,
                id = 6
            ),
            VegeItemDetail(
                itemUuid = itemUuid,
                uuid = UUID.randomUUID().toString(),
                name = "トマト",
                size = 8.0,
                memo = "緑の小さいトマトができました。",
                date = "2023-09-29 20:30:14",
                imagePath = "",
                vegeItemId = 7,
                id = 7
            ),
            VegeItemDetail(
                itemUuid = itemUuid,
                uuid = UUID.randomUUID().toString(),
                name = "トマト",
                size = 9.0,
                memo = "黄色く丸いトマトまで成長しました。",
                date = "2023-09-30 20:30:14",
                imagePath = "",
                vegeItemId = 8,
                id = 8
            ),
            VegeItemDetail(
                itemUuid = itemUuid,
                uuid = UUID.randomUUID().toString(),
                name = "トマト",
                size = 10.0,
                memo = "赤いトマトができたので、収穫しました。\na\na\na\na\na\na",
                date = "2023-10-01 20:30:14",
                imagePath = "",
                vegeItemId = 9,
                id = 9
            )
        )
    }

    fun getImagePathList(): List<String> = (0..getVegetableDetailList().size - 1).map { "" }
}
