package net.runner.fitbit

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.Coil
import coil.ImageLoader
import coil.request.CachePolicy
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.onesignal.OneSignal
import com.onesignal.debug.LogLevel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.runner.fitbit.Firebase.fcmTokenSave
import net.runner.fitbit.WorkoutBuddyDashBoard.TopAppBar.Chatbot.ChatbotPanel
import net.runner.fitbit.OrganizerDashboard.OrganizerDashBoard
import net.runner.fitbit.Profiles.EditProfileBuddy
import net.runner.fitbit.Profiles.ProfileBuddy
import net.runner.fitbit.WorkoutBuddyDashBoard.WorkoutBuddyDashBoard
import net.runner.fitbit.auth.SignUpComposable
import net.runner.fitbit.auth.database.checkIfAccountExists
import net.runner.fitbit.sharedPreference.getTempEmail
import net.runner.fitbit.sharedPreference.removeTempEmail
import net.runner.fitbit.sharedPreference.tempEmailSignUp
import net.runner.fitbit.splashScreen.splashScreen
import net.runner.fitbit.ui.theme.FitbitTheme
import net.runner.fitbit.userDetails.UserDetailComposable

val ONESIGNAL_APP_ID = BuildConfig.ONESIGNAL_APP_ID

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private var email =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        OneSignal.Debug.logLevel = LogLevel.VERBOSE

        OneSignal.initWithContext(this, ONESIGNAL_APP_ID)

        CoroutineScope(Dispatchers.IO).launch {
            OneSignal.Notifications.requestPermission(true)
        }


        val imageLoader = ImageLoader.Builder(this)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .build()

        Coil.setImageLoader(imageLoader)

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
                var startDestination by rememberSaveable { mutableStateOf("splashScreen") }
                if (auth.currentUser != null) {
                    checkIfAccountExists(auth.currentUser!!.email?:""){exists, accountType ->
                        if (exists) {
                            Log.d("gri",accountType.toString())
                            fcmTokenSave(true)

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
                        WorkoutBuddyDashBoard(navController)
                    }
                    composable(route = "dashBoardOrganizer") {
                        OrganizerDashBoard()

                    }
                    composable(route = "profileBuddy") {
                        ProfileBuddy(navController,this@MainActivity)
                    }
                    composable(route="editProfileScreen"){
                        EditProfileBuddy(navController)
                    }
                    composable(route = "chatBot") {
                        ChatbotPanel(navController)
                    }
                    composable(route="group/{groupId}",arguments = listOf(navArgument("groupId") { type = NavType.StringType })) {
                        backStackEntry ->
                        val groupId = backStackEntry.arguments?.getString("groupId")
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