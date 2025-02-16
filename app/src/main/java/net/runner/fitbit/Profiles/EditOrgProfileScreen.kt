package net.runner.fitbit.Profiles

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import net.runner.fitbit.Database.getData
import net.runner.fitbit.ImageCaching
import net.runner.fitbit.R
import net.runner.fitbit.auth.extendedComposables.organizer.OrganizerGroupData
import net.runner.fitbit.auth.extendedComposables.organizer.OrganizerGroupDataSaver
import net.runner.fitbit.auth.extendedComposables.organizer.ShowStatesIndia
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
fun EditOrgProfileScreen(navController: NavController) {

    var bannerImageUri by rememberSaveable {
        mutableStateOf(Uri.EMPTY)
    }
    var showStateDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var facilities by rememberSaveable { mutableStateOf(setOf<String>()) }
    var selectedStartTime by rememberSaveable { mutableStateOf("") }
    var selectedEndTime by rememberSaveable { mutableStateOf("") }
    var fromTimePickerState by rememberSaveable { mutableStateOf(false) }
    var toTimePickerState by rememberSaveable { mutableStateOf(false) }
    var orgPrivacy by rememberSaveable { mutableStateOf(false) }

    var newGroupData by rememberSaveable(stateSaver = OrganizerGroupDataSaver) {
        mutableStateOf(
            OrganizerGroupData(
            organizationName = "",
            organizerMobile = "",
            organizationAddress = "",
            organizationCity = "",
            organizationState = "",
            organizationPostalCode = "",
            organizerName = "",
            organizerGender = "",
            organizerImage = ""
        )
        )
    }
    var profileImageUri by rememberSaveable {
        mutableStateOf<Uri>(Uri.EMPTY)
    }




    val pickImage = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            profileImageUri = uri
        }
    }
    val pickBanner = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            bannerImageUri = uri
        }
    }

    var groupData by rememberSaveable { mutableStateOf(mapOf<String, Any>()) }


    LaunchedEffect(Unit) {
        getData(""){
            groupData = it
            newGroupData = OrganizerGroupData(
                organizationName = (it["groupData"] as Map<String, Any>)["organizationName"].toString(),
                organizerMobile = (it["groupData"] as Map<String, Any>)["organizerMobile"].toString(),
                organizationState = (it["groupData"] as Map<String, Any>)["organizationState"].toString(),
                organizationCity = (it["groupData"] as Map<String, Any>)["organizationCity"].toString(),
                organizationAddress = (it["groupData"] as Map<String, Any>)["organizationAddress"].toString(),
                organizationPostalCode = (it["groupData"] as Map<String, Any>)["organizationPostalCode"].toString(),
                organizerName = (it["groupData"] as Map<String, Any>)["organizerName"].toString(),
                organizerGender = (it["groupData"] as Map<String, Any>)["organizerGender"].toString(),
            )
            profileImageUri = it["profileImageUrl"].toString().toUri()
            facilities = (it["facilities"] as? List<*>)?.filterIsInstance<String>()?.toSet() ?: setOf()
            selectedEndTime = it["orgEndTime"].toString()
            selectedStartTime = it["orgStartTime"].toString()
            bannerImageUri = it["banner"].toString().toUri()
            orgPrivacy = it["private"] as Boolean
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
                        navController.navigate("ProfileOrganizer"){
                            popUpTo("editOrgProfileScreen") { inclusive =true }
                        }
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
                        editOrgProfileSave(profileImageUri,bannerImageUri,facilities,selectedStartTime,selectedEndTime,newGroupData,orgPrivacy)
                        navController.navigate("ProfileOrganizer"){
                            popUpTo("editOrgProfileScreen") { inclusive =true }
                        }
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
                if (profileImageUri != Uri.EMPTY) {
                    Image(
                        painter = rememberAsyncImagePainter(model =profileImageUri),
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
                value = newGroupData.organizerName,
                onValueChange = {newGroupData = newGroupData.copy(organizerName = it) },
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
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Banner", color = lightText, fontSize = 15.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 25.dp))

            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(140.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(lightText.copy(0.1f))
                    .clickable {
                        pickBanner.launch("image/*")
                    }
            ){
                if(bannerImageUri != Uri.EMPTY){

                    AsyncImage(
                        model = ImageCaching().CacheBuilder(LocalContext.current, bannerImageUri.toString()).build(),
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
        }
        item{
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Organization Name", color = lightText, fontSize = 15.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 25.dp))

            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
            ) {

                TextField(
                    value = newGroupData.organizationName,
                    onValueChange = { newGroupData = newGroupData.copy(organizationName = it) },
                    placeholder = { Text(text = "Organization Name", color = lightBlueText, fontSize = 17.sp) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth(),
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
                            painter = painterResource(id = R.drawable.organizer),
                            contentDescription = "",
                            modifier = Modifier.size(23.dp),
                            tint = lightBlueText
                        )
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = newGroupData.organizerMobile,
                    onValueChange = { newGroupData=newGroupData.copy(organizerMobile = it) },
                    placeholder = { Text(text = "Phone No.", color = lightBlueText, fontSize = 17.sp) },
                    singleLine = true,
                    prefix = { if(newGroupData.organizerMobile.isNotEmpty())Text(text = "+91", color = lightBlueText, fontSize = 17.sp)},
                    modifier = Modifier.fillMaxWidth(),
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
                            painter = painterResource(id = R.drawable.phone),
                            contentDescription = "",
                            modifier = Modifier.size(23.dp),
                            tint = lightBlueText
                        )
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = newGroupData.organizationAddress,
                    onValueChange = { newGroupData=newGroupData.copy(organizationAddress = it) },
                    placeholder = { Text(text = "Address", color = lightBlueText, fontSize = 17.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
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
                            painter = painterResource(id = R.drawable.location),
                            contentDescription = "",
                            modifier = Modifier.size(23.dp),
                            tint = lightBlueText
                        )
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = newGroupData.organizationCity,
                    onValueChange = { newGroupData=newGroupData.copy(organizationCity = it) },
                    placeholder = { Text(text = "City", color = lightBlueText, fontSize = 17.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
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
                            painter = painterResource(id = R.drawable.city),
                            contentDescription = "",
                            modifier = Modifier.size(23.dp),
                            tint = lightBlueText
                        )
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ){

                    TextField(
                        value = newGroupData.organizationState,
                        onValueChange = {},
                        placeholder = { Text(text = "State", color = lightBlueText, fontSize = 17.sp) },
                        singleLine = true,
                        readOnly = true,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 6.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = lightText.copy(alpha = 0.1f),
                            unfocusedContainerColor = lightText.copy(alpha = 0.1f),
                            focusedIndicatorColor = Color.Red.copy(alpha = 0.5f),
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                        ),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.location),
                                contentDescription = "",
                                modifier = Modifier.size(23.dp),
                                tint = lightBlueText
                            )
                        },
                        interactionSource = remember { MutableInteractionSource() }
                            .also { interactionSource ->
                                LaunchedEffect(interactionSource) {
                                    interactionSource.interactions.collect {
                                        if (it is PressInteraction.Release) {
                                            showStateDialog=!showStateDialog
                                        }
                                    }
                                }
                            },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = lightBlueText.copy(alpha = 0.7f)
                            )
                        }
                    )
                    TextField(
                        value = newGroupData.organizationPostalCode,
                        onValueChange = {newGroupData=newGroupData.copy(organizationPostalCode = it) },
                        placeholder = { Text(text = "Postal code", color = lightBlueText, fontSize = 17.sp) },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 6.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = lightText.copy(alpha = 0.1f),
                            unfocusedContainerColor = lightText.copy(alpha = 0.1f),
                            focusedIndicatorColor = Color.Red.copy(alpha = 0.5f),
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                        ),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.zipcode),
                                contentDescription = "",
                                modifier = Modifier.size(23.dp),
                                tint = lightBlueText
                            )
                        }
                    )

                }
            }


            if(showStateDialog){
                ShowStatesIndia(showDialog = showStateDialog, onDismiss = {showStateDialog=false}) {
                    newGroupData = newGroupData.copy(organizationState = it)
                }
            }

        }
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ){
                Text("Facilities :", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(0.8f))
                Spacer(modifier = Modifier.height(15.dp))
                DropdownMenuGoals(
                    selectedOptions = facilities,
                    options = listOf("Weight Loss", "Muscle Gain", "Endurance","General Fitness","Mental Fitness"),
                    org = true,
                    onOptionSelected = { facilities = it }
                )
            }

        }
        item {
            Spacer(modifier = Modifier.height(5.dp))
            Text("Timings", fontSize = 15.sp, color = lightText, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 25.dp))

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
        }
        item {
            Spacer(modifier = Modifier.height(10.dp))
        }

    }
}

