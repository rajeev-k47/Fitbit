package net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.GroupsFragment

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.toolsforfools.shimmery.shimmerConfiguration.GradientType
import com.toolsforfools.shimmery.shimmerConfiguration.ShimmerType
import com.toolsforfools.shimmery.shimmerIndividual.shimmer
import net.runner.fitbit.ImageCaching
import net.runner.fitbit.R
import net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.ExploreFragment.GroupNearData
import net.runner.fitbit.ui.theme.lightBlueText
import net.runner.fitbit.ui.theme.lightText

@Composable
fun GroupFragmentComposable(navController: NavController) {

    var searchGroupQuery by rememberSaveable { mutableStateOf("") }
    var userGroupsData by rememberSaveable {
        mutableStateOf(listOf<Map<String, Any>>())
    }
    var userGroups by rememberSaveable {
        mutableStateOf(listOf<Map<String, Any>>())
    }
    var filteredGroupData by rememberSaveable {
        mutableStateOf(listOf<Map<String, Any>>())
    }
    var emptystate by rememberSaveable {
        mutableStateOf(false)
    }
    var isLoaded by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect (Unit){
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if(userGroups.isEmpty()){

            GetUserGroups(userId.toString()){
                userGroups=it
                if(userGroups.isEmpty()){
                    emptystate=true
                }
            }
        }
    }
    LaunchedEffect (userGroups){
        if(isLoaded)return@LaunchedEffect
        userGroups.forEach { group->
            val groupId = group["groupId"]
            GetGroupData(groupId.toString()){
                userGroupsData = (userGroupsData + it)
                filteredGroupData = userGroupsData
                println(userGroupsData)

            }
        }
        if(userGroups.isNotEmpty()){
            isLoaded=true
        }

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
            .padding(top = 5.dp),
        horizontalAlignment = Alignment.Start
    ){
        Box(modifier = Modifier.fillMaxWidth()){

            Text(text = "Groups", color = Color.White, fontSize = 18.sp, modifier = Modifier
                .padding(start = 5.dp)
                .align(Alignment.CenterStart), fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = searchGroupQuery,
            onValueChange = {
                                searchGroupQuery = it
                                filteredGroupData = userGroupsData.filter {group->
                                    val orgName = (group["groupData"] as Map<String, String>)["organizationName"]!!
                                    orgName.lowercase().contains(searchGroupQuery.lowercase())
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
            if(emptystate){
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 10.dp, vertical = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "No Joined groups found", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Explore and join groups", color = lightText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            items(filteredGroupData.size)
            {index->
                val groupId = filteredGroupData[index]["groupId"]
                val groupStatus = userGroups.firstOrNull{it["groupId"] == groupId }?.get("status")


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    SubcomposeAsyncImage(
                        model = ImageCaching().CacheBuilder(LocalContext.current, filteredGroupData[index]["profileImageUrl"].toString()).build(),
                        contentDescription = "avatar",
                        loading = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .shimmer(true) {
                                        shimmerType = ShimmerType.WITH_ALPHA_AND_GRADIANT
                                        gradientType = GradientType.LINEAR
                                        shape = RoundedCornerShape(15.dp)
                                        gradientAnimationSpec = tween(1000)
                                        alphaAnimationSpec = tween(1300)
                                    },
                            )

                        },
                        error = {
                            Image(
                                painter = painterResource(id = R.drawable.user),
                                contentDescription = "avatar",
                                modifier = Modifier
                                    .clip(RoundedCornerShape(15.dp)),
                                contentScale = ContentScale.Crop
                            )
                        },
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

//                                val groupData = userGroupsData[index]["groupData"] as Map<*, String>
                                Text(text = (userGroupsData[index]["groupData"] as Map<String, String>)["organizationName"]!!, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold, overflow = TextOverflow.Ellipsis, maxLines = 1, modifier = Modifier
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
                            if (groupStatus != "Pending"){
                                navController.navigate("group/${groupId}"){
                                    popUpTo("dashboardBuddy"){inclusive = false}
                                }
                            }
                        },
                        modifier = Modifier.padding(end = 14.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = lightText.copy(alpha = 0.1f))
                    ) {
                        Text(text = if(groupStatus == "Pending") "Requested" else "View", color = lightText, fontWeight =  FontWeight.Bold)
                    }


                }
            }
            item { Spacer(modifier = Modifier.height(70.dp)) }

        }


    }
}