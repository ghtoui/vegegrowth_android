package com.moritoui.vegegrowthapp.ui.manual.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.moritoui.vegegrowthapp.R
import com.moritoui.vegegrowthapp.ui.theme.VegegrowthAppTheme

@Composable
fun PagerTopBar(modifier: Modifier = Modifier, currentPage: Int, pageCount: Int) {
    val selectedPointSize = 24.dp
    val defaultPointSize = 12.dp
    val defaultPointSpace = 40.dp
    val diffPointSize = (selectedPointSize - defaultPointSize) / 2
    val centerOffset = if (currentPage == 0 || currentPage == pageCount) {
        diffPointSize / 2
    } else {
        0.dp
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .offset(x = centerOffset),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        (0 until pageCount).forEach { page ->
            when (page) {
                in 0 until currentPage -> {
                    Box(
                        modifier = Modifier
                            .size(selectedPointSize)
                            .background(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(18.dp),
                            painter = painterResource(id = R.drawable.ic_sprout),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
                currentPage -> {
                    Box(
                        modifier = Modifier
                            .size(defaultPointSize)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            )
                    )
                }
                else -> {
                    Box(
                        modifier = Modifier
                            .size(defaultPointSize)
                            .background(
                                color = MaterialTheme.colorScheme.secondary,
                                shape = CircleShape
                            )
                    )
                }
            }
            val pointSpace = defaultPointSpace - (currentPage - page).let {
                if (it < 0) {
                    0.dp
                } else if (it > 2) {
                    2 * diffPointSize
                } else {
                    it * diffPointSize
                }
            }
            if (page != pageCount - 1) {
                HorizontalDivider(
                    modifier = Modifier.width(pointSpace),
                    thickness = 4.dp,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
            }
        }
    }
}

@Preview
@Composable
private fun PagerTopBarPreview() {
    VegegrowthAppTheme {
        Surface {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                repeat(3 + 1) {
                    PagerTopBar(
                        modifier = Modifier,
                        currentPage = it,
                        pageCount = 3
                    )
                }
            }
        }
    }
}
