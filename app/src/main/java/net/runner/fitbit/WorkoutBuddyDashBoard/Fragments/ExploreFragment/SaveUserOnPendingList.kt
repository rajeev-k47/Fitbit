package net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.ExploreFragment

import com.google.firebase.firestore.FirebaseFirestore

fun SaveUserOnPendingList(userId: String, groupId: String) {
    val db = FirebaseFirestore.getInstance()
    val groupCollection = db.collection("users")
    val groupDocument = groupCollection.document(groupId)

    groupDocument.get().addOnSuccessListener { document ->
        if (document != null) {
            val groupData = document.data
            val users = groupData?.get("pending") as? List<String> ?: emptyList()
            val updatedUsers = if (users.contains(userId)) users else users + userId
            groupDocument.update("pending", updatedUsers)
        } else {
            println("No such group")
        }
    }
    .addOnFailureListener { exception ->
    }
}