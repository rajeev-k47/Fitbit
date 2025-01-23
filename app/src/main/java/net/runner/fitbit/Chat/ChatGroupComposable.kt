package net.runner.fitbit.Chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import net.runner.fitbit.R
import net.runner.fitbit.ui.theme.lightBlueText
import net.runner.fitbit.ui.theme.lightText

@Composable
fun ChatGroupComposable(groupChats: List<Map<String, Any>>, groupChatPeopleData: List<Map<String, Any>>,grpUId: String) {
    var myChatText by rememberSaveable { mutableStateOf("") }


    Box(modifier = Modifier
        .fillMaxSize()
    )
    {
        val scrollState = rememberLazyListState()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 70.dp),
            state = scrollState
        ) {
            items(groupChats.size) { index ->
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp),
                    horizontalArrangement = if (groupChats[index]["userId"] != FirebaseAuth.getInstance().currentUser?.uid) {
                        Arrangement.Start
                    } else {
                        Arrangement.End
                    }
                ){


                    if (groupChats[index]["userId"] == FirebaseAuth.getInstance().currentUser?.uid ) {
                        if(index==0){

                            AsyncImage(
                                model = groupChatPeopleData.find { it["userId"] == groupChats[index]["userId"] }
                                    ?.get("profileImageUrl"),
                                contentDescription = "Profile Picture",
                                modifier = Modifier
                                    .padding(10.dp)
                                    .size(35.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }else if( groupChats[index-1]["userId"] != FirebaseAuth.getInstance().currentUser?.uid){
                            AsyncImage(
                                model = groupChatPeopleData.find { it["userId"] == groupChats[index]["userId"] }
                                    ?.get("profileImageUrl"),
                                contentDescription = "Profile Picture",
                                modifier = Modifier
                                    .padding(10.dp)
                                    .size(35.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                    Text(
                        text = groupChats[index]["content"].toString(),
                        modifier = Modifier
                            .padding(2.dp)
                            .background(
                                if (groupChats[index]["userId"] == FirebaseAuth.getInstance().currentUser?.uid)
                                    lightText.copy(0.1f)
                                else
                                    lightText.copy(0.3f),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .padding(12.dp)
                            .widthIn(min = 50.dp, max = 300.dp),
                        color = if (groupChats[index]["userId"] == FirebaseAuth.getInstance().currentUser?.uid)
                            lightText
                        else
                            MaterialTheme.colorScheme.onSecondaryContainer,
                        style = MaterialTheme.typography.titleMedium
                    )
                    if (groupChats[index]["userId"] != FirebaseAuth.getInstance().currentUser?.uid ) {
                        if(index==0){

                            AsyncImage(
                                model = groupChatPeopleData.find { it["userId"] == groupChats[index]["userId"] }?.get("profileImageUrl"),
                                contentDescription = "Profile Picture",
                                modifier = Modifier
                                    .padding(10.dp)
                                    .size(35.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }else if(groupChats[index]["userId"] != groupChats[index-1]["userId"]){
                            AsyncImage(
                                model = groupChatPeopleData.find { it["userId"] == groupChats[index]["userId"] }?.get("profileImageUrl"),
                                contentDescription = "Profile Picture",
                                modifier = Modifier
                                    .padding(10.dp)
                                    .size(35.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }


                }
            }
        }

        TextField(
            value = myChatText,
            onValueChange = { myChatText = it },
            placeholder = { Text(text = "Type something...", color = lightBlueText, fontSize = 17.sp) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
                .padding(horizontal = 10.dp)
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(22.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    if (myChatText.isNotBlank()) {
                        sendGroupMessage(grpUId, myChatText)
                        myChatText = ""

                    }
                }
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = lightText.copy(alpha = 0.1f),
                unfocusedContainerColor = lightText.copy(alpha = 0.1f),
                focusedIndicatorColor = Color.Red.copy(alpha = 0.5f),
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.chat),
                    contentDescription = "",
                    modifier = Modifier.size(25.dp),
                    tint = lightBlueText
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (myChatText.isNotBlank()) {
                            sendGroupMessage(grpUId, myChatText)
                            myChatText = ""
                        }
                    },
                    Modifier.size(35.dp)
                        .clip(CircleShape)
                ) {

                    Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = lightBlueText

                    )
                }
            }
        )
    }
}