package net.runner.fitbit.OrganizerDashboard.OrgFragments.OrgActivityFragment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import net.runner.fitbit.R
import net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.ActivityFragment.Notification
import net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.ActivityFragment.fetchNotifications
import net.runner.fitbit.ui.theme.lightText
import java.text.SimpleDateFormat

@Composable
fun OrgActivityComposable(navController: NavController, switchToRequestsFragment: () -> Unit) {

    var notifications by rememberSaveable {
        mutableStateOf(listOf<Notification>())
    }
    var notificationstate by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        fetchNotifications { it ->
            notifications = it.sortedByDescending{
                SimpleDateFormat("dd MMM, hh:mm aa").parse(it.date)?.time
            }
            if(notifications.isEmpty()){
                notificationstate=true
            }
            println(notifications)
        }
    }

    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp),
    ){
        item {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 20.dp)
            ) {
                Text(text = "Notifications", modifier = Modifier.align(Alignment.CenterStart), color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)

            }
        }
        if(!notificationstate) {

            items(notifications.size) { notification ->
                NotificationCard(notification = notifications[notification], navController){
                    switchToRequestsFragment()
                }
            }


        }else{
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp, vertical = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "No Notifications...", color = lightText, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }

    }
}

@Composable
fun NotificationCard(notification: Notification, navController: NavController, switchToRequestsFragment: () -> Unit) {
    val context= LocalContext.current
    var name by rememberSaveable {
        mutableStateOf("")
    }
    LaunchedEffect(notification) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(notification.sender).get()
            .addOnSuccessListener { document ->
                    name = document.getString("username") ?: ""
            }
            .addOnFailureListener { exception ->
            }
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 2.dp),
        colors = CardDefaults.cardColors(containerColor = lightText.copy(0.1f))
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
            ,
            verticalAlignment = Alignment.CenterVertically,
        ){
            Icon(painter = painterResource(id = R.drawable.ringing), contentDescription = "", modifier = Modifier
                .weight(0.2f)
                .height(25.dp), tint = lightText
            )
            Column(
                modifier = Modifier
                    .weight(0.7f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "${notification.title} from $name", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(end = 10.dp))
                Text(text = notification.date, color = Color.Red , fontSize = 10.sp, fontWeight = FontWeight.Bold, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(end = 10.dp))

            }
            if(notification.redirection != ""){

                Icon(painter = painterResource(id = R.drawable.link), contentDescription = "", modifier = Modifier
                    .weight(0.1f)
                    .height(20.dp).padding(end = 10.dp)

                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        switchToRequestsFragment()

                    }
                    ,
                    tint = lightText
                )
            }


        }
    }
}