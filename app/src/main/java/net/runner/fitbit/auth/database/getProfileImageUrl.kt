package net.runner.fitbit.auth.database

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

fun getProfileImageUrl(callback:(String?)->Unit){
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val userDocRef = db.collection("users").document(auth.uid.toString())

    userDocRef.get()
        .addOnSuccessListener{documentSnapshot ->
            val profileImageUrl = documentSnapshot.getString("profileImageUrl")
            Log.d("Firestore","Profile: $profileImageUrl")
            callback(profileImageUrl)
        }.addOnFailureListener { exception ->
            Log.e("Firestore","Error getting profile", exception)
            callback(null)
        }
}
