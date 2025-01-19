package net.runner.fitbit.OrganizerDashboard.OrgFragments.RequestsFragment

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

fun getPendingRequests(callback: (List<String>) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val requestsCollection = db.collection("users")

    val userId = FirebaseAuth.getInstance().currentUser?.uid
    if (userId != null) {
        requestsCollection.document(userId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val requests = documentSnapshot.get("pending") as? List<String> ?: emptyList()
                callback(requests)
            }
            .addOnFailureListener { exception ->
            }
    }
}

fun getPendingUserData(userId: String, callback: (Map<String, Any>) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val userDocRef = db.collection("users").document(userId)

    userDocRef.get()
        .addOnSuccessListener { documentSnapshot ->
            val userData = documentSnapshot.data ?: emptyMap()
            callback(userData)
        }
        .addOnFailureListener { exception ->
        }
}