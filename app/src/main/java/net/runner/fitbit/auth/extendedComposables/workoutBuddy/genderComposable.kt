package net.runner.fitbit.auth.extendedComposables.workoutBuddy

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.runner.fitbit.R
import net.runner.fitbit.ui.theme.background
import net.runner.fitbit.ui.theme.lightBlueText
import net.runner.fitbit.ui.theme.lightText

@Composable
fun Gender(onresult:(String)-> Unit) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var selectedGender by rememberSaveable { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDialog = true },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = lightText.copy(alpha = 0.1f))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.gender),
                    contentDescription = null,
                    tint = lightBlueText,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = if (selectedGender.isEmpty()) "Gender" else selectedGender,
                    fontSize = 17.sp,
                    color = lightBlueText.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = lightBlueText.copy(alpha = 0.7f)
                )
            }
        }

    }
        if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Select Gender") },
            containerColor = background,
            textContentColor = lightBlueText,
            text = {
                Column {
                    GenderRadioButton("Male",selectedGender) {
                        selectedGender ="Male"
                    }
                    GenderRadioButton("Female",selectedGender) {
                        selectedGender = "Female"
                    }
                    GenderRadioButton("Other",selectedGender) {
                        selectedGender= "Other"
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    onresult(selectedGender)
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }, colors = ButtonDefaults.textButtonColors(contentColor = lightText)) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun GenderRadioButton(
    gender: String,
    selectedGender: String,
    onSelected: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelected),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = (gender == selectedGender),
            onClick = onSelected,
            colors = RadioButtonDefaults.colors(
                selectedColor = lightBlueText
            )
        )
        Text(text = gender, modifier = Modifier.padding(start = 8.dp))
    }
}