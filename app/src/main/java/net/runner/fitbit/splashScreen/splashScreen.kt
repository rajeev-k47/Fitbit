package net.runner.fitbit.splashScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun splashScreen() {

    var timer by remember { mutableStateOf(2) }
    LaunchedEffect(Unit) {
        while (timer > 0) {
            delay(1000)
            timer--
        }

    }

}