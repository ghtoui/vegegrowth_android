package com.moritoui.vegegrowthapp.ui.manage.view

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.moritoui.vegegrowthapp.dummies.ManageScreenDummy
import com.moritoui.vegegrowthapp.model.DateFormatter
import com.moritoui.vegegrowthapp.model.VegeItemDetail
import com.moritoui.vegegrowthapp.ui.theme.VegegrowthAppTheme

@Composable
fun DrawLineChart(modifier: Modifier = Modifier, data: List<VegeItemDetail>, currentIndex: Int) {
    Box(
        modifier =
        modifier
            .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp)
            .background(MaterialTheme.colorScheme.primaryContainer.copy(0.1f))
            .border(
                width = 4.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(0.05f)
            )
    ) {
        val chartColor = MaterialTheme.colorScheme.onBackground
        val textColor = MaterialTheme.colorScheme.onBackground
        val formatter by remember { mutableStateOf(DateFormatter()) }
        val textMeasurer = rememberTextMeasurer()
        Canvas(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            var pointX: Float? = null
            var pointY: Float? = null
            var pointTextX: Float? = null
            var pointTextY: Float? = null
            var detailData = ""

            // 初期化
            val minX = data.minOf { formatter.stringToEpochTime(it.date) }
            val maxX = data.maxOf { formatter.stringToEpochTime(it.date) }
            val minY = data.minOf { it.size }
            val maxY = data.maxOf { it.size }
            val scaleX = size.width / (maxX - minX)
            val scaleY = size.height / (maxY - minY)
            val path = Path()

            data.forEachIndexed { index, item ->
                val x = (formatter.stringToEpochTime(item.date) - minX) * scaleX
                val y = (size.height - (item.size - minY) * scaleY).toFloat()
                if (currentIndex == index) {
                    pointX = x
                    pointY = y
                    detailData =
                        "${item.date}\n${item.getDiffDatetime(data.first().date)}日目\n${item.size}cm"
                }

                // 最初ならパスの開始点, それ以外は直線を描画していく
                when (index) {
                    0 -> path.moveTo(x, y)
                    else -> path.lineTo(x, y)
                }
            }

            // パスの描画
            drawPath(
                path = path,
                color = chartColor,
                style = Stroke(width = 4f)
            )

            if (pointX != null) {
                // データが1つだけの時の処理
                if (pointX!!.isNaN() || pointY!!.isNaN()) {
                    pointX = 0.toFloat()
                    // ここでsizeがNaNになっているからエラーが出る？
                    pointY = size.height
                }
                if (pointX!! < size.width / 2 && pointY!! < size.height / 2) { // 左上
                    pointTextX = pointX!! + 20
                    pointTextY = pointY!! + 30
                } else if (pointX!! > size.width / 2 && pointY!! < size.height / 2) { // 右上
                    pointTextX = pointX!! - 400
                    pointTextY = pointY!! + 30
                } else if (pointX!! < size.width / 2 && pointY!! > size.height / 2) { // 左下
                    pointTextX = pointX!! + 20
                    pointTextY = pointY!! - 150
                } else { // 右下
                    pointTextX = pointX!! - 400
                    pointTextY = pointY!! - 150
                }

                if (pointTextX.isNaN() || pointTextY.isNaN()) {
                    pointX = 0.toFloat()
                    pointY = 0.toFloat()
                    pointTextX = 20.toFloat()
                    pointTextY = 30.toFloat()
                }

                // 点の描画
                drawCircle(
                    color = Color.Red,
                    radius = 16f,
                    center = Offset(pointX!!, pointY!!)
                )
                try {
                    drawText(
                        textMeasurer = textMeasurer,
                        text = detailData,
                        topLeft = Offset(x = pointTextX, y = pointTextY),
                        style = TextStyle.Default.copy(color = textColor)
                    )
                } catch (e: Exception) {
                    Log.e("error", e.toString())
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun DrawLineChartPreview() {
    VegegrowthAppTheme {
        DrawLineChart(
            data = ManageScreenDummy.getVegetableDetailList(),
            currentIndex = 8
        )
    }
}
