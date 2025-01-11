package net.runner.fitbit.WorkoutBuddyDashBoard.BottomNavigationbar

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import net.runner.fitbit.R
import net.runner.fitbit.ui.theme.background
import net.runner.fitbit.ui.theme.lightBlueText
import net.runner.fitbit.ui.theme.lightText

data class NavigationItem(
    val label: String,
    val filledIcon: Painter? = null,
    val unfilledIcon: Painter? = null,
    val onclick :() -> Unit
)

@Composable
fun BottomNavigationbarComposable(modifier: Modifier,onselected:(String) -> Unit) {

    var selectedItem by rememberSaveable { mutableStateOf("Home") }


    val navigationItems = listOf(
        NavigationItem(
            label = "Home",
            filledIcon = painterResource(id = R.drawable.home_filled),
            unfilledIcon = painterResource(id = R.drawable.home_unfilled),
            onclick = {onselected("Home")}
        ),
        NavigationItem(
            label = "Explore",
            filledIcon = painterResource(id = R.drawable.explore_filled),
            unfilledIcon = painterResource(id = R.drawable.explore_unfilled),
            onclick = {
                onselected("Explore")
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
            label = "Groups",
            filledIcon = painterResource(id = R.drawable.group_filled),
            unfilledIcon = painterResource(id = R.drawable.group_unfilled),
            onclick = {
                onselected("Groups")
            }
        )
    )

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
                if(item.label.isNotEmpty()){

                    Column(
                        modifier = Modifier
                            .clickable {
                                selectedItem = item.label
                                item.onclick()
                            },
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
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
                else{
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