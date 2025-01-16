package net.runner.fitbit.Profiles

import android.net.Uri
import android.util.Log
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import net.runner.fitbit.auth.database.profileImageSaving

fun editProfileSave(
    profileBuddy: Uri?,
    name: String,
    goalType: Set<String>,
    timeFrame: String,
    targetValue: String,
    selectedStartTime: String,
    selectedEndTime: String,
    connections: MutableMap<String, String>,
) {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        val userData = hashMapOf(
            "username" to name,
            "goalType" to goalType.toList(),
            "timeFrame" to timeFrame,
            "targetValue" to targetValue,
            "selectedStartTime" to selectedStartTime,
            "selectedEndTime" to selectedEndTime,
            "connections" to connections
        )

        auth.currentUser?.uid?.let { uid ->
            db.collection("users").document(uid)
                .update(userData)
                .addOnSuccessListener {
                    if(profileBuddy!=null){
                        profileImageSaving(imageUri = profileBuddy)
                    }

//                    navController.navigate("profileUpdatedScreen")
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error updating profile", e)
                }
        }

}
