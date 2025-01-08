package net.runner.fitbit.auth.extendedComposables.organizer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.runner.fitbit.statesOfIndia
import net.runner.fitbit.ui.theme.background
import net.runner.fitbit.ui.theme.lightBlueText
import net.runner.fitbit.ui.theme.lightText


@Composable
fun ShowStatesIndia(showDialog:Boolean, onDismiss:(Boolean)->Unit,onresult:(String)->Unit) {
    var selectedState by rememberSaveable { mutableStateOf("") }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss(false) },
            title = { Text(text = "Select State") },
            containerColor = background,
            textContentColor = lightBlueText,
            text = {
                LazyColumn(
                    modifier = Modifier.fillMaxHeight(0.4f)
                ) {
                    items(statesOfIndia.size){

                        StateRadioButton(
                            state = statesOfIndia[it],
                            selectedstate = selectedState,
                            onSelected = { selectedState = statesOfIndia[it] }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onDismiss(false)
                    onresult(selectedState)
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss(false) }, colors = ButtonDefaults.textButtonColors(contentColor = lightText)) {
                    Text("Cancel")
                }
            }
        )
    }
}


@Composable
fun StateRadioButton(
    state: String,
    selectedstate: String,
    onSelected: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelected),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = (state == selectedstate),
            onClick = onSelected,
            colors = RadioButtonDefaults.colors(
                selectedColor = lightBlueText
            )
        )
        Text(text = state, modifier = Modifier.padding(start = 8.dp))
    }
}