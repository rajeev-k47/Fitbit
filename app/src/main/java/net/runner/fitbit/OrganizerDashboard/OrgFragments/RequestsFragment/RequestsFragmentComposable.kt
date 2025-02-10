package net.runner.fitbit.OrganizerDashboard.OrgFragments.RequestsFragment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import net.runner.fitbit.ui.theme.lightBlueText
import net.runner.fitbit.ui.theme.lightText

@Composable
fun RequestsFragmentComposable(navController: NavController) {
    var searchPendingQuery by rememberSaveable { mutableStateOf("") }
    var GroupPendingData by rememberSaveable {
        mutableStateOf(listOf<String>())
    }
    var PendingUsers by rememberSaveable {
        mutableStateOf(listOf<Map<String, Any>>())
    }
    var filteredPendingData by rememberSaveable {
        mutableStateOf(listOf<Map<String, Any>>())
    }
    var isLoaded by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect (Unit){
        getPendingRequests {
            GroupPendingData = it
        }
    }
    LaunchedEffect (GroupPendingData){
        if(isLoaded)return@LaunchedEffect
        GroupPendingData.forEach { user->
            getPendingUserData(user){
                PendingUsers = (PendingUsers + it)
                filteredPendingData = PendingUsers
            }
        }
        if(GroupPendingData.isNotEmpty()){
            isLoaded=true
        }

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
            .padding(top = 15.dp),
        horizontalAlignment = Alignment.Start
    ){
        Box(modifier = Modifier.fillMaxWidth()){

            Text(text = "Pending Requests", color = Color.White, fontSize = 18.sp, modifier = Modifier
                .padding(start = 5.dp)
                .align(Alignment.CenterStart), fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = searchPendingQuery,
            onValueChange = {
                searchPendingQuery = it
                filteredPendingData = PendingUsers.filter { user->
                    user["username"].toString().lowercase().contains(searchPendingQuery.lowercase())
                }
            },
            placeholder = { Text(text = "Search", color = lightBlueText, fontSize = 17.sp) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
            ,
            shape = RoundedCornerShape(16.dp),
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.search), contentDescription = "", modifier = Modifier.size(25.dp), tint = lightText)
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = lightText.copy(alpha = 0.1f),
                unfocusedContainerColor = lightText.copy(alpha = 0.1f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        LazyColumn(
            Modifier.fillMaxWidth()
        ) {
            items(filteredPendingData.size)
            {index->
                var text by rememberSaveable { mutableStateOf("Approve") }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth().clickable {
                            navController.navigate("profileBuddy/${filteredPendingData[index]["userId"].toString()}")
                        }
                ){
                    AsyncImage(
                        model = filteredPendingData[index]["profileImageUrl"],
                        contentDescription = "avatar",
                        placeholder = rememberAsyncImagePainter(R.drawable.user),
                        error = rememberAsyncImagePainter(R.drawable.user),
                        modifier = Modifier
                            .padding(10.dp)
                            .size(58.dp)
                            .clip(RoundedCornerShape(15.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp)
                        .weight(1f)
                    ){
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Row (
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ){

                                Text(text = filteredPendingData[index]["username"].toString(), color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold, overflow = TextOverflow.Ellipsis, maxLines = 1, modifier = Modifier
                                    .padding(end = 5.dp))
                                Spacer(modifier = Modifier.size(10.dp))
                            }

//                            Text(text = "Target : ${ [index].first["targetValue"].toString() }", color = lightText, fontSize = 13.sp, overflow = TextOverflow.Ellipsis, maxLines = 1, modifier = Modifier
//                                .padding(end = 5.dp))

//                    Text(text = "@${repo.owner!!.login!!}", color =BottomNavigationIconUnselected, fontSize = 15.sp)
                        }

                    }


                    Button(
                        onClick = {
                            if(text!="Approved") ApproveRequest(filteredPendingData[index]["userId"].toString())
                            text = "Approved"
                        },
                        modifier = Modifier.padding(end = 14.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = lightText.copy(alpha = 0.1f))
                    ) {
                        Text(text =text, color = lightText, fontWeight =  FontWeight.Bold)
                    }


                }
            }
            item { Spacer(modifier = Modifier.height(70.dp)) }

        }


    }
}