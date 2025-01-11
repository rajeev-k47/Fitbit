package net.runner.fitbit.WorkoutBuddyDashBoard.TopAppBar

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.firebase.auth.FirebaseAuth
import net.runner.fitbit.R
import net.runner.fitbit.auth.database.getProfileImageUrl
import net.runner.fitbit.ui.theme.background
import net.runner.fitbit.ui.theme.lightBlueText
import coil.ImageLoader
import coil.request.CachePolicy

@Composable
fun TopAppBarComposable(navController: NavController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    var imageUrl by rememberSaveable{mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        getProfileImageUrl{ url ->
            imageUrl = url
            Log.d("debug-url",url.toString())
        }
    }
        Box(modifier = Modifier
            .fillMaxWidth()
        )
        {
            Image(painter = painterResource(id = R.drawable.fitbit_logo), contentDescription = "" , modifier = Modifier
                .width(140.dp)
                .align(
                    Alignment.CenterStart
                ))

            Row (
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(horizontal = 10.dp)
                    .align(Alignment.CenterEnd)
                ,
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    painter = painterResource(id = R.drawable.ai),
                    contentDescription = "ai",
                    modifier = Modifier.size(26.dp).clip(CircleShape).padding(1.dp).clickable {
                        navController.navigate("chatBot")

                    }
                    , tint = lightBlueText
                )
                Spacer(modifier = Modifier.width(6.dp))
                AsyncImage(
                    model = if(imageUrl == null)auth.currentUser?.photoUrl else ImageRequest.Builder(context)
                        .data(imageUrl)
                        .build(),

                    imageLoader = ImageLoader.Builder(context)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .build(),
                    contentDescription = "Profile Picture",
                    placeholder = rememberAsyncImagePainter(R.drawable.user),
                    error = rememberAsyncImagePainter(R.drawable.user),
                    modifier = Modifier
                        .padding(10.dp)
                        .size(35.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            navController.navigate("profileBuddy")
                        },
                    contentScale = ContentScale.Crop
                )
            }

        }


}