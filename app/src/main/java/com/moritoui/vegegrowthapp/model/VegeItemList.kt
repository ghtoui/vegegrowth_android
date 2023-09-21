package com.moritoui.vegegrowthapp.model

import java.util.UUID

object VegeItemList {
    fun getVegeList(): List<VegeItem> {
        return listOf(
            VegeItem(
                name = "レタス",
                category = VegeCategory.Leaf,
                uuid = UUID.fromString("9600ce0b-adc9-48c1-9384-098c59976eb5")
            ),
            VegeItem(
                name = "キャベツ",
                category = VegeCategory.Leaf,
                uuid = UUID.fromString("525bdecc-d6e4-4721-a853-db24d384fc75")
            ),
            VegeItem(
                name = "トマト",
                category = VegeCategory.Leaf,
                uuid = UUID.fromString("526bdecc-d6e4-4721-a853-db24d384fc75")
            ),
            VegeItem(
                name = "きゅうり",
                category = VegeCategory.Leaf,
                uuid = UUID.fromString("527bdecc-d6e4-4721-a853-db24d384fc75")
            ),
            VegeItem(
                name = "花",
                category = VegeCategory.Flower,
                uuid = UUID.fromString("528bdecc-d6e4-4721-a853-db24d384fc75")
            ),
            VegeItem(
                name = "レタス",
                category = VegeCategory.Leaf,
                uuid = UUID.fromString("529bdecc-d6e4-4721-a853-db24d384fc75")
            ),
            VegeItem(
                name = "キャベツ",
                category = VegeCategory.Leaf,
                uuid = UUID.fromString("129bdecc-d6e4-4721-a853-db24d384fc75")
            ),
            VegeItem(
                name = "トマト",
                category = VegeCategory.Leaf,
                uuid = UUID.fromString("229bdecc-d6e4-4721-a853-db24d384fc75")
            ),
            VegeItem(
                name = "きゅうり",
                category = VegeCategory.Leaf,
                uuid = UUID.fromString("329bdecc-d6e4-4721-a853-db24d384fc75")
            ),
            VegeItem(
                name = "花",
                category = VegeCategory.Flower,
                uuid = UUID.fromString("321bdecc-d6e4-4721-a853-db24d384fc75")
            ),
            VegeItem(
                name = "レタス",
                category = VegeCategory.Leaf,
                uuid = UUID.fromString("322bdecc-d6e4-4721-a853-db24d384fc75")
            ),
            VegeItem(
                name = "キャベツ",
                category = VegeCategory.Leaf,
                uuid = UUID.fromString("323bdecc-d6e4-4721-a853-db24d384fc75")
            ),
            VegeItem(
                name = "トマト",
                category = VegeCategory.Leaf,
                uuid = UUID.fromString("324bdecc-d6e4-4721-a853-db24d384fc75")
            ),
            VegeItem(
                name = "きゅうり",
                category = VegeCategory.Leaf,
                uuid = UUID.fromString("325bdecc-d6e4-4721-a853-db24d384fc75")
            ),
            VegeItem(
                name = "花",
                category = VegeCategory.Flower,
                uuid = UUID.fromString("326bdecc-d6e4-4721-a853-db24d384fc75")
            ),
        )
    }
}
