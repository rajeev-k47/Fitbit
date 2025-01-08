package net.runner.fitbit.auth.extendedComposables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import net.runner.fitbit.ui.theme.background
import net.runner.fitbit.ui.theme.lightBlueText
import net.runner.fitbit.ui.theme.lightText
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerButton(
    state:String,
    onConfirm: (TimePickerState,state: String) -> Unit,
    onDismiss: () -> Unit,
) {

    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = false,
    )

    TimePickerDialog(
        onDismiss = { onDismiss() },
        onConfirm = { onConfirm(timePickerState,state) }
    ) {
        TimePicker(
            state = timePickerState,
            colors = TimePickerDefaults.colors(
                clockDialColor = background.copy(alpha = 0.5f),
                selectorColor = MaterialTheme.colorScheme.secondary,
                periodSelectorBorderColor = MaterialTheme.colorScheme.secondary,
                periodSelectorUnselectedContainerColor = lightText.copy(alpha = 0.1f),
                periodSelectorSelectedContainerColor = background.copy(alpha = 0.8f),
                timeSelectorSelectedContainerColor = background.copy(alpha = 0.8f),
                timeSelectorUnselectedContainerColor = lightText.copy(alpha = 0.1f),
                timeSelectorSelectedContentColor = lightText,
                timeSelectorUnselectedContentColor = lightBlueText,

            )
        )
    }
}

@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = lightText
                )
            ) {
                Text("Dismiss")
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text("OK")
            }
        },
        text = { content() }
    )
}
