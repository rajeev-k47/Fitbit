package net.runner.fitbit.Profiles

import android.content.Context
import android.util.Log
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import net.runner.fitbit.Database.getUserData
import net.runner.fitbit.R
import net.runner.fitbit.auth.database.getProfileImageUrl
import net.runner.fitbit.ui.theme.background
import net.runner.fitbit.ui.theme.lightBlueText
import net.runner.fitbit.ui.theme.lightText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun ProfileBuddy(navController: NavController,context:Context) {
    val auth = FirebaseAuth.getInstance()
    var imageUrl by rememberSaveable{ mutableStateOf<String?>(null) }

    var userData by rememberSaveable {
        mutableStateOf(mutableMapOf<String, Any>())
    }
//    var joiningDate by rememberSaveable {
//        mutableStateOf("")
//    }


    LaunchedEffect(Unit) {
        getProfileImageUrl{ url ->
            imageUrl = url
        }
        getUserData{
            userData = it
            println(userData)
        }
    }
//    LaunchedEffect(userData) {
//        joiningDate=getFormatedTime(userData["joiningDate"].toString())
//        Log.d("gri",userData["joiningDate"].toString())
//    }


    Box(modifier = Modifier
        .fillMaxSize()
        .background(background)
        .statusBarsPadding()
    ){
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                )
                {
                    Spacer(modifier = Modifier.height(60.dp))

                    Text(text = "Profile", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 10.dp))

                    Button(
                        onClick = {
                            auth.signOut()
                            navController.navigate("signUpScreen"){
                                popUpTo("profileBuddy"){ inclusive = true }
                            }

                        },
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier
                            .size(34.dp)
                            .align(Alignment.CenterEnd),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = lightBlueText.copy(0.1f),
                        ),
                        border = BorderStroke(0.5.dp, lightText.copy(0.4f))
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.setting),
                            contentDescription = "ai",
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .padding(1.dp)
                            , tint = lightBlueText
                        )
                    }
                        Spacer(modifier = Modifier.width(6.dp))


            }
        }
            item {
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp))
            }
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(100.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = lightText.copy(alpha = 0.1f)
                    )
                ) {
                    Log.d("debug-url",imageUrl.toString())
                    AsyncImage(
                        model = imageUrl?.toUri() ?: auth.currentUser?.photoUrl,
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        imageLoader = ImageLoader.Builder(context)
                            .memoryCachePolicy(CachePolicy.ENABLED)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .build(),
                        modifier = Modifier
                            .fillMaxWidth(0.25f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(20.dp))
                            .border(
                                2.dp,
                                Color.Gray.copy(0.4f),
                                RoundedCornerShape(20.dp)
                            ),
                    )
                }
            }
            item{
                Spacer(modifier = Modifier.height(25.dp))
            }
            if(userData.isNotEmpty()){

                item {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    )

                    {

                        Text(
                            text = auth.currentUser?.displayName ?: "",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = "@${userData["username"].toString()} • Joined ${
                                getFormatedTime(
                                    userData["joiningDate"].toString()
                                )
                            }",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = lightText.copy(1f),
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(
                                text = "${userData["followers"].toString()} ",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                            )

                            Text(
                                text = "Followers",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = lightText.copy(1f),
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "${userData["following"].toString()} ",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                            )

                            Text(
                                text = "Following",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = lightText.copy(1f),
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {

                            },
                            contentPadding = PaddingValues(10.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = lightBlueText.copy(0.1f),
                            ),
                            border = BorderStroke(0.5.dp, Color.White)
                        ) {
                            Row (
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ){

                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "ai",
                                    modifier = Modifier
                                        .size(22.dp)
                                        .clip(CircleShape)
                                        .padding(1.dp)
                                    , tint = Color.White
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(text = "Edit Profile", color = Color.White, fontSize = 15.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        if(userData["connections"] != null){
                            for ((key, value) in userData["connections"] as Map<*, *>) {
                                Log.d("userData", "Key: $key, Value: $value")
                                Text(
                                    text = "Connections : $value",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                )
                            }

                        }


                    }



                }
            }


    }
    }

}

fun getFormatedTime(dateString: String): String {
    val inputFormat = SimpleDateFormat("dd MMMM yyyy 'at' HH:mm:ss z", Locale.ENGLISH)

    try {

        val date = inputFormat.parse(dateString)
        val outputFormat = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
        return outputFormat.format(date)
    }catch (e:Exception){
        return ""
    }


}