package net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.HomeFragment

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import net.runner.fitbit.Database.getData
import net.runner.fitbit.UserFeed.UserFeedComposable
import net.runner.fitbit.UserFeed.getPosts
import net.runner.fitbit.WorkoutBuddyDashBoard.TopAppBar.TopAppBarComposable

@Composable
fun HomeFragment(navController: NavController) {
    var feed by rememberSaveable { mutableStateOf(listOf<Map<String, Any>>()) }
    val context = LocalContext.current
    var feedAuthors by rememberSaveable {
        mutableStateOf(listOf<Pair<Map<String, Any>, String>>())
    }

    LaunchedEffect(
        Unit
    ) {
        getPosts{
            feed=it
        }
    }
    LaunchedEffect(feed) {
        feed.forEach {element->
            getData(element["author"].toString()){
                feedAuthors += Pair(it,element["postId"].toString())
            }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            TopAppBarComposable(navController)
        }
        if(feedAuthors.isNotEmpty()){

            items(
                feed.size
            ) {index->
                val post = feed[index]
                val matchingAuthor = feedAuthors.find { it.second == post["postId"].toString() }
                if (matchingAuthor != null) {
                    UserFeedComposable(context, post, matchingAuthor)
                }
            }
        }
        item {
            Spacer(modifier = Modifier.padding(50.dp))
        }

    }
}