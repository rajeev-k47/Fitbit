package net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.HomeFragment

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import net.runner.fitbit.WorkoutBuddyDashBoard.TopAppBar.TopAppBarComposable

@Composable
fun HomeFragment(navController: NavController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            TopAppBarComposable(navController)
        }

    }
}