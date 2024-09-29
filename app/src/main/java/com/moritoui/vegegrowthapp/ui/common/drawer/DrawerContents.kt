package com.moritoui.vegegrowthapp.ui.common.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moritoui.vegegrowthapp.R
import com.moritoui.vegegrowthapp.ui.theme.VegegrowthAppTheme

@Composable
fun DrawerContents(
    modifier: Modifier = Modifier,
    onManualClick: () -> Unit,
    onRegisterDateSwitch: (Boolean) -> Unit,
    isRegisterSelectDate: Boolean,
) {
    ModalDrawerSheet {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(72.dp),
                painter = painterResource(id = R.mipmap.ic_launcher_foreground),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = stringResource(id = R.string.app_name))
        }
        HorizontalDivider()
        Spacer(modifier = Modifier.height(8.dp))
        NavigationDrawerItem(
            modifier = modifier,
            label = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(painter = painterResource(id = R.drawable.ic_description), contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = stringResource(id = R.string.drawer_manual))
                }
            },
            selected = false,
            onClick = onManualClick
        )
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(id = R.string.drawer_register_select_date_time))
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = isRegisterSelectDate,
                onCheckedChange = onRegisterDateSwitch
            )
        }
    }
}

@Preview
@Composable
private fun DrawerContentsPreview() {
    VegegrowthAppTheme {
        Surface {
            DrawerContents(
                modifier = Modifier,
                onManualClick = {},
                onRegisterDateSwitch = {},
                isRegisterSelectDate = false,
            )
        }
    }
}
