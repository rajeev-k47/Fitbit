package net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.ExploreFragment

import android.os.Parcelable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.parcel.Parcelize
import net.runner.fitbit.Database.getUserData
import net.runner.fitbit.R
import net.runner.fitbit.ui.theme.background
import net.runner.fitbit.ui.theme.lightText

@Parcelize
data class PeopleNearData(
    val personData: Pair<Map<String, Any>, Int>,
    var distance: String,
) : Parcelable
@Parcelize
data class GroupNearData(
    val GroupData: Pair<Map<String, Any>, Int>,
    var distance: String,
) : Parcelable

@Composable
fun ExploreContent(selectedFilter:String,typeFilerPeopleSelected:Boolean,navController: NavController) {
    var userData by rememberSaveable {
        mutableStateOf(mutableMapOf<String, Any>())
    }
    var peopleRelatedFeed by rememberSaveable {
        mutableStateOf(listOf<Pair<Map<String, Any>, Int>>())
    }
    var userLocation by rememberSaveable {
        mutableStateOf(mutableMapOf<String, String>())
    }
    var groupRelatedFeed by rememberSaveable {
        mutableStateOf(listOf<Pair<Map<String, Any>, Int>>())
    }
    var peopleNearFeed by rememberSaveable {
        mutableStateOf(listOf<PeopleNearData>())
    }
    var groupNearFeed by rememberSaveable {
        mutableStateOf(listOf<GroupNearData>())
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
            GroupsRelatedFeed((userData["goalType"] as List<String>)){
                groupRelatedFeed = it.filterNot { group ->
                    val userGroups = userData["groups"] as? List<Map<String, Any>> ?: return@filterNot false
                    userGroups.any { userGroup -> userGroup["groupId"] == group.first["groupId"] }
                }.map { group ->
                    Pair(group.first,group.second)

                }
                groupNearFeed = groupRelatedFeed.map {group->
                    GroupNearData(GroupData = group, distance = "Calculating...")
                }
            }
        }

    }
    LaunchedEffect (groupRelatedFeed){
        val latitude = userLocation["latitude"].toString()
        val longitude = userLocation["longitude"].toString()
        groupRelatedFeed.forEach { group->
            val groupL = group.first["userLocation"] as MutableMap<String, String>
            val groupLatitude = groupL["latitude"].toString()
            val groupLongitude = groupL["longitude"].toString()
//                        fetchDistanceMatrix(group.first["email"].toString(),"$latitude,$longitude","$groupLatitude,$groupLongitude",BuildConfig.DISTANCE_MATRIX_API_KEY, onSuccess = {
//                    gdistance, _,groupemail ->
//                groupNearFeed = groupNearFeed.map{
//                    if (it.GroupData.first["email"].toString() == groupemail) {
//                        it.copy(distance = gdistance)
//                    } else {
//                        it
//                    }
//                }.sortedBy { group ->
//                    val distanceString = group.distance.trim()
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
//            }
//            ){
//                println(it)
//            }
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
        else if(!typeFilerPeopleSelected && selectedFilter=="Related"){
            items(groupRelatedFeed.size){index->
                
                GroupExploreRelatedFeedCard(groupRelatedFeed[index],navController)
            }
        }else if(!typeFilerPeopleSelected && selectedFilter=="Near you"){
            items(groupNearFeed.size){index->
                GroupExploreNearFeedCard(groupNearFeed[index],navController)
                
            }
        }
        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun GroupExploreNearFeedCard(groupNearData: GroupNearData,navController: NavController){
    val orgData= groupNearData.GroupData.first["groupData"] as Map<*, *>
    val facilities = groupNearData.GroupData.first["facilities"] as List<String>
    var joinStatus by rememberSaveable {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    Spacer(modifier = Modifier.height(10.dp))
    Card(
        colors = CardColors(
            containerColor = background,
            contentColor = lightText,
            disabledContentColor = lightText,
            disabledContainerColor = background
        ),
        border = BorderStroke(
            0.5.dp,
            Brush.verticalGradient(
                colors = listOf(lightText, Color.Black),
                startY = 0.0f,
                endY = 35.0f
            )
        ),
        modifier = Modifier.fillMaxWidth()

    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Icon(painter = painterResource(id = R.drawable.location), contentDescription = "", tint = lightText, modifier = Modifier
                .size(25.dp)
                .padding(horizontal = 5.dp))
            Text(text = "${groupNearData.distance}", color = lightText, fontSize = 14.sp, fontWeight = FontWeight.Bold, overflow = TextOverflow.Ellipsis, maxLines = 1, modifier = Modifier)
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 15.dp)
        ) {

            Row (
                modifier = Modifier.fillMaxWidth()
            ){

                AsyncImage(
                    model = groupNearData.GroupData.first["profileImageUrl"],
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
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = orgData["organizationName"].toString(), color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold, overflow = TextOverflow.Ellipsis, maxLines = 1, modifier = Modifier
                            .padding(end = 5.dp)
                            .align(Alignment.CenterStart))
                        Row(modifier = Modifier.align(Alignment.CenterEnd), verticalAlignment = Alignment.CenterVertically){
                            Icon(painter = painterResource(id = R.drawable.clock), contentDescription = "",Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "${groupNearData.GroupData.first["orgStartTime"]} to ${groupNearData.GroupData.first["orgEndTime"]}", color = lightText, fontSize = 13.sp, fontWeight = FontWeight.Bold, overflow = TextOverflow.Ellipsis, maxLines = 1, modifier = Modifier
                                .padding(end = 5.dp))
                        }

                    }

                    Text(text = "by ${orgData["organizerName"].toString()}", color = lightText, fontSize = 13.sp, fontWeight = FontWeight.Bold, overflow = TextOverflow.Ellipsis, maxLines = 1, modifier = Modifier
                        .padding(end = 5.dp))
                }

            }

            Text(text = "Facilities :", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold, overflow = TextOverflow.Ellipsis, maxLines = 1, modifier = Modifier
                .padding(horizontal = 10.dp))
            Text(text = facilities.joinToString(","), color = lightText, fontSize = 14.sp, fontWeight = FontWeight.Bold, overflow = TextOverflow.Ellipsis, maxLines = 1, modifier = Modifier
                .padding(horizontal = 10.dp))
            Spacer(modifier = Modifier.height(10.dp))

            AsyncImage(
                model = groupNearData.GroupData.first["banner"],
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
                    val auth = FirebaseAuth.getInstance()
                    val userUid = auth.currentUser?.uid
                    if(!joinStatus){
                        ManagerGroupJoining(context,userUid!!,groupNearData.GroupData.first["groupId"].toString()){private->
                            if(private){
                                joinStatus = true
                            }else{
                                navController.navigate("group/${groupNearData.GroupData.first["groupId"].toString()}"){
                                    popUpTo("dashBoardBuddy") { inclusive =false }
                                }
                            }
                        }
                    }

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
                Text(if(joinStatus) "Requested" else "Join", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
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
                .clip(RoundedCornerShape(15.dp)),
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
                    .clip(RoundedCornerShape(15.dp)),
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

                    Text(text = "Target : ${ peopleData[index].first["targetValue"].toString() }", color = lightText, fontSize = 13.sp,fontWeight = FontWeight.Bold, overflow = TextOverflow.Ellipsis, maxLines = 1, modifier = Modifier
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
fun GroupExploreRelatedFeedCard(groupData :Pair<Map<String, Any>, Int>,navController: NavController){
    val orgData= groupData.first["groupData"] as Map<*, *>
    val facilities = groupData.first["facilities"] as List<String>
    val auth = FirebaseAuth.getInstance()
    val userUid = auth.currentUser?.uid
    var joinStatus by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    Spacer(modifier = Modifier.height(10.dp))
    Card(
        colors = CardColors(
            containerColor = background,
            contentColor = lightText,
            disabledContentColor = lightText,
            disabledContainerColor = background
        ),
        border = BorderStroke(
            0.5.dp,
            Brush.verticalGradient(
                    colors = listOf(lightText, Color.Black),
                startY = 0.0f,
                endY = 35.0f
            )
        ),
        modifier = Modifier.fillMaxWidth()

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
                    model = groupData.first["profileImageUrl"],
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
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = orgData["organizationName"].toString(), color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold, overflow = TextOverflow.Ellipsis, maxLines = 1, modifier = Modifier
                            .padding(end = 5.dp)
                            .align(Alignment.CenterStart))
                        Row(modifier = Modifier.align(Alignment.CenterEnd), verticalAlignment = Alignment.CenterVertically){
                            Icon(painter = painterResource(id = R.drawable.clock), contentDescription = "",Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "${groupData.first["orgStartTime"]} to ${groupData.first["orgEndTime"]}", color = lightText, fontSize = 13.sp, fontWeight = FontWeight.Bold, overflow = TextOverflow.Ellipsis, maxLines = 1, modifier = Modifier
                                .padding(end = 5.dp))
                        }

                    }

                    Text(text = "by ${orgData["organizerName"].toString()}", color = lightText, fontSize = 13.sp, fontWeight = FontWeight.Bold, overflow = TextOverflow.Ellipsis, maxLines = 1, modifier = Modifier
                        .padding(end = 5.dp))
                }

            }

            Text(text = "Facilities :", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold, overflow = TextOverflow.Ellipsis, maxLines = 1, modifier = Modifier
                .padding(horizontal = 10.dp))
            Text(text = facilities.joinToString(","), color = lightText, fontSize = 14.sp, fontWeight = FontWeight.Bold, overflow = TextOverflow.Ellipsis, maxLines = 1, modifier = Modifier
                .padding(horizontal = 10.dp))
            Spacer(modifier = Modifier.height(10.dp))

            AsyncImage(
                model = groupData.first["banner"],
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
                    if(!joinStatus){

                    ManagerGroupJoining(context,userUid!!,groupData.first["groupId"].toString()){private->
                        if(private){
                            joinStatus = true
                        }
                        else{

                            navController.navigate("group/${groupData.first["groupId"].toString()}"){
                                popUpTo("dashBoardBuddy") { inclusive =false }
                            }
                        }

                    }
                    }
                },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .fillMaxWidth()
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
                Text(text = if(joinStatus) "Requested" else "Join", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

        }
    }
    
}