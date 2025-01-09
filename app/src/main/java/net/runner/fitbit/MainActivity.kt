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
import net.runner.fitbit.auth.database.checkIfAccountExists
import net.runner.fitbit.sharedPreference.getTempEmail
import net.runner.fitbit.sharedPreference.removeTempEmail
import net.runner.fitbit.sharedPreference.tempEmailSignUp
import net.runner.fitbit.splashScreen.splashScreen
import net.runner.fitbit.ui.theme.FitbitTheme
import net.runner.fitbit.userDetails.UserDetailComposable

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private var email =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val intent = intent
        val emailLink = intent.data
        email = getTempEmail(this@MainActivity).toString()
        auth = Firebase.auth
        if(email.isNotEmpty()&&emailLink.toString().isNotEmpty()){
            chechAuth(email,emailLink?.query.toString(),auth,this@MainActivity)
        }

        setContent {
            FitbitTheme {
                val navController = rememberNavController()
                var startDestination = "splashScreen"
                if (auth.currentUser != null) {
                    checkIfAccountExists(auth.currentUser!!.email?:""){exists, accountType ->
                        if (exists) {
                            Log.d("gri",accountType.toString())
                            startDestination = when (accountType) {
                                "Workoutbuddy"-> "dashBoardBuddy"
                                "Organizer"-> "dashBoardOrganizer"
                                else-> "userDetails"
                            }
                        } else {
                            startDestination = "userDetails"
                        }
                    }
                }else{
                    startDestination = "signUpScreen"
                }
                NavHost(
                    navController = navController,
                    startDestination = startDestination
                ) {
                    composable(route = "splashScreen") {
                        splashScreen()
                    }
                    composable(route = "signUpScreen") {
                        SignUpComposable(navController, activity = this@MainActivity)
                    }
                    composable(route = "userDetails") {
                        UserDetailComposable(navController)
                    }
                    composable(route = "dashBoardBuddy") {

                    }
                    composable(route = "dashBoardOrganizer") {

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

fun chechAuth(email:String,emailLink:String,auth: FirebaseAuth,context: Context){
    if (auth.isSignInWithEmailLink(emailLink)) {
        auth.signInWithEmailLink(email, emailLink)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("TAG","Successfully signed in with email link!")
                    val result = task.result
                    removeTempEmail(context)
                    Log.d("gri",result.toString())
                } else {
                    Log.e("TAG","Error signing in with email link", task.exception)
                }
            }
    }else{
        Log.d("TAG","Not signed in with email link")
    }

}