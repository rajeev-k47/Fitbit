package net.runner.fitbit.OrganizerDashboard.Fragments

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import net.runner.fitbit.OrganizerDashboard.TopAppBar.OrgTopAppBarComposable

@Composable
fun OrgHomeFragment(navController: NavController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            OrgTopAppBarComposable(navController =navController)
        }

    }
}