package net.runner.fitbit.OrganizerDashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import net.runner.fitbit.OrganizerDashboard.BottomNavigationBar.OrgBottomNavigationBarComposable
import net.runner.fitbit.OrganizerDashboard.OrgFragments.MembersFragment.MembersFragmentComposable
import net.runner.fitbit.OrganizerDashboard.OrgFragments.OrgHomeFragment.OrgHomeFragment
import net.runner.fitbit.OrganizerDashboard.OrgFragments.RequestsFragment.RequestsFragmentComposable
import net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.CreateFragment.createContent
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
        .statusBarsPadding().navigationBarsPadding()){
        when (currentFragment) {
            "Home" -> {
                OrgHomeFragment(navController)
            }
            "Members" -> {
                MembersFragmentComposable(navController)
            }
            "Activity" -> {
//                ActivityFragmentComposable()
            }
            "Requests" -> {
                RequestsFragmentComposable(navController)
            }
        }

        if(modalState){
//            createContent(modalStatus = modalState) {
//                modalState=it
//            }
        }


        OrgBottomNavigationBarComposable(Modifier.align(Alignment.BottomCenter)){
            if(it!="Create"){
                currentFragment = it
            }else{
//                modalState = true
            }
        }

    }
}