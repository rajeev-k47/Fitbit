package net.runner.fitbit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import net.runner.fitbit.auth.SignUpComposable
import net.runner.fitbit.ui.theme.FitbitTheme
import net.runner.fitbit.userDetails.UserDetailComposable

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = Firebase.auth
        setContent {
            FitbitTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "signUpScreen"
                ) {
                    composable(route = "signUpScreen") {
                        SignUpComposable(navController, activity = this@MainActivity)
                    }
                    composable(route = "userDetails") {
                        UserDetailComposable(navController)
                    }

                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001) {
            try {
                val credential = Identity.getSignInClient(this).getSignInCredentialFromIntent(data)
                val idToken = credential.googleIdToken
                if (idToken != null) {
                    val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                    auth.signInWithCredential(firebaseCredential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = auth.currentUser
                            } else {
                                Log.w("FirebaseAuth","signInWithCredential:failure",task.exception)
                            }
                        }
                } else {
                    Log.e("SignIn","No ID token!")
                }
            } catch (e: ApiException) {
                Log.e("SignIn","Sign-in failed",e)
            }
        }
    }

}

