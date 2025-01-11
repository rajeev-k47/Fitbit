package net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.ExploreFragment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.runner.fitbit.ui.theme.lightText

@Composable
fun FeedFilter(label:String,isSelected: String,onclick:(String)->Unit) {
    Button(
        onClick = { onclick(isSelected)},
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if(label==isSelected)lightText.copy(0.15f) else Color.Transparent,
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 1.dp),
        modifier = Modifier.height(30.dp).padding(horizontal = 10.dp)
    ) {
        Text(text = label, color = lightText, fontWeight = FontWeight.Bold, fontSize = 15.sp)
    }

}