package net.runner.fitbit.auth.authFunctions

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.actionCodeSettings
import com.google.firebase.auth.auth

fun EmailLinkAuth(emailText:String){
    val actionCodeSettings = actionCodeSettings {
        url = "https://fitbit-6d7b2.web.app"
        handleCodeInApp = true
        setIOSBundleId("com.example.ios")
        setAndroidPackageName(
            "net.runner.fitbit",
            false, // installIfNotAvailable
            "12", // minimumVersion
        )
    }
    Firebase.auth.sendSignInLinkToEmail(emailText, actionCodeSettings)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("TAG", "Email sent.")
            }
        }
}