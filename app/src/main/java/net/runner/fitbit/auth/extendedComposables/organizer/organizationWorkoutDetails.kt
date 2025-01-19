package net.runner.fitbit.auth.extendedComposables.organizer

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import net.runner.fitbit.auth.database.DataBaseEntryOrganizer
import net.runner.fitbit.auth.database.DatabaseEntryBuddy
import net.runner.fitbit.auth.extendedComposables.workoutBuddy.DropdownMenuGoals
import net.runner.fitbit.auth.extendedComposables.workoutBuddy.TimePickerButton
import net.runner.fitbit.ui.theme.background
import net.runner.fitbit.ui.theme.lightBlueText
import net.runner.fitbit.ui.theme.lightText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun organizationWorkoutDetails(groupData: OrganizerGroupData,imageUri: Uri,navController: NavController) {
    var facilities by rememberSaveable { mutableStateOf(setOf<String>()) }
    var orgStartTime by rememberSaveable { mutableStateOf("") }
    var orgEndTime by rememberSaveable { mutableStateOf("") }
    var orgFromTimePickerState by rememberSaveable { mutableStateOf(false) }
    var orgToTimePickerState by rememberSaveable { mutableStateOf(false) }
    var orgPrivacy by rememberSaveable { mutableStateOf(false) }
    var orgBanner by rememberSaveable {mutableStateOf(Uri.EMPTY)}

    val pickImage = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            orgBanner = uri
        }
    }


    HorizontalDivider(color = lightBlueText.copy(0.4f))
    Spacer(modifier = Modifier.height(10.dp))

    Text("About Organization", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = lightText)

    Spacer(modifier = Modifier.height(10.dp))
    Box(modifier = Modifier.fillMaxWidth()){
        Text("Org Banner :", fontSize = 16.sp, color = lightText, fontWeight = FontWeight.Bold, modifier = Modifier
            .align(Alignment.CenterStart)
            .padding(start = 10.dp))
    }
    Spacer(modifier = Modifier.height(10.dp))
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(lightText.copy(0.1f))
            .clickable {
                pickImage.launch("image/*")
            }
    ){
        if(orgBanner != Uri.EMPTY){

            AsyncImage(
                model = orgBanner,
                contentDescription = "banner",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(lightText.copy(0.1f))
                ,
                contentScale = ContentScale.Crop
            )
        }else{
            Text(text = "Upload Banner", color = lightText, fontSize = 20.sp, modifier = Modifier.align(Alignment.Center), fontWeight = FontWeight.Bold)
        }

    }

    Spacer(modifier = Modifier.height(10.dp))

    Box(modifier = Modifier.fillMaxWidth()){
        Text("Timings :", fontSize = 16.sp, color = lightText, fontWeight = FontWeight.Bold, modifier = Modifier
            .align(Alignment.CenterStart)
            .padding(start = 10.dp))
    }

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
                    onClick = { orgFromTimePickerState = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        containerColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues(10.dp),
                    modifier = Modifier.padding(horizontal = 10.dp),
                    border = BorderStroke(1.dp, lightText)
                ) {
                    Text(orgStartTime.ifEmpty { "Select" }, color = lightText, fontWeight = FontWeight.Bold)
                }
                if (orgFromTimePickerState) {
                    TimePickerButton("from", onConfirm = { time, state ->
                        if (state == "from") {
                            val calendar = Calendar.getInstance()
                            calendar.set(Calendar.HOUR_OF_DAY, time.hour)
                            calendar.set(Calendar.MINUTE, time.minute)
                            calendar.set(Calendar.SECOND, 0)

                            val formattedTime = SimpleDateFormat("h:mm a", Locale.getDefault()).format(calendar.time)
                            orgStartTime = formattedTime
                            orgFromTimePickerState = false
                        }
                    }) {
                        orgFromTimePickerState = false
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
                    onClick = { orgToTimePickerState = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        containerColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues(10.dp),
                    modifier = Modifier.padding(horizontal = 10.dp),
                    border = BorderStroke(1.dp, lightText)
                ) {
                    Text(orgEndTime.ifEmpty { "Select" }, color = lightText, fontWeight = FontWeight.Bold)
                }
                if (orgToTimePickerState) {
                    TimePickerButton("to", onConfirm = { time, state ->
                        if (state == "to") {
                            val calendar = Calendar.getInstance()
                            calendar.set(Calendar.HOUR_OF_DAY, time.hour)
                            calendar.set(Calendar.MINUTE, time.minute)
                            calendar.set(Calendar.SECOND, 0)

                            val formattedTime = SimpleDateFormat("h:mm a", Locale.getDefault()).format(calendar.time)
                            orgEndTime = formattedTime
                            orgToTimePickerState = false
                        }
                    }) {
                        orgToTimePickerState = false
                    }
                }
            }

        }
    }
    Spacer(modifier = Modifier.height(10.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Private", color = lightText, fontSize = 17.sp, fontWeight = FontWeight.Bold)
            Checkbox(
                checked = orgPrivacy,
                onCheckedChange = {
                    orgPrivacy = !orgPrivacy
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = lightBlueText,
                    uncheckedColor = lightText
                )
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Public", color = lightText, fontSize = 17.sp, fontWeight = FontWeight.Bold)
            Checkbox(
                checked = !orgPrivacy,
                onCheckedChange = {
                    orgPrivacy = !orgPrivacy
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = lightBlueText,
                    uncheckedColor = lightText
                )
            )
        }
    }


    DropdownMenuGoals(
        selectedOptions = facilities,
        options = listOf("Weight Loss", "Muscle Gain", "Endurance","General Fitness","Mental Fitness"),
        org = true,
        onOptionSelected = { facilities = it }
    )
    Spacer(modifier = Modifier.height(30.dp))
    if(groupData.organizationName.isNotEmpty()&&groupData.organizationAddress.isNotEmpty()&&groupData.organizationCity.isNotEmpty()&&facilities.isNotEmpty()){
        val context = LocalContext.current
        Button(
            onClick = {
                DataBaseEntryOrganizer(groupData, orgBanner,facilities, orgStartTime, orgEndTime,orgPrivacy,imageUri,context){
                    if(it=="success"){
                        navController.navigate("dashBoardOrganizer"){
                            popUpTo("userDetails") { inclusive = true }
                        }
                    }
                }
            },
            modifier = Modifier
                .padding(vertical = 10.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = lightBlueText,
                contentColor = Color.Black
            )
        ) {
            Text("Save Organization", color = background, fontWeight = FontWeight.Bold)
        }
    }

}