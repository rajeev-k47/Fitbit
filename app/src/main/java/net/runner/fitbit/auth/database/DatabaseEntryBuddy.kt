package net.runner.fitbit.auth.database

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

fun DatabaseEntryBuddy(
    goalType: Set<String>,
    selectedStartTime: String,
    selectedEndTime: String,
    username: String,
    email: String,
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
        "email" to email,
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
            val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("user_exists_${auth.uid}", "buddy")
            editor.apply()

        }
        .addOnFailureListener { e ->
            onresult("failure")
        }
}
