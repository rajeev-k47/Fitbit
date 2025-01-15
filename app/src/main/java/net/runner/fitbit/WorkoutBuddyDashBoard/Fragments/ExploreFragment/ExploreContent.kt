package net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.ExploreFragment

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import net.runner.fitbit.BuildConfig
import net.runner.fitbit.Database.getUserData
import net.runner.fitbit.GoogleAPis.fetchDistanceMatrix
import net.runner.fitbit.R
import net.runner.fitbit.ui.theme.background
import net.runner.fitbit.ui.theme.lightText

data class PeopleNearData(
    val personData: Pair<Map<String, Any>, Int>,
    var distance: String,
)

@Composable
fun ExploreContent(selectedFilter:String,typeFilerPeopleSelected:Boolean) {
    var userData by rememberSaveable {
        mutableStateOf(mutableMapOf<String, Any>())
    }
    var peopleRelatedFeed by rememberSaveable {
        mutableStateOf(listOf<Pair<Map<String, Any>, Int>>())
    }
    var userLocation by rememberSaveable {
        mutableStateOf(mutableMapOf<String, String>())
    }
    var peopleNearFeed by rememberSaveable {
        mutableStateOf(listOf<PeopleNearData>())
    }
    LaunchedEffect(Unit) {
        getUserData{
            userData = it
            val location = it["userLocation"]
                    as MutableMap<String, String>
            userLocation = location

            PeopleRelatedFeed((userData["goalType"] as List<String>) ){
                peopleRelatedFeed = it
                peopleNearFeed = it.map { person ->
                    PeopleNearData(personData = person, distance = "Calculating...")
                }
            }
        }

    }

    LaunchedEffect(peopleRelatedFeed) {
        val latitude = userLocation["latitude"].toString()
        val longitude = userLocation["longitude"].toString()
        peopleRelatedFeed.forEach {person->
            val personL = person.first["userLocation"] as MutableMap<String, String>
            val personLatitude = personL["latitude"].toString()
            val personLongitude = personL["longitude"].toString()
//            fetchDistanceMatrix(person.first["email"].toString(),"$latitude,$longitude","$personLatitude,$personLongitude",BuildConfig.DISTANCE_MATRIX_API_KEY, onSuccess = {
//                    pdistance, _,personemail ->
//                Log.d("gtr",pdistance)
//                peopleNearFeed = peopleNearFeed.map{
//                    if (it.personData.first["email"].toString() == personemail) {
//                        it.copy(distance = pdistance)
//                    } else {
//                        it
//                    }
//                }.sortedBy { person ->
//                    val distanceString = person.distance.trim()
//                    when {
//                        distanceString.endsWith("km") -> {
//                            distanceString.replace(" km", "").toDoubleOrNull()?.times(1000)
//                        }
//                        distanceString.endsWith("m") -> {
//                            distanceString.replace(" m", "").toDoubleOrNull()
//                        }
//                        else -> {
//                            Double.MAX_VALUE
//                        }
//                    } ?: Double.MAX_VALUE
//                }
//                Log.d("gtr", "Updated list: $peopleNearFeed")
//            }
//            ){
//                println(it)
//            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(typeFilerPeopleSelected && selectedFilter=="Related"){

            items(peopleRelatedFeed.size){index->

            PeopleExploreRelatedFeedCard(peopleRelatedFeed,index)

            }
        }
        else if(typeFilerPeopleSelected && selectedFilter=="Near you"){
            items(peopleNearFeed.size){index->

                PeopleExploreNearFeedCard(peopleNearFeed[index])
            }
        }
    }
}

@Composable
fun PeopleExploreNearFeedCard(peopleNearData: PeopleNearData){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
    ){
        AsyncImage(
            model = peopleNearData.personData.first["profileImageUrl"],
            contentDescription = "avatar",
            placeholder = rememberAsyncImagePainter(R.drawable.user),
            error = rememberAsyncImagePainter(R.drawable.user),
            modifier = Modifier
                .padding(10.dp)
                .size(58.dp)
                .clip(RoundedCornerShape(15.dp))
                .border(
                    width = 1.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(15.dp)
                ),
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

                    Text(text = peopleNearData.personData.first["username"].toString(), color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold, overflow = TextOverflow.Ellipsis, maxLines = 1, modifier = Modifier
                        .padding(end = 5.dp))
                    Spacer(modifier = Modifier.size(10.dp))
                }

                Text(text = "Distance from you : ${ peopleNearData.distance }", color = lightText, fontSize = 13.sp, overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.Bold,maxLines = 1, modifier = Modifier
                    .padding(end = 5.dp))

//                    Text(text = "@${repo.owner!!.login!!}", color =BottomNavigationIconUnselected, fontSize = 15.sp)
            }

        }

        Button(
            onClick = {
            },
            modifier = Modifier.padding(end = 14.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = lightText.copy(alpha = 0.1f))
        ) {
            Text(text = "View", color = lightText, fontWeight =  FontWeight.Bold)
        }


    }
}


@Composable
fun PeopleExploreRelatedFeedCard(peopleData:List<Pair<Map<String, Any>, Int>>,index:Int) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
        ){
            AsyncImage(
                model = peopleData[index].first["profileImageUrl"],
                contentDescription = "avatar",
                placeholder = rememberAsyncImagePainter(R.drawable.user),
                error = rememberAsyncImagePainter(R.drawable.user),
                modifier = Modifier
                    .padding(10.dp)
                    .size(58.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .border(
                        width = 1.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(15.dp)
                    ),
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

                        Text(text = peopleData[index].first["username"].toString(), color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold, overflow = TextOverflow.Ellipsis, maxLines = 1, modifier = Modifier
                            .padding(end = 5.dp))
                        Spacer(modifier = Modifier.size(10.dp))
                    }

                    Text(text = "Tagret : ${ peopleData[index].first["targetValue"].toString() }", color = Color.White, fontSize = 13.sp, overflow = TextOverflow.Ellipsis, maxLines = 1, modifier = Modifier
                        .padding(end = 5.dp))

//                    Text(text = "@${repo.owner!!.login!!}", color =BottomNavigationIconUnselected, fontSize = 15.sp)
                }

            }

            Button(
                onClick = {
                },
                modifier = Modifier.padding(end = 14.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = lightText.copy(alpha = 0.1f))
            ) {
                Text(text = "View", color = lightText, fontWeight =  FontWeight.Bold)
            }


        }

}