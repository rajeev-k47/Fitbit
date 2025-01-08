package net.runner.fitbit.auth.extendedComposables.organizer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import net.runner.fitbit.R
import net.runner.fitbit.ui.theme.lightBlueText
import net.runner.fitbit.ui.theme.lightText

data class OrganizerGroupData(
    var organizationName: String ="",
    var organizerMobile: String ="",
    var organizationAddress: String ="",
    var organizationCity: String="",
    var organizationState: String="",
    var organizationPostalCode: String="",
    var organizerName : String="",
    var organizerEmail : String="",
    var organizerGender : String="",
)

@Composable
fun OrganizerCreateGroup(
    username: String,
    email: String,
    gender: String,
    navController: NavController
) {
    var groupData by rememberSaveable(stateSaver = OrganizerGroupDataSaver) {
        mutableStateOf(OrganizerGroupData(
            organizationName = "",
            organizerMobile = "",
            organizationAddress = "",
            organizationCity = "",
            organizationState = "",
            organizationPostalCode = "",
            organizerName = username,
            organizerEmail = email,
            organizerGender = gender
        ))
    }
    var showStateDialog by rememberSaveable {
        mutableStateOf(false)
    }

    Text(text ="Create Group", color = lightBlueText, fontSize = 22.sp, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(10.dp))

    TextField(
        value = groupData.organizationName,
        onValueChange = { text -> groupData = groupData.copy(organizationName = text) },
        placeholder = { Text(text = "Organization Name", color = lightBlueText, fontSize = 17.sp) },
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
                painter = painterResource(id = R.drawable.organizer),
                contentDescription = "",
                modifier = Modifier.size(23.dp),
                tint = lightBlueText
            )
        }
    )
    Spacer(modifier = Modifier.height(10.dp))
    TextField(
        value = groupData.organizerMobile,
        onValueChange = { text -> groupData = groupData.copy(organizerMobile = text)},
        placeholder = { Text(text = "Phone No.", color = lightBlueText, fontSize = 17.sp) },
        singleLine = true,
        prefix = { if(groupData.organizerMobile.isNotEmpty())Text(text = "+91", color = lightBlueText, fontSize = 17.sp)},
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
        value = groupData.organizationAddress,
        onValueChange = { text -> groupData = groupData.copy(organizationAddress = text)},
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
        value = groupData.organizationCity,
        onValueChange = { text -> groupData = groupData.copy(organizationCity = text)},
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
            value = groupData.organizationState,
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
            value = groupData.organizationPostalCode,
            onValueChange = { text -> groupData = groupData.copy(organizationPostalCode = text)},
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
    if(showStateDialog){
        ShowStatesIndia(showDialog = showStateDialog, onDismiss = {showStateDialog=false}) {
            groupData.organizationState = it
        }
    }
    Spacer(modifier = Modifier.height(10.dp))

    organizationWorkoutDetails(groupData,navController)


}