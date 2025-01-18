package net.runner.fitbit.auth.database

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging


fun getfcmToken(onresult:(String)->Unit){

    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
        if (!task.isSuccessful) {
            Log.w("FCM", "Fetching FCM registration token failed", task.exception)
            return@addOnCompleteListener
        }
        val token = task.result
        onresult(token)
    }
}
