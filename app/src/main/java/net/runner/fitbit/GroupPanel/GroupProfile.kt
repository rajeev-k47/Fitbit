package net.runner.fitbit.GroupPanel

import androidx.compose.foundation.background
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
import net.runner.fitbit.R
import net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.GroupsFragment.GetGroupData
import net.runner.fitbit.ui.theme.background
import net.runner.fitbit.ui.theme.lightBlueText
import net.runner.fitbit.ui.theme.lightText

@Composable
fun GroupProfile(navController: NavController,groupId:String) {

    var groupData by rememberSaveable {
        mutableStateOf(mapOf<String, Any>())
    }

    LaunchedEffect (Unit){

        GetGroupData(groupId = groupId) {
            groupData = it

        }
    }


    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .statusBarsPadding()
    ){
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)){
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
                        Icon(painter = painterResource(id = R.drawable.left_arrow), contentDescription = "back", modifier = Modifier.size(22.dp), tint = lightBlueText)
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
                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.6f),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ){
                        Text(text = "${grpData?.get("organizationName")}", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(text = "by ${grpData?.get("organizerName")}", color = lightText, fontSize = 15.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }

                    Row (
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.4f)
                    ) {
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
                                .align(Alignment.CenterVertically)
                        ) {
                            Icon(painter = painterResource(id = R.drawable.chat_group), contentDescription = "chat", modifier = Modifier.size(16.dp), tint = lightBlueText)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                navController.navigate("groupInfo/${groupId}")
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
                            Icon(imageVector = Icons.Filled.Info, contentDescription = "info", modifier = Modifier.size(22.dp), tint = lightBlueText)
                        }
                    }
                }
            }
            HorizontalDivider()
            GroupContent(groupData)


        }



    }
}