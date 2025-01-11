package net.runner.fitbit.WorkoutBuddyDashBoard.TopAppBar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import net.runner.fitbit.R
import net.runner.fitbit.auth.database.getProfileImageUrl
import net.runner.fitbit.ui.theme.background

@Composable
fun TopAppBarComposable() {

    val auth = FirebaseAuth.getInstance()
    var imageUrl by rememberSaveable{mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        getProfileImageUrl{ url ->
            imageUrl = url
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
                AsyncImage(
                    model = if(imageUrl == null)auth.currentUser?.photoUrl else imageUrl,
                    contentDescription = "Profile Picture",
                    placeholder = rememberAsyncImagePainter(R.drawable.user),
                    error = rememberAsyncImagePainter(R.drawable.user),
                    modifier = Modifier
                        .padding(10.dp)
                        .size(35.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }

        }


}