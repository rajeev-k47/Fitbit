package net.runner.fitbit.Chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import net.runner.fitbit.Database.getData
import net.runner.fitbit.R
import net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.GroupsFragment.GetGroupData
import net.runner.fitbit.ui.theme.background
import net.runner.fitbit.ui.theme.lightBlueText
import net.runner.fitbit.ui.theme.lightText

data class Message(
    val timestamp: Long = System.currentTimeMillis(),
    val userId: String = "",
    val content: String = ""
)


@Composable
fun ChatManager(uId:String,groupChat:Boolean,navController:NavController) {

    var groupChatData by rememberSaveable { mutableStateOf(listOf<Map<String, Any>>()) }
    var groupChatPeopleData by rememberSaveable {
        mutableStateOf(listOf<Map<String, Any>>())
    }
    var groupData by rememberSaveable {
        mutableStateOf(mapOf<String, Any>())
    }

    var UserChatData by rememberSaveable {
        mutableStateOf(listOf<Map<String, Any>>())
    }
    var participantsData by rememberSaveable {
        mutableStateOf(listOf<Map<String,Any>>())
    }

    LaunchedEffect(uId) {
        if(groupChat){
            getGroupChat(uId){
                groupChatData = it
            }
            GetGroupData(uId){
                groupData = it
            }

        }else{
            getUserChat(FirebaseAuth.getInstance().currentUser?.uid!!,uId){
                UserChatData =it
            }
        }
    }
    LaunchedEffect(groupChatData) {
        groupChatData.forEach { chat->
            getData(chat["userId"].toString()){
                if(groupChatPeopleData.none { it["userId"] == chat["userId"].toString()}){
                    groupChatPeopleData = groupChatPeopleData + it
                }
            }

        }
    }
    LaunchedEffect(UserChatData) {
        val participants = listOf(FirebaseAuth.getInstance().currentUser?.uid!!,uId)
        participants.forEach { it ->
            getData(it){
                participantsData+=it
            }
        }
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .background(background)
        .statusBarsPadding()
        .navigationBarsPadding()) {


        if (groupChat) {
            if (groupChatPeopleData.isNotEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Button(
                                onClick = {
                                    navController.popBackStack()
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = lightText.copy(alpha = 0.2f),
                                ),
                                contentPadding = PaddingValues(end = 3.dp),
                                modifier = Modifier
                                    .size(35.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.left_arrow),
                                    contentDescription = "back",
                                    modifier = Modifier.size(22.dp),
                                    tint = lightBlueText
                                )
                            }

                            Spacer(modifier = Modifier.width(15.dp))

                            AsyncImage(
                                model = groupData["profileImageUrl"],
                                contentDescription = "avatar",
                                placeholder = rememberAsyncImagePainter(R.drawable.user),
                                error = rememberAsyncImagePainter(R.drawable.user),
                                modifier = Modifier
                                    .size(45.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            val grpData = groupData["groupData"] as? Map<String, Any>
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(0.6f),
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.Top
                            ) {
                                Text(
                                    text = "${grpData?.get("organizationName")}",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "by ${grpData?.get("organizerName")}",
                                    color = lightText,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(0.4f)
                            ) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = {
                                        navController.navigate("groupInfo/${uId}")
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = lightText.copy(alpha = 0.2f),
                                    ),
                                    contentPadding = PaddingValues(0.dp),
                                    modifier = Modifier
                                        .size(35.dp)
                                        .align(Alignment.CenterVertically)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Info,
                                        contentDescription = "info",
                                        modifier = Modifier.size(22.dp),
                                        tint = lightBlueText
                                    )
                                }
                            }
                        }
                    }
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(5.dp))

                    ChatGroupComposable(groupChatData, groupChatPeopleData, uId)
                }

            }
        } else {
            if (participantsData.isNotEmpty() && UserChatData.isNotEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Button(
                                onClick = {
                                    navController.popBackStack()
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = lightText.copy(alpha = 0.2f),
                                ),
                                contentPadding = PaddingValues(end = 3.dp),
                                modifier = Modifier
                                    .size(35.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.left_arrow),
                                    contentDescription = "back",
                                    modifier = Modifier.size(22.dp),
                                    tint = lightBlueText
                                )
                            }

                            Spacer(modifier = Modifier.width(15.dp))
                            val participant =
                                participantsData.find { it["userId"] != FirebaseAuth.getInstance().currentUser?.uid!! }

                            AsyncImage(
                                model = participant?.get("profileImageUrl"),
                                contentDescription = "avatar",
                                placeholder = rememberAsyncImagePainter(R.drawable.user),
                                error = rememberAsyncImagePainter(R.drawable.user),
                                modifier = Modifier
                                    .size(45.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(0.6f),
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.Top
                            ) {
                                Text(
                                    text = "${participant?.get("username")}",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(0.4f)
                            ) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = {
                                        navController.navigate("profileBuddy/${uId}")
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = lightText.copy(alpha = 0.2f),
                                    ),
                                    contentPadding = PaddingValues(0.dp),
                                    modifier = Modifier
                                        .size(35.dp)
                                        .align(Alignment.CenterVertically)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Info,
                                        contentDescription = "info",
                                        modifier = Modifier.size(22.dp),
                                        tint = lightBlueText
                                    )
                                }
                            }
                        }
                    }
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(5.dp))

                    ChatUserComposable(UserChatData, participantsData)
                }
            }
        }


    }
}