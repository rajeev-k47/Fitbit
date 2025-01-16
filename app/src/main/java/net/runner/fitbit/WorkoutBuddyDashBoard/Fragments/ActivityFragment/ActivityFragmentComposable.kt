package net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.ActivityFragment

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.runner.fitbit.ui.theme.lightText

@Composable
fun ActivityFragmentComposable() {

    val notifications = emptyList<String>()

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
        item {
            if(notifications.isEmpty()) {
                Box (
                    modifier = Modifier
                        .fillMaxSize()
                ){

                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }

    }
}