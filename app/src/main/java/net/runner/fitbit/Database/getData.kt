package net.runner.fitbit.Database

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

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