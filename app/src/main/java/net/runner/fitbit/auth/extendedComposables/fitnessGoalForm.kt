package net.runner.fitbit.auth.extendedComposables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import net.runner.fitbit.R
import net.runner.fitbit.ui.theme.lightBlueText
import net.runner.fitbit.ui.theme.lightText

@Composable
fun FitnessGoalsForm() {
    var goalType by remember { mutableStateOf("Select") }
    var targetValue by remember { mutableStateOf("5") }
    var timeFrame by remember { mutableStateOf("1 month") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Set Your Fitness Goals", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(0.8f))
        Spacer(modifier = Modifier.height(10.dp))
        DropdownMenuGoals(
            selectedOption = goalType,
            options = listOf("Weight Loss", "Muscle Gain", "Endurance", "General Fitness"),
            onOptionSelected = { goalType = it }
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text("Target", fontSize = 16.sp, color = lightText)
        Spacer(modifier = Modifier.height(5.dp))
        TextField(
            value = targetValue,
            onValueChange = {targetValue = it },
            label = { Text("Enter your target (e.g., kg to lose, reps to perform)", color = lightBlueText.copy(0.4f)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = lightText.copy(alpha = 0.1f),
                unfocusedContainerColor = lightText.copy(alpha = 0.1f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedTextColor = lightText.copy(alpha = 0.7f),
                unfocusedTextColor = lightText.copy(0.7f)
            ),
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.dumbell), contentDescription = "", modifier = Modifier.size(24.dp), tint = lightBlueText)
            }
        )

        Spacer(modifier = Modifier.height(20.dp))
        Text("Time Frame", fontSize = 16.sp, color = lightText)
        DropdownMenuGoals(
            selectedOption = timeFrame,
            options = listOf("1 month", "2 months", "3 months", "6 months"),
            onOptionSelected = { timeFrame = it }
        )

        Spacer(modifier = Modifier.height(30.dp))
        Button(
            onClick = { /* Save goal */ },
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 10.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = lightBlueText,
                contentColor = Color.Black
            )
        ) {
            Text("Save Fitness Goal")
        }
    }
}

