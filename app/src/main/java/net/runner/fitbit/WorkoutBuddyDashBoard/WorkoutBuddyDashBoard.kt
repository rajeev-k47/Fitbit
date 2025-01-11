package net.runner.fitbit.WorkoutBuddyDashBoard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import net.runner.fitbit.WorkoutBuddyDashBoard.BottomNavigationbar.BottomNavigationbarComposable
import net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.ExploreFragment.ExploreFragmentComposable
import net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.HomeFragment.HomeFragment
import net.runner.fitbit.ui.theme.background
import net.runner.fitbit.ui.theme.lightText

@Composable
fun WorkoutBuddyDashBoard(navController:NavController){
    
    var currentFragment by rememberSaveable { mutableStateOf("Home") }
    
    Box(modifier = Modifier
        .fillMaxSize()
        .background(background)
        .statusBarsPadding()){
        when (currentFragment) {
            "Home" -> {
                HomeFragment(navController = navController)
            }
            "Explore" -> {
                ExploreFragmentComposable()
            }
            "Activity" -> {
                Text(text = "Activity", color = lightText)
            }
            "Groups" -> {
                
            }
            "Create" -> {
                Text(text = "Create", color = lightText)
            }
        }
       

        BottomNavigationbarComposable(Modifier.align(Alignment.BottomCenter)){
            currentFragment = it
        }

    }
}