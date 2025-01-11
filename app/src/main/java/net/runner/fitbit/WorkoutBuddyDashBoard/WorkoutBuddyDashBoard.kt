package net.runner.fitbit.WorkoutBuddyDashBoard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import net.runner.fitbit.WorkoutBuddyDashBoard.BottomNavigationbar.BottomNavigationbarComposable
import net.runner.fitbit.WorkoutBuddyDashBoard.TopAppBar.TopAppBarComposable
import net.runner.fitbit.ui.theme.background
import net.runner.fitbit.ui.theme.lightText

@Composable
fun WorkoutBuddyDashBoard(navController:NavController){
    Box(modifier = Modifier
        .fillMaxSize()
        .background(background)){

        LazyColumn(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                TopAppBarComposable(navController)
            }

        }

        BottomNavigationbarComposable(Modifier.align(Alignment.BottomCenter))

    }
}