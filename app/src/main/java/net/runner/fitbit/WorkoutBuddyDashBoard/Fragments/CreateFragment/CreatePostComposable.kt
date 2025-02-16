package net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.CreateFragment

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import net.runner.fitbit.ImageCaching
import net.runner.fitbit.R
import net.runner.fitbit.ui.theme.background
import net.runner.fitbit.ui.theme.lightBlueText
import net.runner.fitbit.ui.theme.lightText

@Composable
fun CreatePostComposable(navController: NavController) {
    var bannerImageUri by rememberSaveable {
        mutableStateOf(Uri.EMPTY)
    }
    val pickBanner = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            bannerImageUri = uri
        }
    }
    var PostTitle by rememberSaveable {
        mutableStateOf("")
    }
    var PostDescription by rememberSaveable {
        mutableStateOf("")
    }
    var tags by rememberSaveable {
        mutableStateOf(listOf<String>())
    }
    var currentTag by rememberSaveable { mutableStateOf("") }
    var referenceLink by rememberSaveable {
        mutableStateOf("")
    }


    LaunchedEffect(Unit) {

    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .statusBarsPadding(),

        ) {
        item{
            Box (
                modifier = Modifier.fillMaxWidth(),
            ){

                Button(
                    onClick = {
                        navController.popBackStack()
                    },
                    contentPadding = PaddingValues(10.dp),
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 3.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = lightText.copy(alpha = 0.1f),
                    ),
                    border = BorderStroke(
                        0.5.dp,
                        Color.White
                    )
                ) {
                    Text("Cancel", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        createNewPost(FirebaseAuth.getInstance().currentUser?.uid!!,PostTitle,PostDescription,tags,bannerImageUri,referenceLink)
                        navController.popBackStack()
                    },
                    contentPadding = PaddingValues(10.dp),
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                        .align(Alignment.CenterEnd),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                    )
                ) {
                    Text("Creat Post", color = background, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        item {
            HorizontalDivider(Modifier.padding(top = 5.dp))
        }
        item {
                Text(text = "New Post", modifier = Modifier
                    .padding(10.dp)
                    .padding(top = 10.dp), color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        item {
            Spacer(modifier = Modifier.height(30.dp))
            Text(text = "Title", color = lightText, fontSize = 15.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 25.dp))
            TextField(
                value = PostTitle,
                onValueChange = { text -> PostTitle = text },
                placeholder = { Text(text = "Title", color = lightBlueText, fontSize = 17.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = lightText.copy(alpha = 0.1f),
                    unfocusedContainerColor = lightText.copy(alpha = 0.1f),
                    focusedIndicatorColor = Color.Red.copy(alpha = 0.5f),
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.title),
                        contentDescription = "",
                        modifier = Modifier.size(24.dp),
                        tint = lightBlueText
                    )
                }
            )
        }
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Banner", color = lightText, fontSize = 15.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 25.dp))

            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(140.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(lightText.copy(0.1f))
                    .clickable {
                        pickBanner.launch("image/*")
                    }
            ){
                if(bannerImageUri != Uri.EMPTY){

                    AsyncImage(
                        model = ImageCaching().CacheBuilder(LocalContext.current, bannerImageUri.toString()).build(),
                        contentDescription = "banner",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .background(lightText.copy(0.1f))
                        ,
                        contentScale = ContentScale.Crop
                    )
                }else{
                    Text(text = "Upload Banner", color = lightText, fontSize = 20.sp, modifier = Modifier.align(Alignment.Center), fontWeight = FontWeight.Bold)
                }
            }

            }
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Description", color = lightText, fontSize = 15.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 25.dp))
            TextField(
                value = PostDescription,
                onValueChange = { text -> PostDescription = text },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = lightText.copy(alpha = 0.1f),
                    unfocusedContainerColor = lightText.copy(alpha = 0.1f),
                    focusedIndicatorColor = Color.Red.copy(alpha = 0.5f),
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.description),
                        contentDescription = "",
                        modifier = Modifier.size(24.dp),
                        tint = lightBlueText
                    )
                }
            )
            Spacer(modifier = Modifier.height(8.dp))


        }
        item {
            Text("Tags", fontSize = 15.sp, color = lightText, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 25.dp))
            Spacer(modifier = Modifier.height(1.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(tags.size) { index ->
                        Card (
                            colors = CardColors(
                                containerColor = lightText.copy(0.1f),
                                contentColor = Color.White,
                                disabledContentColor = Color.White,
                                disabledContainerColor = Color.Red
                            )
                        ){
                            Text("# ${tags[index]}", fontSize = 15.sp, color = lightText, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp))


                        }

                    }
                }


                TextField(
                    value = currentTag,
                    onValueChange = { newValue ->
                        if (newValue.contains(" ")) {
                            val newTag = newValue.trim()
                            if (newTag.isNotEmpty()) {
                                tags = tags + newTag
                            }
                            currentTag = ""
                        } else {
                            currentTag = newValue
                        }
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.hastag),
                            contentDescription = "",
                            modifier = Modifier.size(24.dp),
                            tint = lightBlueText
                        )
                    },
                    label = {
                        Text("Enter space to create multiple tags", color = lightBlueText.copy(0.4f))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = lightText.copy(alpha = 0.1f),
                        unfocusedContainerColor = lightText.copy(alpha = 0.1f),
                        focusedIndicatorColor = Color.Red.copy(alpha = 0.5f),
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    )
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
        item {
            Text(text = "Reference Link", color = lightText, fontSize = 15.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 25.dp))
            TextField(
                value = referenceLink,
                onValueChange = { text -> referenceLink = text },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = lightText.copy(alpha = 0.1f),
                    unfocusedContainerColor = lightText.copy(alpha = 0.1f),
                    focusedIndicatorColor = Color.Red.copy(alpha = 0.5f),
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                label = {
                    Text("e.g. https://medium.com/3jfu3haudb", color = lightBlueText.copy(0.4f))
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.link),
                        contentDescription = "",
                        modifier = Modifier.size(24.dp),
                        tint = lightBlueText
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Spacer(modifier = Modifier.height(300.dp))
        }

    }
}