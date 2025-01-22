package net.runner.fitbit.UserFeed

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import net.runner.fitbit.R
import net.runner.fitbit.ui.theme.background
import net.runner.fitbit.ui.theme.lightText

@Composable
fun UserFeedComposable(context: Context, Feed: Map<String, Any>, FeedAuthors: Pair<Map<String, Any>, String>) {

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
                                .clickable {
                                    val redirectionLink =
                                        FeedAuthors.first["referenceLink"].toString()
                                    try {
                                        val intent = Intent(Intent.ACTION_VIEW)
                                        intent.data = Uri.parse(redirectionLink)
                                        context.startActivity(intent)
                                    }catch (error:Exception){
                                        Toast.makeText(context, "Invalid Link", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            , verticalAlignment = Alignment.CenterVertically
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

            Text(text = "Facilities :", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold, overflow = TextOverflow.Ellipsis, maxLines = 1, modifier = Modifier
                .padding(horizontal = 10.dp))
            Text(text =" facilities.joinToString", color = lightText, fontSize = 14.sp, fontWeight = FontWeight.Bold, overflow = TextOverflow.Ellipsis, maxLines = 1, modifier = Modifier
                .padding(horizontal = 10.dp))
            Spacer(modifier = Modifier.height(10.dp))

            AsyncImage(
                model = "groupNearDat",
                contentDescription = "avatar",
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(15.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {


                },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 3.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                ),
            ) {
                Text("Join", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

        }
    }
}