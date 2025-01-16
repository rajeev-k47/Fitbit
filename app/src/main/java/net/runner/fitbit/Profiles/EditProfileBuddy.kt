package net.runner.fitbit.Profiles

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.AsyncImage
import net.runner.fitbit.Database.getUserData
import net.runner.fitbit.ui.theme.background
import net.runner.fitbit.ui.theme.lightText

@Composable
fun EditProfileBuddy(navController: NavController){

    var userData by rememberSaveable {
        mutableStateOf(mutableMapOf<String, Any>())
    }

    LaunchedEffect(Unit) {
        getUserData{
            userData = it
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
            Text(text = "Edit Profile", modifier = Modifier.padding(10.dp).padding(top = 10.dp), color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp).padding(top = 20.dp)
                    .height(100.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = lightText.copy(alpha = 0.1f)
                )
            ) {
                AsyncImage(
                    model = userData["profileImageUrl"].toString().toUri() ,
//                            ?: auth.currentUser?.photoUrl,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(100.dp)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(20.dp))
                        .border(
                            2.dp,
                            Color.Gray.copy(0.4f),
                            RoundedCornerShape(20.dp)
                        ),
                )
            }
        }
        item {  }

    }

}