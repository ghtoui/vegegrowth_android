package com.moritoui.vegegrowthapp.ui.startup.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moritoui.vegegrowthapp.R
import com.moritoui.vegegrowthapp.navigation.HomeAddItem
import com.moritoui.vegegrowthapp.ui.theme.VegegrowthAppTheme
import java.time.LocalDateTime

@Composable
fun FirstPage(
    modifier: Modifier = Modifier
) {
    Column {
        Row(
            modifier = Modifier
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .border(
                        BorderStroke(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                        ),
                        shape = CircleShape,
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(id = R.string.one_number))
            }
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = stringResource(id = R.string.manual_add_button),
            )
        }
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = stringResource(R.string.home_screen_title),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.weight(1f))
                HomeAddItem(
                    onFolderAddClick = {},
                    onAddClick = {}
                )
            }
            Icon(
                modifier = Modifier
                    .padding(top = 60.dp, end = 2.dp, bottom = 10.dp)
                    .size(70.dp)
                    .align(Alignment.CenterEnd),
                painter = painterResource(id = R.drawable.ic_touch),
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
private fun FirstPagePreview() {
    VegegrowthAppTheme {
        Surface {
            FirstPage()
        }
    }
}

@Composable
fun SecondPage(
    modifier: Modifier = Modifier
) {
    Column {
        Row(
            modifier = Modifier
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .border(
                        BorderStroke(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                        ),
                        shape = CircleShape,
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(id = R.string.two_number))
            }
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = stringResource(id = R.string.manual_tap_item),
            )
        }
        ListItemManual()
    }
}

@Preview
@Composable
private fun SecondPagePreview() {
    VegegrowthAppTheme {
        Surface {
            SecondPage()
        }
    }
}

@Composable
private fun ListItemManual(
    modifier: Modifier = Modifier
) {
    Box {
        Card(
            modifier = modifier
                .padding(6.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            onClick = { },
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Row(
                modifier = modifier
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(70.dp),
                    painter = painterResource(id = R.drawable.flower_sample_image),
                    contentDescription = null,
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.flower),
                            contentDescription = null,
                            tint = Color.Red
                        )
                        Text(stringResource(id = R.string.manual_daria_name))
                    }
                    Text(
                        "最後の登録日 ${LocalDateTime.now().toString().take(10)}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
        Icon(
            modifier = Modifier
                .padding(top = 60.dp, bottom = 10.dp)
                .size(70.dp)
                .align(Alignment.BottomCenter),
            painter = painterResource(id = R.drawable.ic_touch),
            contentDescription = null
        )
    }
}

@Composable
fun ThirdPage(
    modifier: Modifier = Modifier
) {
    Column {
        Row(
            modifier = Modifier
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .border(
                        BorderStroke(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                        ),
                        shape = CircleShape,
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(id = R.string.third_number))
            }
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = stringResource(id = R.string.manual_take_picture),
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.height(200.dp),
                painter = painterResource(id = R.drawable.flower_sample_image),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {},
                enabled = false
            ) {
                Text(stringResource(id = R.string.take_picture_button))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box {
                Button(onClick = {}) {
                    Text(stringResource(id = R.string.register_button))
                }
                Icon(
                    modifier = Modifier
                        .padding(top = 30.dp, bottom = 10.dp)
                        .size(70.dp)
                        .align(Alignment.BottomCenter),
                    painter = painterResource(id = R.drawable.ic_touch),
                    contentDescription = null
                )
            }
        }
    }
}

@Preview
@Composable
private fun ThirdPagePreview () {
    VegegrowthAppTheme {
        Surface {
            ThirdPage()
        }
    }
}
