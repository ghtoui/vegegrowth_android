package com.moritoui.vegegrowthapp.ui.home.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.moritoui.vegegrowthapp.R
import com.moritoui.vegegrowthapp.model.VegeCategory
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegeItemDetail
import com.moritoui.vegegrowthapp.model.VegeStatus
import com.moritoui.vegegrowthapp.model.VegeStatusMethod
import com.moritoui.vegegrowthapp.model.getIcon
import com.moritoui.vegegrowthapp.model.getTint
import com.moritoui.vegegrowthapp.previews.DarkLightPreview
import com.moritoui.vegegrowthapp.ui.theme.VegegrowthAppTheme

@Composable
fun VegeItemListCard(
    modifier: Modifier = Modifier,
    vegetable: VegeItem,
    vegetableDetail: VegeItemDetail?,
    onClick: (Int) -> Unit,
) {
    var showStatus by rememberSaveable { mutableStateOf(vegetable.status) }
    showStatus = vegetable.status

    val statusIcon: ImageVector = VegeStatusMethod.getIcon(showStatus)
    val statusIconTint: Color = VegeStatusMethod.getIconTint(vegeStatus = showStatus) ?: LocalContentColor.current
    val iconTint: Color = vegetable.category.getTint(otherColor = LocalContentColor.current)
    val categoryIcon = vegetable.category.getIcon()

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        onClick = { onClick(vegetable.id) }
    ) {
        Row(
            modifier = modifier
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(16.dp)),
                model = vegetableDetail?.imagePath,
                contentDescription = "最後に撮影された写真",
                error = painterResource(id = R.drawable.no_image),
                // Previewで見えるようにするため
                placeholder = painterResource(R.drawable.loading_image),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (categoryIcon != null) {
                        Icon(
                            painter = painterResource(id = categoryIcon),
                            contentDescription = null,
                            tint = iconTint
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        vegetable.name,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                if (vegetableDetail != null) {
                    Text(
                        vegetableDetail.date,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                statusIcon,
                contentDescription = null,
                tint = statusIconTint,
            )
        }
    }
}

@DarkLightPreview
@Composable
fun VegeItemListCardPreview() {
    VegegrowthAppTheme {
        VegeItemListCard(
            vegetable = VegeItem(
                name = "キャベツ",
                id = 0,
                status = VegeStatus.End,
                category = VegeCategory.Leaf,
            ),
            vegetableDetail = VegeItemDetail(
                name = "キャベツ",
                id = 0,
                date = "2020-04-04",
                vegeItemId = 0,
                memo = "",
                size = 3.3,
                imagePath = "test",
                itemUuid = "",
                uuid = ""
            ),
            onClick = {}
        )
    }
}
