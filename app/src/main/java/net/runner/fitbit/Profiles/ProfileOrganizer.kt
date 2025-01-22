package net.runner.fitbit.Profiles

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.CachePolicy
import com.google.firebase.auth.FirebaseAuth
import net.runner.fitbit.Database.getData
import net.runner.fitbit.Firebase.fcmTokenSave
import net.runner.fitbit.R
import net.runner.fitbit.ui.theme.background
import net.runner.fitbit.ui.theme.lightBlueText
import net.runner.fitbit.ui.theme.lightText

@Composable
fun ProfileOrganizer(navController: NavController,orgId:String) {
    val auth = FirebaseAuth.getInstance()

    var OrganizerData by rememberSaveable {
        mutableStateOf(mutableMapOf<String, Any>())
    }

    LaunchedEffect(Unit) {
        getData(orgId.ifEmpty { "" }){
            OrganizerData = it
        }
    }


    Box(modifier = Modifier
        .fillMaxSize()
        .background(background)
        .statusBarsPadding()
    ){
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                )
                {
                    Spacer(modifier = Modifier.height(60.dp))

                    Text(text = "Profile", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 10.dp))

                    if(orgId.isEmpty()){

                        Button(
                            onClick = {
                                fcmTokenSave(false)
                                auth.signOut()
                                navController.navigate("signUpScreen"){
                                    popUpTo("ProfileOrganizer"){ inclusive = true }
                                }

                            },
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier
                                .size(34.dp)
                                .align(Alignment.CenterEnd),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = lightBlueText.copy(0.1f),
                            ),
                            border = BorderStroke(0.5.dp, lightText.copy(0.4f))
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.logout),
                                contentDescription = "Logout",
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(start = 2.dp)
                                , tint = lightBlueText
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))


                }
            }
            item {
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp))
            }
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(100.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = lightText.copy(alpha = 0.1f)
                    )
                ) {
                    AsyncImage(
                        model = OrganizerData["profileImageUrl"].toString().toUri() ,
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
            item{
                Spacer(modifier = Modifier.height(25.dp))
            }
            if(OrganizerData.isNotEmpty()){

                item {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    )

                    {

                        Text(
                            text = "${(OrganizerData["groupData"] as Map<*, *>?)?.get("organizerName").toString().toUpperCase()}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = "@${auth.currentUser?.email} • Joined ${
                                getFormatedTime(
                                    OrganizerData["joiningDate"].toString()
                                )
                            }",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = lightText.copy(1f),
                        )
                        Spacer(modifier = Modifier.height(0.dp))
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(
                                text = if((OrganizerData["users"] != null))"${(OrganizerData["users"] as List<String>).size} " else "0 " ,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                            )

                            Text(
                                text = "Buddies ",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = lightText.copy(1f),
                            )

                            if(orgId.isEmpty()){

                                Text(
                                    text = if((OrganizerData["pending"] != null))"• ${(OrganizerData["pending"] as List<String>).size} " else "• 0 " ,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                )
                                Text(
                                    text = "Pending",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = lightText.copy(1f),
                                )
                            }

                        }
                        Spacer(modifier = Modifier.height(15.dp))

                        Text(
                            text = "Facilities :",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = "${(OrganizerData["facilities"] as List<String>).joinToString(", ")}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = lightText,
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Banner",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        AsyncImage(
                            model = OrganizerData["banner"],
                            contentDescription = "avatar",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                                .clip(RoundedCornerShape(15.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Address :",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        val orgD = OrganizerData["groupData"] as Map<String, String>?
                        Text(
                            text = "${orgD?.get("organizationAddress").toString()}, ${orgD?.get("organizationCity").toString()}, ${orgD?.get("organizationState").toString()} (${orgD?.get("organizationPostalCode").toString()}) ",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = lightText,
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Contact :",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = "+91 ${orgD?.get("organizerMobile").toString()}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = lightText,
                        )
                        Spacer(modifier = Modifier.height(20.dp))



                        if(orgId.isEmpty()) {

                            Button(
                                onClick = {
                                    navController.navigate("editOrgProfileScreen"){
                                        popUpTo("ProfileOrganizer"){ inclusive = true }
                                    }
                                },
                                contentPadding = PaddingValues(10.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = lightBlueText.copy(0.1f),
                                ),
                                border = BorderStroke(0.5.dp, Color.White)
                            ) {
                                Row (
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ){

                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "edit",
                                        modifier = Modifier
                                            .size(22.dp)
                                            .clip(CircleShape)
                                            .padding(1.dp)
                                        , tint = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(text = "Edit Profile", color = Color.White, fontSize = 15.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                    }



                }
            }


        }
    }

}

