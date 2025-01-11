package net.runner.fitbit.auth.database

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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
        "accountType" to "Workoutbuddy"
    )

    db.collection("users")
        .document(auth.uid.toString())
        .set(userData)
        .addOnSuccessListener { documentReference ->
           onresult("success")
           profileImageSaving(imageUri = imageUri)

        }
        .addOnFailureListener { e ->
            onresult("failure")
        }
}
