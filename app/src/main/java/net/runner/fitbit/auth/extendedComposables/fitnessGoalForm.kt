package net.runner.fitbit.auth.extendedComposables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import net.runner.fitbit.R
import net.runner.fitbit.auth.database.DatabaseEntryBuddy
import net.runner.fitbit.ui.theme.background
import net.runner.fitbit.ui.theme.lightBlueText
import net.runner.fitbit.ui.theme.lightText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitnessGoalsForm(
    username:String,
    email:String,
    gender:String,
    navController: NavController
) {
    var goalType by remember { mutableStateOf(setOf<String>()) }
    var targetValue by remember { mutableStateOf("5") }
    var timeFrame by remember { mutableStateOf("1 month") }
    var selectedStartTime by rememberSaveable { mutableStateOf("") }
    var selectedEndTime by rememberSaveable { mutableStateOf("") }
    var fromTimePickerState by rememberSaveable { mutableStateOf(false) }
    var toTimePickerState by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text("Set Your Fitness Goals", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(0.8f))
        Spacer(modifier = Modifier.height(15.dp))

        DropdownMenuGoals(
            selectedOptions = goalType,
            options = listOf("Weight Loss", "Muscle Gain", "Endurance","General Fitness","Mental Fitness"),
            onOptionSelected = { goalType = it }
        )
        Spacer(modifier = Modifier.height(3.dp))

        DropdownMenuGoalsTime(
            selectedOption = timeFrame,
            options = listOf("1 month", "2 months", "3 months", "6 months"),
            onOptionSelected = { timeFrame = it }
        )

        Spacer(modifier = Modifier.height(10.dp))
        Text("Target", fontSize = 16.sp, color = lightText, fontWeight = FontWeight.Bold)
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
        Spacer(modifier = Modifier.height(15.dp))
        Text("Availability", fontSize = 16.sp, color = lightText, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(5.dp))

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = lightText.copy(alpha = 0.1f)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                // From Row
                Row(
                    modifier = Modifier
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("From :", fontSize = 16.sp, color = lightText, fontWeight = FontWeight.Bold)
                    Button(
                        onClick = { fromTimePickerState = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.Black,
                            containerColor = Color.Transparent
                        ),
                        contentPadding = PaddingValues(10.dp),
                        modifier = Modifier.padding(horizontal = 10.dp),
                        border = BorderStroke(1.dp, lightText)
                    ) {
                        Text(selectedStartTime.ifEmpty { "Select" }, color = lightText, fontWeight = FontWeight.Bold)
                    }
                    if (fromTimePickerState) {
                        TimePickerButton("from", onConfirm = { time, state ->
                            if (state == "from") {
                                val calendar = Calendar.getInstance()
                                calendar.set(Calendar.HOUR_OF_DAY, time.hour)
                                calendar.set(Calendar.MINUTE, time.minute)
                                calendar.set(Calendar.SECOND, 0)

                                val formattedTime = SimpleDateFormat("h:mm a", Locale.getDefault()).format(calendar.time)
                                selectedStartTime = formattedTime
                                fromTimePickerState = false
                            }
                        }) {
                            fromTimePickerState = false
                        }
                    }
                }

                // To Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(14.dp),
                ) {
                    Text("To :", fontSize = 16.sp, color = lightText, fontWeight = FontWeight.Bold)
                    Button(
                        onClick = { toTimePickerState = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.Black,
                            containerColor = Color.Transparent
                        ),
                        contentPadding = PaddingValues(10.dp),
                        modifier = Modifier.padding(horizontal = 10.dp),
                        border = BorderStroke(1.dp, lightText)
                    ) {
                        Text(selectedEndTime.ifEmpty { "Select" }, color = lightText, fontWeight = FontWeight.Bold)
                    }
                    if (toTimePickerState) {
                        TimePickerButton("to", onConfirm = { time, state ->
                            if (state == "to") {
                                val calendar = Calendar.getInstance()
                                calendar.set(Calendar.HOUR_OF_DAY, time.hour)
                                calendar.set(Calendar.MINUTE, time.minute)
                                calendar.set(Calendar.SECOND, 0)

                                val formattedTime = SimpleDateFormat("h:mm a", Locale.getDefault()).format(calendar.time)
                                selectedEndTime = formattedTime
                                toTimePickerState = false
                            }
                        }) {
                            toTimePickerState = false
                        }
                    }
                }

            }
        }

        Spacer(modifier = Modifier.height(30.dp))
        if(selectedStartTime.isNotEmpty()&&selectedEndTime.isNotEmpty()&&goalType.isNotEmpty()){
            val context = LocalContext.current
            Button(
                onClick = {
                    DatabaseEntryBuddy(goalType, selectedStartTime, selectedEndTime,username,email,gender,timeFrame,targetValue, context){
                        if(it=="success"){
                            navController.navigate("dashBoardBuddy"){
                                popUpTo("userDetails") { inclusive = true }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 10.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = lightBlueText,
                    contentColor = Color.Black
                )
            ) {
                Text("Save Fitness Goal", color = background, fontWeight = FontWeight.Bold)
            }
        }
    }
}


