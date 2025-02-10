package net.runner.fitbit.Profiles

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import com.google.firebase.auth.FirebaseAuth
import com.toolsforfools.shimmery.shimmerConfiguration.GradientType
import com.toolsforfools.shimmery.shimmerConfiguration.ShimmerType
import com.toolsforfools.shimmery.shimmerIndividual.shimmer
import net.runner.fitbit.Database.getData
import net.runner.fitbit.Firebase.fcmTokenSave
import net.runner.fitbit.R
import net.runner.fitbit.supportedconnections
import net.runner.fitbit.ui.theme.background
import net.runner.fitbit.ui.theme.lightBlueText
import net.runner.fitbit.ui.theme.lightText
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ProfileBuddy(navController: NavController,context:Context,editEnabled:Boolean,profileId: String) {
    val auth = FirebaseAuth.getInstance()

    var userData by rememberSaveable {
        mutableStateOf(mutableMapOf<String, Any>())
    }

    LaunchedEffect(Unit) {
        getData(profileId.ifEmpty { "" }){
            userData = it
        }
    }


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

                    if(editEnabled) {

                        Button(
                            onClick = {
                                fcmTokenSave(false)
                                navController.navigate("signUpScreen"){
                                    popUpTo("profileBuddy"){ inclusive = true }
                                }
                                auth.signOut()

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
                                painter = painterResource(id = R.drawable.logout),
                                contentDescription = "Logout",
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(start = 2.dp)
                                , tint = lightBlueText
                            )
                        }
                    }
                    else{
                        Button(
                            onClick = {
                                navController.navigate("userChat/${userData["userId"].toString()}")

                            },
                            contentPadding = PaddingValues(10.dp),
                            modifier = Modifier
                                .align(Alignment.CenterEnd),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = lightBlueText.copy(0.1f),
                            ),
                            border = BorderStroke(0.5.dp, Color.White)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.chat_group),
                                contentDescription = "Chat",
                                modifier = Modifier
                                    .size(18.dp)
                                    , tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Chat", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
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
                    SubcomposeAsyncImage(
                        model = userData["profileImageUrl"].toString().toUri() ,
//                            ?: auth.currentUser?.photoUrl,
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        imageLoader = ImageLoader.Builder(context)
                            .memoryCachePolicy(CachePolicy.ENABLED)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .build(),
                        loading = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .size(35.dp)
                                    .shimmer(true) {
                                        shimmerType = ShimmerType.WITH_ALPHA_AND_GRADIANT
                                        gradientType = GradientType.LINEAR
                                        shape = RoundedCornerShape(20.dp)
                                        gradientAnimationSpec = tween(1000)
                                        alphaAnimationSpec = tween(1300)
                                    },
                            )

                        },
                        modifier = Modifier
                            .width(100.dp)
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
                            text = userData["username"].toString(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = "@${userData["email"].toString()} • Joined ${
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
                        Text(
                            text = "Goals :",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Card (
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = lightText.copy(0.1f)
                            ),
                        ){

                            Text(
                                text = (userData["goalType"] as List<String>).joinToString(", "),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = lightText.copy(1f),
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Targets :",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Card (
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = lightText.copy(0.1f)
                            ),
                        ){

                            Text(
                                text = userData["targetValue"].toString() ,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = lightText.copy(1f),
                                modifier = Modifier.padding(10.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Bio :",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        )

                        Spacer(modifier = Modifier.height(5.dp))
                        Card (
                            modifier = Modifier
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = 150.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = lightText.copy(0.1f)
                            ),
                        ){

                            Text(
                                text = if(userData["Bio"]!=null)userData["Bio"].toString() else "Hi ${userData["username"]} here, I joined this community for my Fitness and Health Goals." ,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = lightText.copy(1f),
                                modifier = Modifier.padding(10.dp)
                            )
                        }





                        if(editEnabled){
                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = {
                                    navController.navigate("editProfileScreen"){
                                        popUpTo("profileBuddy"){ inclusive = true }
                                    }
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
                        }



                        Spacer(modifier = Modifier.height(20.dp))

                        if(userData["connections"] != null){
                            println(userData["connections"])
                            val connections = userData["connections"] as Map<*, *>
                            var isconnections = false
                            connections.forEach {
                                if(it.value.toString().isNotEmpty()){
                                    isconnections= true
                                }
                            }
                            if(isconnections){

                                Text(
                                    text = "Connections :",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                )
                            }
                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically){
                                items(connections.size) {index->
                                    val key = connections.keys.toList()[index]
                                    val value = connections.values.toList()[index]
                                    if(key.toString() in supportedconnections){
                                        if(key=="instagram" && value.toString().isNotEmpty()){
                                            connectionButton(context,"https://www.instagram.com/${value}",value.toString(), painterResource(id = R.drawable.instagram))
                                        }
                                        if (key=="x"&&value.toString().isNotEmpty()){
                                            connectionButton(context,"https://www.x.com/${value}",value.toString(), painterResource(id = R.drawable.x))
                                        }
                                        if (key=="facebook" && value.toString().isNotEmpty()){
                                            connectionButton(context,"https://www.facebook.com/${value}",value.toString(), painterResource(id = R.drawable.facebook))
                                        }
                                        if (key=="linkedin" && value.toString().isNotEmpty()){
                                            connectionButton(context,"https://www.linkedin.com/in/${value}",value.toString(), painterResource(id = R.drawable.linkedin))
                                        }
                                        if (key=="reddit" && value.toString().isNotEmpty()){
                                            connectionButton(context,"https://www.reddit.com/user/${value}",value.toString(), painterResource(id = R.drawable.reddit))
                                        }

                                    }

                                }

                            }

                        }



                    }



                }
            }


    }
    }

}

@Composable
fun connectionButton(context: Context,url: String,value: String,icon: Painter) {
    Button(
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        },
        contentPadding = PaddingValues(10.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = lightBlueText.copy(0.1f),
        ),
        border = BorderStroke(0.5.dp, Color.White.copy(0.6f))
    ) {
        Row (
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){

            Image(
                painter = icon,
                contentDescription = "icon",
                modifier = Modifier
                    .size(22.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = "@${value}", color = lightText, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
    }
}

fun getFormatedTime(dateString: String): String {
    val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)

    return try {
        val date = inputFormat.parse(dateString)
        val outputFormat = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
        outputFormat.format(date)
    } catch (e: Exception) {
        ""
    }




}