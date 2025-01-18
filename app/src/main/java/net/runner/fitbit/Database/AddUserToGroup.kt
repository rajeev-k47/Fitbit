package net.runner.fitbit.Database

import com.google.firebase.firestore.FirebaseFirestore

fun addUserToGroup(userId: String, groupId: String) {
    val db = FirebaseFirestore.getInstance()
    val groupCollection = db.collection("users")
    val groupDocument = groupCollection.document(groupId)

    groupDocument.get().addOnSuccessListener { document ->
        if (document != null) {
            val groupData = document.data
            val users = groupData?.get("users") as? List<String> ?: emptyList()
            val updatedUsers = if (users.contains(userId)) users else users + userId
            groupDocument.update("users", updatedUsers)
        } else {
            println("No such group")
        }
    }
    .addOnFailureListener { exception ->
    }
}