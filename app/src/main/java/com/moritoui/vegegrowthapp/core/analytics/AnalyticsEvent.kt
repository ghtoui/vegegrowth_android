package com.moritoui.vegegrowthapp.core.analytics

import com.moritoui.vegegrowthapp.model.VegeCategory
import com.moritoui.vegegrowthapp.model.VegeStatus

/**
 * 分析イベント
 *
 * @param event: できるだけ，標準のイベントを設定する.適切なものがなかったらカスタムイベントを定義する．
 * @param params: イベントに追加のコンテキストを提供するパラメータのリスト
 */
data class AnalyticsEvent(
    val event: String,
    val params: List<Param> = emptyList(),
) {
    /**
     * key, valueペアのイベント
     *
     * @param key: パラメータキー, できるだけ標準のPramKeysのいずれかを使用する.適切なものがなかったら，カスタムキーを定義する．
     * @param value: パラメータ値
     */
    data class Param(val key: String, val value: String)

    object Analytics {
        /**
         * ダイアログを開いた
         */
        fun openDialog(dialogType: String): AnalyticsEvent = AnalyticsEvent(
            event = Events.OPEN_DIALOG.name,
            params = listOf(
                Param(
                    key = Params.DIALOG_TYPE.name,
                    value = dialogType,
                ),
            )
        )

        /**
         * ダイアログをキャンセルした
         */
        fun cancelDialog(dialogType: String): AnalyticsEvent = AnalyticsEvent(
            event = Events.CANCEL_DIALOG.name,
            params = listOf(
                Param(
                    key = Params.DIALOG_TYPE.name,
                    value = dialogType,
                ),
            )
        )
        /*
        * ダイアログを確認した(登録)
        */
        fun confirmDialog(dialogType: String): AnalyticsEvent = AnalyticsEvent(
            event = Events.CONFIRM_DIALOG.name,
            params = listOf(
                Param(
                    key = Params.DIALOG_TYPE.name,
                    value = dialogType,
                ),
            )
        )

        /**
         * 管理するアイテムを作成
         */
        fun createItem(itemName: String, category: VegeCategory): AnalyticsEvent = AnalyticsEvent(
            event = Events.CREATE_ITEM.name,
            params = listOf(
                Param(
                    key = Params.ITEM_NAME.name,
                    value = itemName,
                ),
                Param(
                    key = Params.CATEGORY.name,
                    value = category.name,
                ),
            )
        )

        /**
         * 写真を撮影した
         */
        fun takePicture(): AnalyticsEvent = AnalyticsEvent(
            event = Events.TAKE_PICTURE.name,
            params = listOf(
                Param(
                    key = Params.COUNT.name,
                    value = "take_picture_count",
                ),
            )
        )

        /**
         * マニュアルのページを閲覧
         */
        fun lookManualPage(page: String): AnalyticsEvent = AnalyticsEvent(
            event = Events.MANUAL.name,
            params = listOf(
                Param(
                    key = Params.PAGE.name,
                    value = page,
                ),
            )
        )

        /**
         * アイテムの削除
         */
        fun deleteItem(itemName: String): AnalyticsEvent = AnalyticsEvent(
            event = Events.DELETE_ITEM.name,
            params = listOf(
                Param(
                    key = Params.ITEM_NAME.name,
                    value = itemName,
                ),
            )
        )

        /**
         * フォルダの作成
         */
        fun createFolder(folderName: String): AnalyticsEvent = AnalyticsEvent(
            event = Events.CREATE_FOLDER.name,
            params = listOf(
                Param(
                    key = Params.FOLDER_NAME.name,
                    value = folderName,
                ),
            )
        )

        /**
         * フォルダの削除
         */
        fun deleteFolder(folderName: String): AnalyticsEvent = AnalyticsEvent(
            event = Events.DELETE_FOLDER.name,
            params = listOf(
                Param(
                    key = Params.FOLDER_NAME.name,
                    value = folderName,
                ),
            )
        )

        /**
         * フォルダの移動
         */
        fun moveFolder(folderName: String, itemName: String): AnalyticsEvent = AnalyticsEvent(
            event = Events.MOVE_FOLDER.name,
            params = listOf(
                Param(
                    key = Params.FOLDER_NAME.name,
                    value = folderName,
                ),
                Param(
                    key = Params.ITEM_NAME.name,
                    value = itemName,
                ),
            )
        )

        /**
         * ステータスの変更
         */
        fun changeStatus(status: VegeStatus, itemName: String): AnalyticsEvent = AnalyticsEvent(
            event = Events.CHANGE_STATUS.name,
            params = listOf(
                Param(
                    key = Params.STATUS.name,
                    value = status.name,
                ),
                Param(
                    key = Params.ITEM_NAME.name,
                    value = itemName,
                ),
            )
        )
    }
}

enum class Events {
    CONFIRM_DIALOG,
    CANCEL_DIALOG,
    OPEN_DIALOG,
    CREATE_ITEM,
    DELETE_ITEM,
    CREATE_FOLDER,
    DELETE_FOLDER,
    MOVE_FOLDER,
    CHANGE_STATUS,
    TAKE_PICTURE,
    MANUAL,

}
enum class Params {
    ITEM_NAME,
    FOLDER_NAME,
    DIALOG_TYPE,
    STATUS,
    CATEGORY,
    COUNT,
    PAGE,
}
