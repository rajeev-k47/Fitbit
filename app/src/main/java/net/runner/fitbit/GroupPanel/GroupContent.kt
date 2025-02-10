package net.runner.fitbit.GroupPanel

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import com.toolsforfools.shimmery.shimmerConfiguration.GradientType
import com.toolsforfools.shimmery.shimmerConfiguration.ShimmerType
import com.toolsforfools.shimmery.shimmerIndividual.shimmer
import net.runner.fitbit.Profiles.getFormatedTime
import net.runner.fitbit.R
import net.runner.fitbit.ui.theme.lightText

@Composable
fun GroupContent(groupData: Map<String, Any>) {
    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
    ){
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
                    model = groupData["profileImageUrl"],
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
                Text(text = "${(groupData["groupData"] as? Map<*, *>?)?.get("organizationName")}", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold, overflow = TextOverflow.Ellipsis, maxLines = 1, modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    textAlign = TextAlign.Center
                    )
                Text(text = "@${(groupData["groupData"] as? Map<*, *>?)?.get("organizerName")} • Created ${getFormatedTime(groupData["joiningDate"].toString())} ", color = lightText, fontSize = 12.sp, fontWeight = FontWeight.Bold, overflow = TextOverflow.Ellipsis, maxLines = 1, modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    textAlign = TextAlign.Center
                    )
                Text(text = "Total Members : ${(groupData["users"] as? List<String>)?.size}", color = lightText, fontSize = 12.sp, fontWeight = FontWeight.Bold, overflow = TextOverflow.Ellipsis, maxLines = 1, modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    textAlign = TextAlign.Center
                    )

            }

        }
    }

}