package net.runner.fitbit.auth.authFunctions

import android.app.Activity
import android.content.IntentSender
import android.util.Log
import androidx.core.app.ActivityCompat.startIntentSenderForResult
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import net.runner.fitbit.BuildConfig


fun gOauthClient(activity: Activity) {
    val oneTapClient = Identity.getSignInClient(activity)
    val REQ_ONE_TAP = 1001

    val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(BuildConfig.G_OAUTH_WEB_SERVER_CLIENT_ID)
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .build()

    oneTapClient.beginSignIn(signInRequest)
        .addOnSuccessListener(activity) { result ->
            try {
                startIntentSenderForResult(
                    activity,
                    result.pendingIntent.intentSender,
                    REQ_ONE_TAP,
                    null, 0, 0, 0, null
                )
            } catch (e: IntentSender.SendIntentException) {
                Log.e("SignIn","Error starting intent sender",e)
            }
        }
        .addOnFailureListener(activity) { e ->
            Log.e("SignIn","Google Sign-In failed",e)
        }
}
