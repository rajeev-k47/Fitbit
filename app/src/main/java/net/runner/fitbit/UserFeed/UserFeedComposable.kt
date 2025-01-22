package net.runner.fitbit.UserFeed

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import net.runner.fitbit.R
import net.runner.fitbit.ui.theme.background
import net.runner.fitbit.ui.theme.lightText

@Composable
fun UserFeedComposable(context: Context, Feed: Map<String, Any>, FeedAuthors: Pair<Map<String, Any>, String>) {
    var liked by rememberSaveable { mutableStateOf(false) }

    Spacer(modifier = Modifier.height(10.dp))
    Card(
        colors = CardColors(
            containerColor = background,
            contentColor = lightText,
            disabledContentColor = lightText,
            disabledContainerColor = background
        ),
        border = BorderStroke(
            0.25.dp,
            Brush.verticalGradient(
                colors = listOf(lightText, Color.Transparent),
                startY = 0.0f,
                endY = 35.0f
            )
        ),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 15.dp)
        ) {

            Row (
                modifier = Modifier.fillMaxWidth()
            ){

                AsyncImage(
                    model = FeedAuthors.first["profileImageUrl"],
                    error = painterResource(id = R.drawable.user),
                    contentDescription = "avatar",
                    modifier = Modifier
                        .padding(10.dp)
                        .size(48.dp)
                        .clip(RoundedCornerShape(15.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(10.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),

                    ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text =FeedAuthors.first["username"].toString(), color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold, overflow = TextOverflow.Ellipsis, maxLines = 1, modifier = Modifier
                            .padding(end = 5.dp)
                            .weight(0.6f), textAlign = TextAlign.Start
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Row(
                            modifier = Modifier
                                .weight(0.4f)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    val redirectionLink =
                                        Feed["referenceLink"].toString()
                                    try {
                                        val intent = Intent(Intent.ACTION_VIEW)
                                        intent.data = Uri.parse(redirectionLink)
                                        context.startActivity(intent)
                                    } catch (error: Exception) {
                                        Toast
                                            .makeText(context, "Invalid Link", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }
                            , verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ){
                            Icon(painter = painterResource(id = R.drawable.link), contentDescription = "",
                                Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Read More", color = lightText, fontSize = 15.sp, fontWeight = FontWeight.Bold, overflow = TextOverflow.Ellipsis, maxLines = 1, modifier = Modifier
                                .padding(end = 10.dp))
                        }

                    }
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Text(text = getFormatedTimeFeed(Feed["dateTime"].toString()), color = lightText, fontSize = 13.sp, fontWeight = FontWeight.Bold, overflow = TextOverflow.Ellipsis, maxLines = 1, modifier = Modifier
                            .padding(end = 5.dp))
                    }

                }

            }
            Spacer(modifier = Modifier.height(15.dp))


            Text(text = Feed["title"].toString(), color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold, overflow = TextOverflow.Ellipsis, maxLines = 3, modifier = Modifier
                .padding(horizontal = 10.dp))

            Spacer(modifier = Modifier.height(5.dp))


            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items((Feed["tags"] as? List<String> ?: emptyList()).size) { index ->
                    Card (
                        colors = CardColors(
                            containerColor = lightText.copy(0.1f),
                            contentColor = Color.White,
                            disabledContentColor = Color.White,
                            disabledContainerColor = Color.Red
                        )
                    ){
                        Text("# ${(Feed["tags"] as List<String>)[index]}", fontSize = 14.sp, color = lightText, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp))
                    }


                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            AsyncImage(
                model = "${Feed["bannerImageUri"]}",
                contentDescription = "banner",
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .border(0.5.dp, lightText.copy(0.4f), RoundedCornerShape(15.dp))
                ,
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Card (
                colors = CardColors(
                    containerColor = lightText.copy(0.1f),
                    contentColor = Color.White,
                    disabledContentColor = Color.White,
                    disabledContainerColor = Color.Red
                ),
                    modifier = Modifier.padding(horizontal = 10.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(indication = LocalIndication.current, interactionSource = remember { MutableInteractionSource() }) {
                        liked = !liked
                        manageLikeStatus(FeedAuthors.second, FirebaseAuth.getInstance().currentUser!!.uid)

                    }
            ){
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,

                ) {
                    Icon(painter = if(!liked) painterResource(id = R.drawable.hand_unliked) else painterResource(id = R.drawable.hand_like), tint = lightText,contentDescription = "", modifier = Modifier.padding(10.dp)
                        .size(18.dp)
                        .align(Alignment.CenterVertically))
                    Text(if (!liked) "Like" else "Liked", fontSize = 15.sp, color = lightText, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 10.dp))
                }
            }

        }
    }
}