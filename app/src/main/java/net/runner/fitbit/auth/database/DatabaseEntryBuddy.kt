package net.runner.fitbit.auth.database

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import net.runner.fitbit.auth.UserLocation.saveUserLocation
import java.util.Calendar

fun DatabaseEntryBuddy(
    goalType: Set<String>,
    selectedStartTime: String,
    selectedEndTime: String,
    username: String,
    imageUri: Uri,
    gender: String,
    timeFrame: String,
    targetValue: String,
    context: Context,
    onresult:(String)-> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    val userData = hashMapOf(
        "goalType" to goalType.toList(),
        "selectedStartTime" to selectedStartTime,
        "selectedEndTime" to selectedEndTime,
        "username" to username,
        "gender" to gender,
        "timeFrame" to timeFrame,
        "targetValue" to targetValue,
        "accountType" to "Workoutbuddy",
        "joiningDate" to Calendar.getInstance().time.toString(),
        "email" to auth.currentUser?.email.toString()
    )

    db.collection("users")
        .document(auth.uid.toString())
        .set(userData)
        .addOnSuccessListener { documentReference ->
           onresult("success")
            Log.d("gri",imageUri.toString())
            profileImageSaving(imageUri = imageUri)
            saveUserLocation(context)


        }
        .addOnFailureListener { e ->
            onresult("failure")
        }
}
