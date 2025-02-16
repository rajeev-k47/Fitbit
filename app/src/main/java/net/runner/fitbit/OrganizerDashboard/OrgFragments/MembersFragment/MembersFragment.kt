package net.runner.fitbit.OrganizerDashboard.OrgFragments.MembersFragment

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
import com.toolsforfools.shimmery.shimmerConfiguration.GradientType
import com.toolsforfools.shimmery.shimmerConfiguration.ShimmerType
import com.toolsforfools.shimmery.shimmerIndividual.shimmer
import net.runner.fitbit.ImageCaching
import net.runner.fitbit.OrganizerDashboard.OrgFragments.RequestsFragment.ApproveRequest
import net.runner.fitbit.OrganizerDashboard.OrgFragments.RequestsFragment.getPendingRequests
import net.runner.fitbit.OrganizerDashboard.OrgFragments.RequestsFragment.getPendingUserData
import net.runner.fitbit.R
import net.runner.fitbit.ui.theme.lightBlueText
import net.runner.fitbit.ui.theme.lightText

@Composable
fun MembersFragmentComposable(navController: NavController) {
    var searchPendingQuery by rememberSaveable { mutableStateOf("") }
    var Users by rememberSaveable {
        mutableStateOf(listOf<String>())
    }
    var GroupUsersData by rememberSaveable {
        mutableStateOf(listOf<Map<String, Any>>())
    }
    var filteredUsersData by rememberSaveable {
        mutableStateOf(listOf<Map<String, Any>>())
    }
    var isLoaded by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect (Unit){
        getGroupUsers("") {
            Users = it
        }
    }
    LaunchedEffect (Users){
        if(isLoaded)return@LaunchedEffect
        Users.forEach { user->
            getPendingUserData(user){
                GroupUsersData = (GroupUsersData + it)
                filteredUsersData = GroupUsersData
            }
        }
        if(Users.isNotEmpty()){
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

            Text(text = "Members", color = Color.White, fontSize = 18.sp, modifier = Modifier
                .padding(start = 5.dp)
                .align(Alignment.CenterStart), fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = searchPendingQuery,
            onValueChange = {
                searchPendingQuery = it
                filteredUsersData = GroupUsersData.filter { user->
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
            items(filteredUsersData.size)
            {index->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    SubcomposeAsyncImage(
                        model = ImageCaching().CacheBuilder(LocalContext.current, filteredUsersData[index]["profileImageUrl"].toString()).build(),
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

                                Text(text = filteredUsersData[index]["username"].toString(), color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold, overflow = TextOverflow.Ellipsis, maxLines = 1, modifier = Modifier
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
                            navController.navigate("profileBuddy/${GroupUsersData[index]["userId"].toString()}")

                        },
                        modifier = Modifier.padding(end = 14.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = lightText.copy(alpha = 0.1f))
                    ) {
                        Text(text ="Details", color = lightText, fontWeight =  FontWeight.Bold)
                    }


                }
            }
            item { Spacer(modifier = Modifier.height(70.dp)) }

        }


    }
}