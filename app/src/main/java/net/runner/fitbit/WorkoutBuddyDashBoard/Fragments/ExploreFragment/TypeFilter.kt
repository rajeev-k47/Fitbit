package net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.ExploreFragment

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


@Composable
fun TypeFilter(selected: Boolean, title:String, Icon: Painter, onClick: (Boolean) -> Unit) {

    FilterChip(
        onClick = { onClick(!selected) },
        label = {
            Text(text = title, color = Color.White.copy(0.8f), fontWeight = FontWeight.Bold, fontSize = 14.sp)
        },
        selected = selected,
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            {
            Icon(
                painter = Icon,
                contentDescription = "",
                modifier = Modifier.size(FilterChipDefaults.IconSize),
                tint = Color.White
            )
            }


        },
    )
}