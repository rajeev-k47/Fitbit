package net.runner.fitbit.OrganizerDashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import net.runner.fitbit.R
import net.runner.fitbit.ui.theme.background

@Composable
fun OrganizerDashBoard(){
    Box(modifier = Modifier
        .fillMaxSize()
        .background(background).statusBarsPadding()
        .clickable {
            FirebaseAuth.getInstance().signOut()
        }
    )
    {
        Image(painter = painterResource(id = R.drawable.logo1), contentDescription = "" , modifier = Modifier.size(300.dp).align(Alignment.CenterStart))

    }
}