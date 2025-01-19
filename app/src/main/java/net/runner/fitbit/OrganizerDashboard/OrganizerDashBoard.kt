package net.runner.fitbit.OrganizerDashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import net.runner.fitbit.OrganizerDashboard.BottomNavigationBar.OrgBottomNavigationBarComposable
import net.runner.fitbit.OrganizerDashboard.Fragments.OrgHomeFragment
import net.runner.fitbit.R
import net.runner.fitbit.WorkoutBuddyDashBoard.BottomNavigationbar.BottomNavigationbarComposable
import net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.ActivityFragment.ActivityFragmentComposable
import net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.CreateFragment.createContent
import net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.ExploreFragment.ExploreFragmentComposable
import net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.GroupsFragment.GroupFragmentComposable
import net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.HomeFragment.HomeFragment
import net.runner.fitbit.ui.theme.background

@Composable
fun OrganizerDashBoard(navController: NavController){
    var currentFragment by rememberSaveable { mutableStateOf("Home") }
    var modalState by rememberSaveable {
        mutableStateOf(false)
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(background)
        .statusBarsPadding()){
        when (currentFragment) {
            "Home" -> {
                OrgHomeFragment(navController)
//                HomeFragment(navController = navController)
            }
            "Explore" -> {
//                ExploreFragmentComposable(navController = navController)
            }
            "Activity" -> {
//                ActivityFragmentComposable()
            }
            "Groups" -> {
//                GroupFragmentComposable(navController)
            }
        }

        if(modalState){
            createContent(modalStatus = modalState) {
                modalState=it
            }
        }


        OrgBottomNavigationBarComposable(Modifier.align(Alignment.BottomCenter)){

        }

    }
}