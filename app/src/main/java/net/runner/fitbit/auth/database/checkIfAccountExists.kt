package net.runner.fitbit.auth.database

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

fun checkIfAccountExists(email: String, callback: (Boolean, String?) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val usersCollection = db.collection("users")
    usersCollection.whereEqualTo("email", email).get()
        .addOnSuccessListener { documents ->
            if (!documents.isEmpty) {
                val accountType = documents.documents[0].getString("accountType")
                callback(true, accountType)
            } else {
                callback(false, null)
            }
        }
        .addOnFailureListener { exception ->
            Log.e("Firestore", "Error checking account: ${exception.message}")
            callback(false, null)
        }
}
