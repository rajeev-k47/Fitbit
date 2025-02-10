package net.runner.fitbit.WorkoutBuddyDashBoard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
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
import net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.ActivityFragment.ActivityFragmentComposable
import net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.CreateFragment.createContent
import net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.ExploreFragment.ExploreFragmentComposable
import net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.GroupsFragment.GroupFragmentComposable
import net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.HomeFragment.HomeFragment
import net.runner.fitbit.ui.theme.background
import net.runner.fitbit.ui.theme.lightText

@Composable
fun WorkoutBuddyDashBoard(navController:NavController){
    
    var currentFragment by rememberSaveable { mutableStateOf("Home") }
    var modalState by rememberSaveable {
        mutableStateOf(false)
    }
    
    Box(modifier = Modifier
        .fillMaxSize()
        .background(background)
        .statusBarsPadding().navigationBarsPadding()){
        when (currentFragment) {
            "Home" -> {
                HomeFragment(navController = navController)
            }
            "Explore" -> {
                ExploreFragmentComposable(navController = navController)
            }
            "Activity" -> {
                ActivityFragmentComposable(navController = navController)
            }
            "Groups" -> {
                GroupFragmentComposable(navController)
            }
        }

        if(modalState){
            createContent(modalStatus = modalState,navController) {
                modalState=it
            }
        }


        BottomNavigationbarComposable(Modifier.align(Alignment.BottomCenter)) {
            if(it!="Create"){
                currentFragment = it
            }else{
                modalState = true
            }
        }

    }
}