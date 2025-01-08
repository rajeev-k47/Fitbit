package net.runner.fitbit.userDetails

import android.util.Patterns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import net.runner.fitbit.R
import net.runner.fitbit.auth.authFunctions.EmailLinkAuth
import net.runner.fitbit.auth.extendedComposables.FitnessGoalsForm
import net.runner.fitbit.auth.extendedComposables.OrganizerGoals
import net.runner.fitbit.ui.theme.background
import net.runner.fitbit.ui.theme.lightBlueText
import net.runner.fitbit.ui.theme.lightText

@Composable
fun UserDetailComposable(navController: NavController) {
    var imageUri by rememberSaveable { mutableStateOf<String?>(null) }
    var username by rememberSaveable {
        mutableStateOf("")
    }
    val email = FirebaseAuth.getInstance().currentUser?.email

    val pickImage = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            imageUri = uri.toString()
        }
    }

    var WorkoutBuddy by remember { mutableStateOf(false) }
    var Organizer by remember { mutableStateOf(false) }


    Box(modifier = Modifier
        .fillMaxSize()
        .background(background)){
        Column (
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ){
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){

                Text(text = "Details", modifier = Modifier.padding(10.dp), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 26.sp)
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp)
                    .weight(0.7f),
                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
            ) {
                if (imageUri != null) {
                    Image(
                        painter = rememberImagePainter(data = imageUri),
                        contentDescription = "Selected User Image",
                        contentScale = ContentScale.Crop
                        ,
                        modifier = Modifier
                            .size(150.dp)
                            .padding(16.dp)
                            .border(2.dp, Color.Gray, CircleShape)
                            .clip(CircleShape)
                            .clickable { pickImage.launch("image/*") }
                        ,
                    )
                }
                else {
                    Image(
                        painter = painterResource(id = R.drawable.user),
                        contentDescription = "Default User Image",
                        modifier = Modifier
                            .size(150.dp)
                            .clickable { pickImage.launch("image/*") },
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.size(30.dp))

                TextField(
                    value = username,
                    onValueChange = {text-> username = text},
                    placeholder = {
                        Text(text = "Name", color = lightBlueText, fontSize = 17.sp)
                    },
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
                        Icon(painter = painterResource(id = R.drawable.username), contentDescription = "", modifier = Modifier.size(24.dp), tint = lightBlueText)
                    }
                )
                Spacer(modifier = Modifier.size(10.dp))

                TextField(
                    value = email!!,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = {
                        Text(text = "Email", color = lightBlueText, fontSize = 17.sp)
                    },
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
                            Icon(painter = painterResource(id = R.drawable.mail), contentDescription = "", modifier = Modifier.size(24.dp), tint = lightBlueText)
                    }
                )
                Spacer(modifier = Modifier.size(20.dp))
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ){
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(text = "Workout Buddy", color = lightText, fontSize = 17.sp)
                        Checkbox(checked = WorkoutBuddy, onCheckedChange = {
                            if(Organizer && !WorkoutBuddy){
                                Organizer=!Organizer
                            }
                            WorkoutBuddy= !WorkoutBuddy
                        },colors = CheckboxDefaults.colors(
                            checkedColor = lightBlueText,
                            uncheckedColor = lightText

                        ))
                    }
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(text = "Organizer", color = lightText, fontSize = 17.sp)
                        Checkbox(
                            checked = Organizer, onCheckedChange = {
                                if(WorkoutBuddy && !Organizer){
                                    WorkoutBuddy=!WorkoutBuddy
                                }
                                Organizer= !Organizer
                               },
                            colors = CheckboxDefaults.colors(
                                checkedColor = lightBlueText,
                                uncheckedColor = lightText

                            )
                        )
                    }

                }
                Spacer(modifier = Modifier.size(20.dp))

                if(WorkoutBuddy){
                    FitnessGoalsForm()
                }
                if(Organizer){
                    OrganizerGoals()
                }



            }

        }
    }
}