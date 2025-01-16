package net.runner.fitbit.Profiles

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import net.runner.fitbit.Database.getUserData
import net.runner.fitbit.R
import net.runner.fitbit.auth.extendedComposables.workoutBuddy.DropdownMenuGoals
import net.runner.fitbit.auth.extendedComposables.workoutBuddy.DropdownMenuGoalsTime
import net.runner.fitbit.auth.extendedComposables.workoutBuddy.TimePickerButton
import net.runner.fitbit.ui.theme.background
import net.runner.fitbit.ui.theme.lightBlueText
import net.runner.fitbit.ui.theme.lightText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileBuddy(navController: NavController){
    var imageUri by rememberSaveable {
        mutableStateOf(Uri.EMPTY)
    }
    var goalType by rememberSaveable { mutableStateOf(setOf<String>()) }
    var timeFrame by remember { mutableStateOf("1 month") }
    var targetValue by rememberSaveable { mutableStateOf("") }
    var selectedStartTime by rememberSaveable { mutableStateOf("") }
    var selectedEndTime by rememberSaveable { mutableStateOf("") }
    var fromTimePickerState by rememberSaveable { mutableStateOf(false) }
    var toTimePickerState by rememberSaveable { mutableStateOf(false) }
    var connections by rememberSaveable {
        mutableStateOf(
            mutableMapOf(
            "instagram" to "",
            "facebook" to "",
            "x" to "",
            "linkedin" to "",
            "reddit" to "",
        ))
    }



    val pickImage = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            imageUri = uri
        }
    }


    var userData by rememberSaveable {
        mutableStateOf(mutableMapOf<String, Any>())
    }
    var name by rememberSaveable {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {
        getUserData{
            userData = it
            imageUri = it["profileImageUrl"].toString().toUri()
            name = it["username"].toString()
            goalType = (it["goalType"] as? List<*>)?.filterIsInstance<String>()?.toSet() ?: setOf()
            timeFrame = it["timeFrame"].toString()
            targetValue = it["targetValue"].toString()
            selectedEndTime = it["selectedEndTime"].toString()
            selectedStartTime = it["selectedStartTime"].toString()
            //todo connections = it["connections"]
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .statusBarsPadding(),

    ) {
        item{
            Box (
                modifier = Modifier.fillMaxWidth(),
            ){

                Button(
                    onClick = {
                    },
                    contentPadding = PaddingValues(10.dp),
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 3.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = lightText.copy(alpha = 0.1f),
                    ),
                    border = BorderStroke(
                        0.5.dp,
                        Color.White
                    )
                ) {
                    Text("Cancel", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                    },
                    contentPadding = PaddingValues(10.dp),
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                        .align(Alignment.CenterEnd),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                    )
                ) {
                    Text("Submit", color = background, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        item {
            HorizontalDivider(Modifier.padding(top = 5.dp))
        }
        item {
            Text(text = "Edit Profile", modifier = Modifier
                .padding(10.dp)
                .padding(top = 10.dp), color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 20.dp)
                    .height(100.dp)
                    .clickable {
                        pickImage.launch("image/*")
                    }
                ,
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = lightText.copy(alpha = 0.1f)
                )
            ) {
                if (imageUri != Uri.EMPTY) {
                    Image(
                        painter = rememberAsyncImagePainter(model = imageUri),
                        contentDescription = "Selected User Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .border(1.dp, Color.LightGray, RoundedCornerShape(20.dp))
                            .clip(RoundedCornerShape(20.dp))
                    )
                } else {
                    Text(text = "Upload Image", modifier = Modifier
                        .padding(30.dp)
                        .align(Alignment.CenterHorizontally), color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(30.dp))
            Text(text = "Username", color = lightText, fontSize = 15.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 25.dp))
            TextField(
                value = name,
                onValueChange = { text -> name = text },
                placeholder = { Text(text = "Name", color = lightBlueText, fontSize = 17.sp) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = lightText.copy(alpha = 0.1f),
                    unfocusedContainerColor = lightText.copy(alpha = 0.1f),
                    focusedIndicatorColor = Color.Red.copy(alpha = 0.5f),
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.username),
                        contentDescription = "",
                        modifier = Modifier.size(24.dp),
                        tint = lightBlueText
                    )
                }
            )
        }
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ){
                Text("Set Your Fitness Goals", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(0.8f))
                Spacer(modifier = Modifier.height(15.dp))
                DropdownMenuGoals(
                    selectedOptions = goalType,
                    options = listOf("Weight Loss", "Muscle Gain", "Endurance","General Fitness","Mental Fitness"),
                    org = false,
                    onOptionSelected = { goalType = it }
                )
            }

        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {

                DropdownMenuGoalsTime(
                    selectedOption = timeFrame,
                    options = listOf("1 month", "2 months", "3 months", "6 months"),
                    onOptionSelected = { timeFrame = it }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
        item {
            Text("Target", fontSize = 15.sp, color = lightText, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 25.dp))
            TextField(
                value = targetValue,
                onValueChange = {targetValue = it },
                label = { Text("Enter your target (e.g., kg to lose, reps to perform)", color = lightBlueText.copy(0.4f)) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
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
                placeholder = {
                    Text("e.g. Lose 5 Kg", color = lightBlueText.copy(0.4f))
                },
                leadingIcon = {
                    Icon(painter = painterResource(id = R.drawable.dumbell), contentDescription = "", modifier = Modifier.size(24.dp), tint = lightBlueText)
                }
            )
        }
        item {
            Spacer(modifier = Modifier.height(5.dp))
            Text("Availability", fontSize = 15.sp, color = lightText, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 25.dp))

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = lightText.copy(alpha = 0.1f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
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
        }
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Profile Social Links", color = Color.LightGray, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 25.dp))
            Spacer(modifier = Modifier.height(10.dp))

        }
        item {
            TextField(
                value = connections["instagram"].toString(),
                onValueChange = { text ->
                    connections = connections.toMutableMap().apply {
                        this["instagram"] = text
                    }
                },
                singleLine = true,
                label = {
                    Text(text = "Instagram ID", color = lightBlueText, fontSize = 13.sp)
                        },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = lightText.copy(alpha = 0.1f),
                    unfocusedContainerColor = lightText.copy(alpha = 0.1f),
                    focusedIndicatorColor = Color.Red.copy(alpha = 0.5f),
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.instagram),
                        contentDescription = "",
                        modifier = Modifier.size(24.dp),
                        tint = lightBlueText
                    )
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            TextField(
                value = connections["facebook"].toString(),
                onValueChange = { text ->
                    connections = connections.toMutableMap().apply {
                        this["facebook"] = text
                    }
                },
                singleLine = true,
                label = {
                    Text(text = "Facebook Handle", color = lightBlueText, fontSize = 13.sp)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = lightText.copy(alpha = 0.1f),
                    unfocusedContainerColor = lightText.copy(alpha = 0.1f),
                    focusedIndicatorColor = Color.Red.copy(alpha = 0.5f),
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.facebook_icon),
                        contentDescription = "",
                        modifier = Modifier.size(24.dp),
                        tint = lightBlueText
                    )
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            TextField(
                value = connections["x"].toString(),
                onValueChange = { text ->
                    connections = connections.toMutableMap().apply {
                        this["x"] = text
                    }
                },
                singleLine = true,
                label = {
                    Text(text = "X ", color = lightBlueText, fontSize = 13.sp)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = lightText.copy(alpha = 0.1f),
                    unfocusedContainerColor = lightText.copy(alpha = 0.1f),
                    focusedIndicatorColor = Color.Red.copy(alpha = 0.5f),
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.x),
                        contentDescription = "",
                        modifier = Modifier.size(20.dp),
                        tint = lightBlueText
                    )
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            TextField(
                value = connections["reddit"].toString(),
                onValueChange = { text ->
                    connections = connections.toMutableMap().apply {
                        this["reddit"] = text
                    }
                },
                singleLine = true,
                label = {
                    Text(text = "Reddit", color = lightBlueText, fontSize = 13.sp)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = lightText.copy(alpha = 0.1f),
                    unfocusedContainerColor = lightText.copy(alpha = 0.1f),
                    focusedIndicatorColor = Color.Red.copy(alpha = 0.5f),
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.reddit_icon),
                        contentDescription = "",
                        modifier = Modifier.size(28.dp),
                        tint = lightBlueText
                    )
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            TextField(
                value = connections["linkedin"].toString(),
                onValueChange = { text ->
                    connections = connections.toMutableMap().apply {
                        this["linkedin"] = text
                        println(connections)
                    }
                },
                singleLine = true,
                label = {
                    Text(text = "LinkedIn", color = lightBlueText, fontSize = 13.sp)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = lightText.copy(alpha = 0.1f),
                    unfocusedContainerColor = lightText.copy(alpha = 0.1f),
                    focusedIndicatorColor = Color.Red.copy(alpha = 0.5f),
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.linkedin_icon),
                        contentDescription = "",
                        modifier = Modifier.size(24.dp),
                        tint = lightBlueText
                    )
                }
            )
        }
        item {
            Spacer(modifier = Modifier.height(10.dp))
        }

    }

}