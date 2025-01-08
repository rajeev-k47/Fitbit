package net.runner.fitbit.auth.extendedComposables.workoutBuddy

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.runner.fitbit.ui.theme.background
import net.runner.fitbit.ui.theme.lightText

@Composable
fun DropdownMenuGoals(
    selectedOptions: Set<String>,
    options: List<String>,
    org:Boolean,
    onOptionSelected: (Set<String>) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        TextButton(
            onClick = { expanded = true},
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.textButtonColors(
                contentColor = lightText
            ),
            border = BorderStroke(1.dp, lightText),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(if (org){"Facilities : "}else{"Goal Type : "}, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(selectedOptions.joinToString(", "), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Icon(imageVector = Icons.Filled.KeyboardArrowDown, contentDescription = "")
            }
        }


        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(background.copy(0.7f))
        ) {

            options.forEach { option ->
                val isSelected = option in selectedOptions
                DropdownMenuItem(text = { Text(option) }, onClick = {
                    val newSelection = if (isSelected) {
                        selectedOptions - option
                    } else {
                        selectedOptions + option
                    }
                    onOptionSelected(newSelection)
                },

                )
            }
        }
    }
}
@Composable
fun DropdownMenuGoalsTime(
    selectedOption: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        TextButton(
            onClick = { expanded = true},
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.textButtonColors(
                contentColor = lightText
            ),
            border = BorderStroke(1.dp, lightText),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text("Time Frame : ", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(selectedOption, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Icon(imageVector = Icons.Filled.KeyboardArrowDown, contentDescription = "")
            }
        }


        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(background.copy(0.7f))
        ) {
            options.forEach { option ->
                DropdownMenuItem(text = { Text(option) }, onClick = {
                    onOptionSelected(option)
                    expanded = false
                },

                    )
            }
        }
    }
}