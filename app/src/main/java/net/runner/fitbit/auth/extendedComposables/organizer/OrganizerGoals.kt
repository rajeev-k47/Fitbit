package net.runner.fitbit.auth.extendedComposables.organizer

import android.util.Patterns
import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import kotlinx.coroutines.launch
import net.runner.fitbit.R
import net.runner.fitbit.auth.authFunctions.EmailLinkAuth
import net.runner.fitbit.auth.database.DatabaseEntryBuddy
import net.runner.fitbit.ui.theme.background
import net.runner.fitbit.ui.theme.lightBlueText
import net.runner.fitbit.ui.theme.lightText

@Composable
fun OrganizerGoals(
    username: String,
    email: String,
    gender: String,
    navController: NavController
){
    var createGroupStatus by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if(!createGroupStatus){
            Button(
                onClick = {
                    createGroupStatus = true
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = lightBlueText,
                    contentColor = Color.Black
                )
            ) {
                Text("Create New Group", color = background, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            HorizontalDivider(color = lightBlueText, thickness = 1.dp, modifier = Modifier.padding(vertical = 10.dp))
            Box(modifier = Modifier.fillMaxWidth()){
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ){
                    Text(text = "Or Continue without Group", color = lightBlueText, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = {
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = lightText.copy(alpha = 0.2f),
                        ),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier
                            .size(35.dp)
                    ) {
                        Icon(painter = painterResource(id = R.drawable.arrow_right), contentDescription = "signUp", modifier = Modifier.size(22.dp), tint = lightBlueText)
                    }
                }
            }
        }else{
            OrganizerCreateGroup(username,email,gender,navController)
        }


    }
}
