package net.runner.fitbit.splashScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import net.runner.fitbit.R

@Composable
fun splashScreen() {

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)){
        Image(painter = painterResource(id = R.drawable.logo1), contentDescription = "Logo", modifier = Modifier.size(300.dp).align(Alignment.Center))
    }


}