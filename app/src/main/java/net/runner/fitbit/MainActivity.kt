package net.runner.fitbit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import net.runner.fitbit.auth.SignUpComposable
import net.runner.fitbit.ui.theme.FitbitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FitbitTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "signUpScreen"
                ) {
                    composable(route = "signUpScreen") {
                        SignUpComposable(navController)
//                        LoginScreen(navController)
                    }
                    composable(route = "MainScreen") {
                    }

                }
            }
        }
    }
}

