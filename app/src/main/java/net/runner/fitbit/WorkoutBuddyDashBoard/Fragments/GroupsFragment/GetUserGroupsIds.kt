package net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.GroupsFragment

import com.google.firebase.firestore.FirebaseFirestore

fun GetUserGroups(userId: String,onresult:(List<Map<String, Any>>)->Unit) {
    val db = FirebaseFirestore.getInstance()
    val groupCollection = db.collection("users")
    val groupDocument = groupCollection.document(userId)
    groupDocument.get().addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val groupData = task.result.get("groups") as? List<Map<String, Any>> ?: emptyList()
            onresult(groupData)
        }
    }
}