package net.runner.fitbit

import android.content.Context
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
    private var email =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val intent = intent
        val emailLink = intent.data.toString()
        auth = Firebase.auth

        if(email.isNotEmpty()){
            chechAuth(email,emailLink,auth)
        }
        val sharedPreferences = this.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val userExists =
            try {
                sharedPreferences.getString("user_exists_${auth.currentUser?.uid.toString()}", null)
            } catch (e: Exception) {
                "false"
            }

        setContent {
            FitbitTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = if (auth.currentUser==null)"signUpScreen" else if (userExists == "true") "dashBoardBuddy" else "userDetails"
                ) {
                    composable(route = "signUpScreen") {
                        SignUpComposable(navController, activity = this@MainActivity){data ->
                            email=data
                        }
                    }
                    composable(route = "userDetails") {
                        UserDetailComposable(navController)
                    }
                    composable(route = "dashBoardBuddy") {

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

fun chechAuth(email:String,emailLink:String,auth: FirebaseAuth){
    if (auth.isSignInWithEmailLink(emailLink)) {
        auth.signInWithEmailLink(email, emailLink)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("TAG","Successfully signed in with email link!")
                    val result = task.result
                    Log.d("gri",result.toString())
                } else {
                    Log.e("TAG","Error signing in with email link", task.exception)
                }
            }
    }

}