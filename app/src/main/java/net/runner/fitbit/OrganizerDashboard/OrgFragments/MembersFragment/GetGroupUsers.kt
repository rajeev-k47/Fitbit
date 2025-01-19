package net.runner.fitbit.OrganizerDashboard.OrgFragments.MembersFragment

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

fun getGroupUsers(callback: (List<String>) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val usersCollection = db.collection("users")

    val groupId = FirebaseAuth.getInstance().currentUser?.uid
    if (groupId != null) {
        usersCollection.document(groupId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val groupData = documentSnapshot.data
                val groups = groupData?.get("users") as? List<String> ?: emptyList()
                callback(groups)
            }
            .addOnFailureListener { exception ->
            }
    }
}
fun getGroupUsersData(callback: (List<Map<String, Any>>) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val usersCollection = db.collection("users")

    val groupId = FirebaseAuth.getInstance().currentUser?.uid
    if (groupId != null) {
        usersCollection.document(groupId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val groupData = documentSnapshot.data
                val groups = groupData?.get("users") as? List<Map<String, Any>> ?: emptyList()
                callback(groups)
            }
            .addOnFailureListener { exception ->
            }
    }
}