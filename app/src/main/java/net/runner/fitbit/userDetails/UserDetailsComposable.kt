package net.runner.fitbit.userDetails

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import net.runner.fitbit.R
import net.runner.fitbit.auth.extendedComposables.organizer.OrganizerGoals
import net.runner.fitbit.auth.extendedComposables.workoutBuddy.FitnessGoalsForm
import net.runner.fitbit.auth.extendedComposables.workoutBuddy.Gender
import net.runner.fitbit.ui.theme.background
import net.runner.fitbit.ui.theme.lightBlueText
import net.runner.fitbit.ui.theme.lightText

@Composable
fun UserDetailComposable(navController: NavController) {
    var imageUri by rememberSaveable { mutableStateOf<Uri>(Uri.EMPTY) }
    var username by rememberSaveable { mutableStateOf("") }
    val email = FirebaseAuth.getInstance().currentUser?.email
    var gender by rememberSaveable { mutableStateOf("") }

    val pickImage = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            imageUri = uri
        }
    }

    var WorkoutBuddy by rememberSaveable { mutableStateOf(false) }
    var Organizer by rememberSaveable { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .statusBarsPadding(),
        contentPadding = PaddingValues(horizontal = 30.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Details",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp
                )
            }

        }
        item {
            Spacer(modifier = Modifier.height(10.dp))
        }
        item {
            if (imageUri != Uri.EMPTY) {
                Image(
                    painter = rememberImagePainter(data = imageUri),
                    contentDescription = "Selected User Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .border(2.dp, Color.Gray, CircleShape)
                        .clip(CircleShape)
                        .clickable { pickImage.launch("image/*") }
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.user),
                    contentDescription = "Default User Image",
                    modifier = Modifier
                        .size(150.dp)
                        .clickable { pickImage.launch("image/*") },
                    contentScale = ContentScale.Crop
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(15.dp))
        }
        item {
            TextField(
                value = username,
                onValueChange = { text -> username = text },
                placeholder = { Text(text = "Name", color = lightBlueText, fontSize = 17.sp) },
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
                        painter = painterResource(id = R.drawable.username),
                        contentDescription = "",
                        modifier = Modifier.size(24.dp),
                        tint = lightBlueText
                    )
                }
            )
        }
        item {
            TextField(
                value = email!!,
                onValueChange = {},
                readOnly = true,
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
                    Icon(
                        painter = painterResource(id = R.drawable.mail),
                        contentDescription = "",
                        modifier = Modifier.size(24.dp),
                        tint = lightBlueText
                    )
                }
            )
        }
        item {
            Gender{
                gender=it
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Workout Buddy", color = lightText, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    Checkbox(
                        checked = WorkoutBuddy,
                        onCheckedChange = {
                            if (Organizer && !WorkoutBuddy) {
                                Organizer = !Organizer
                            }
                            WorkoutBuddy = !WorkoutBuddy
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = lightBlueText,
                            uncheckedColor = lightText
                        )
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Organizer", color = lightText, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    Checkbox(
                        checked = Organizer,
                        onCheckedChange = {
                            if (WorkoutBuddy && !Organizer) {
                                WorkoutBuddy = !WorkoutBuddy
                            }
                            Organizer = !Organizer
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = lightBlueText,
                            uncheckedColor = lightText
                        )
                    )
                }
            }
        }
        if (WorkoutBuddy) {
            item {
                FitnessGoalsForm(username ,
                    imageUri,gender,navController)
            }
        }
        if (Organizer) {
            item { OrganizerGoals(username ,imageUri,gender,navController) }
        }
    }
}
