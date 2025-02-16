package net.runner.fitbit.OrganizerDashboard.OrgFragments.OrgHomeFragment

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.toolsforfools.shimmery.shimmerConfiguration.GradientType
import com.toolsforfools.shimmery.shimmerConfiguration.ShimmerType
import com.toolsforfools.shimmery.shimmerIndividual.shimmer
import net.runner.fitbit.GroupPanel.GroupContent
import net.runner.fitbit.ImageCaching
import net.runner.fitbit.OrganizerDashboard.TopAppBar.OrgTopAppBarComposable
import net.runner.fitbit.Profiles.getFormatedTime
import net.runner.fitbit.R
import net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.GroupsFragment.GetGroupData
import net.runner.fitbit.ui.theme.background
import net.runner.fitbit.ui.theme.lightBlueText
import net.runner.fitbit.ui.theme.lightText

@Composable
fun OrgHomeFragment(navController: NavController) {
    var groupData by rememberSaveable {
        mutableStateOf(mapOf<String, Any>())
    }
    val groupId = FirebaseAuth.getInstance().currentUser?.uid!!

    LaunchedEffect (Unit){

        GetGroupData(groupId = groupId) {
            groupData = it

        }
    }

    val scrollstate = rememberLazyListState()
    LaunchedEffect(groupData) {
        if(groupData.size>0){
            scrollstate.scrollToItem(groupData.size - 1)
        }
    }

    Box(modifier = Modifier.fillMaxSize()){

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = scrollstate
        ) {
            item {
                OrgTopAppBarComposable(navController =navController)
            }
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
            item {
                Column (
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    SubcomposeAsyncImage(
                        model = ImageCaching().CacheBuilder(LocalContext.current, groupData["profileImageUrl"].toString()).build(),
                        contentDescription = "Profile Picture",
                        loading = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .shimmer(true) {
                                        shimmerType = ShimmerType.WITH_ALPHA_AND_GRADIANT
                                        gradientType = GradientType.LINEAR
                                        shape = RoundedCornerShape(25.dp)
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
                                    .clip(RoundedCornerShape(25.dp)),
                                contentScale = ContentScale.Crop
                            )
                        },
                        modifier = Modifier
                            .padding(10.dp)
                            .size(105.dp)
                            .clip(RoundedCornerShape(25.dp))
                            .border(0.5.dp, Color.White, RoundedCornerShape(25.dp))
                            .clickable {
//                            navController.navigate("profileBuddy")
                            },
                        contentScale = ContentScale.Crop
                    )
                    Text(text = "${(groupData["groupData"] as? Map<*, *>?)?.get("organizationName")}", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold, overflow = TextOverflow.Ellipsis, maxLines = 1, modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(text = "@${(groupData["groupData"] as? Map<*, *>?)?.get("organizerName")} • Created ${getFormatedTime(groupData["joiningDate"].toString())} ", color = lightText, fontSize = 12.sp, fontWeight = FontWeight.Bold, overflow = TextOverflow.Ellipsis, maxLines = 1, modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(text = "Total Members : ${(groupData["users"] as? List<String>)?.size}", color = lightText, fontSize = 12.sp, fontWeight = FontWeight.Bold, overflow = TextOverflow.Ellipsis, maxLines = 1, modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                        textAlign = TextAlign.Center
                    )

                }

            }

        }
        ExtendedFloatingActionButton(
            onClick = {  },
            icon = { Icon(Icons.Filled.Add, "") },
            text = { Text(text = "New Event") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 100.dp, end = 20.dp),
            containerColor = lightBlueText.copy(0.5f),
            contentColor = Color.White,
        )
    }


}