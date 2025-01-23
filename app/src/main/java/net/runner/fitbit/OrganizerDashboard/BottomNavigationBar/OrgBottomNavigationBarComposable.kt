package net.runner.fitbit.OrganizerDashboard.BottomNavigationBar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import net.runner.fitbit.R
import net.runner.fitbit.WorkoutBuddyDashBoard.BottomNavigationbar.NavigationItem
import net.runner.fitbit.ui.theme.background
import net.runner.fitbit.ui.theme.lightBlueText
import net.runner.fitbit.ui.theme.lightText

@Composable
fun OrgBottomNavigationBarComposable(modifier: Modifier,onselected:(String)->Unit) {
    var selectedItem by rememberSaveable { mutableStateOf("Home") }
    var isPrivate by rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        val db = FirebaseFirestore.getInstance()
        val userUid = FirebaseAuth.getInstance().currentUser?.uid
        if (userUid != null) {
            db.collection("users").document(userUid).get()
                .addOnSuccessListener { documentSnapshot ->
                    isPrivate = documentSnapshot.getBoolean("private") ?: false
                }
        }
    }



    val navigationItems = mutableListOf(
        NavigationItem(
            label = "Home",
            filledIcon = painterResource(id = R.drawable.home_filled),
            unfilledIcon = painterResource(id = R.drawable.home_unfilled),
            onclick = {onselected("Home")}
        ),
        NavigationItem(
            label = "Members",
            filledIcon = painterResource(id = R.drawable.group_filled),
            unfilledIcon = painterResource(id = R.drawable.group_unfilled),
            onclick = {
                onselected("Members")
            }
        ),
        NavigationItem(
            label = "",
            onclick = {
                onselected("Create")
            }
        ),
        NavigationItem(
            label = "Activity",
            filledIcon = painterResource(id = R.drawable.activity_filled),
            unfilledIcon = painterResource(id = R.drawable.activity_unfilled),
            onclick = {
                onselected("Activity")
            }
        ),

        NavigationItem(
            label = "Requests",
            filledIcon = painterResource(id = R.drawable.requests_filled),
            unfilledIcon = painterResource(id = R.drawable.requests_unfilled),
            onclick = {
                onselected("Requests")
            }
        )
    )
    if(!isPrivate){
        val temp = navigationItems[2]
        navigationItems[2] = navigationItems[3]
        navigationItems[3] = temp
    }

    Card(
        modifier = modifier
            .padding(bottom = 10.dp)
            .padding(horizontal = 10.dp)
            .border(0.5.dp, lightText, shape = RoundedCornerShape(20.dp))
            .shadow(5.dp, shape = RoundedCornerShape(20.dp), spotColor = lightText)
        ,
        colors = CardDefaults.cardColors(
            containerColor = background
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),

        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navigationItems.forEach { item ->
                if(item.label.isNotEmpty()&&(isPrivate||item.label!="Requests")){
                    Box(
                        modifier = Modifier.size(55.dp).clickable(
                            onClick = {
                                selectedItem = item.label
                                item.onclick()
                            },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ),
                        contentAlignment = Alignment.Center
                    ){

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = if (selectedItem == item.label) item.filledIcon!! else item.unfilledIcon!!,
                                contentDescription = item.label,
                                tint = if (selectedItem == item.label) Color.White else lightText,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = item.label,
                                color = if (selectedItem == item.label) Color.White else lightText,
                                style = MaterialTheme.typography.labelSmall,
                                maxLines = 1
                            )
                        }
                    }

                }
                else if (isPrivate||item.label!="Requests"){
                    Button(
                        onClick = {
                            item.onclick()
                        },
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier
                            .size(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = lightBlueText.copy(0.1f),
                        ),
                        border = BorderStroke(1.dp, lightText.copy(0.4f))
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "", modifier = Modifier.size(26.dp), tint = lightText)
                    }
                }
            }
        }
    }
}