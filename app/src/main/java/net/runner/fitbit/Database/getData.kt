package net.runner.fitbit.Database

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

fun getUserData(onresult:(MutableMap<String,Any>)->Unit) {
    val db = FirebaseFirestore.getInstance()
    val userUid = FirebaseAuth.getInstance().currentUser?.uid

    if (userUid != null) {
        val userDocRef = db.collection("users").document(userUid)
        userDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                val user = documentSnapshot.data
                onresult(user as MutableMap<String, Any>)
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore","Error getting profile",exception)
            }
    }

}
suspend fun getUserDataSuspendable(): MutableMap<String, Any>? {
    val db = FirebaseFirestore.getInstance()
    val userUid = FirebaseAuth.getInstance().currentUser?.uid

    return if (userUid != null) {
        try {
            val userDocRef = db.collection("users").document(userUid)
            val documentSnapshot = userDocRef.get().await()
            documentSnapshot.data as? MutableMap<String, Any>
        } catch (exception: Exception) {
            Log.e("Firestore","Error getting profile",exception)
            null
        }
    } else {
        null
    }
}