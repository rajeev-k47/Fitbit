package net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.ExploreFragment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import net.runner.fitbit.Database.getUserData
import net.runner.fitbit.ui.theme.background

@Composable
fun ExploreContent() {
    var userData by rememberSaveable {
        mutableStateOf(mutableMapOf<String, Any>())
    }
    var peopleRelatedFeed by rememberSaveable {
        mutableStateOf(listOf<Pair<Map<String, Any>, Int>>())
    }
    LaunchedEffect(Unit) {
        getUserData{
            userData = it
            PeopleRelatedFeed((userData["goalType"] as List<String>) ){
                peopleRelatedFeed = it
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item{

            Text(text = "$peopleRelatedFeed.")

        }
    }
}