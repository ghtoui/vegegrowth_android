package com.moritoui.vegegrowthapp.ui.timeline.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moritoui.vegegrowthapp.model.VegeCategory
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegeStatus
import com.moritoui.vegegrowthapp.model.VegeStatusMethod
import com.moritoui.vegegrowthapp.model.getIconId
import com.moritoui.vegegrowthapp.ui.theme.VegegrowthAppTheme
import java.util.UUID

@Composable
fun TimelineHomeListItem(vegeItem: VegeItem, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(100.dp)
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${vegeItem.name}"
            )
            Spacer(modifier = Modifier.width(8.dp))
            vegeItem.category.getIconId()?.let {
                Icon(
                    painter = painterResource(id = it),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                painter = painterResource(id = VegeStatusMethod.getIconId(vegeItem.status)),
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
private fun TimelineHomeListItemPreview() {
    VegegrowthAppTheme {
        Surface {
            TimelineHomeListItem(
                vegeItem = VegeItem(
                    id = 0,
                    name = "test",
                    status = VegeStatus.Default,
                    category = VegeCategory.Leaf,
                    folderId = null,
                    uuid = UUID.randomUUID().toString()
                )
            )
        }
    }
}
