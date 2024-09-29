package com.moritoui.vegegrowthapp.ui.takepicture.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moritoui.vegegrowthapp.R
import com.moritoui.vegegrowthapp.core.extensions.toDateString
import com.moritoui.vegegrowthapp.ui.theme.VegegrowthAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimeView(
    modifier: Modifier = Modifier,
    isRegisterSelectDate: Boolean,
    onDateSelectClick: (Long?) -> Unit,
    onDismiss: () -> Unit,
) {
    val isVisibleDatePicker = rememberSaveable {
        mutableStateOf(false)
    }
    val datePickerState = rememberDatePickerState()

    if (isRegisterSelectDate) {
        Column(
            modifier = modifier
                .background(MaterialTheme.colorScheme.background)
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .height(64.dp),
                value = datePickerState.selectedDateMillis?.toDateString() ?: "",
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    IconButton(
                        onClick = { isVisibleDatePicker.value = !isVisibleDatePicker.value },
                    ) {
                        Icon(painter = painterResource(id = R.drawable.ic_calendar) , contentDescription = null)
                    }
                }
            )
            if (isVisibleDatePicker.value) {
                DatePickerDialog(
                    onDismissRequest = {
                        onDismiss()
                        isVisibleDatePicker.value = !isVisibleDatePicker.value
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                onDateSelectClick(datePickerState.selectedDateMillis)
                                isVisibleDatePicker.value = !isVisibleDatePicker.value
                                onDismiss()
                            }
                        ) {
                            Text(text = stringResource(id = R.string.register_button))
                        }
                    },
                ) {
                    DatePicker(
                        datePickerState
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun DateTimeViewPreview() {
    val date: MutableState<Long?> = remember {
        mutableStateOf(null)
    }
    VegegrowthAppTheme {
        Surface {
            DateTimeView(
                isRegisterSelectDate = true,
                onDateSelectClick = { date.value = it },
                onDismiss = {}
            )
        }
    }
}
