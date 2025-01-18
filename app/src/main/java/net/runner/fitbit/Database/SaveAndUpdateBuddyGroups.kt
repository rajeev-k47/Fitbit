package net.runner.fitbit.Database

import androidx.compose.runtime.Composable
import com.google.firebase.firestore.FirebaseFirestore

data class GroupStatus(
    val groupId: String,
    val status: Any
)
fun SaveAndUpdateBuddyGroups(userId: String, group:GroupStatus) {

        val firestore = FirebaseFirestore.getInstance()

    val userDocument = firestore.collection("users").document(userId)

    userDocument.get()
        .addOnSuccessListener { document ->
            if (document.exists()) {
                val userData = document.data
                println( userData?.get("groups"))
                val groups = (userData?.get("groups") as? List<Map<String, Any>>)?.map {
                    GroupStatus(it["groupId"] as String, it["status"] as Any)
                }?.toMutableList() ?: mutableListOf()
                if (!groups.any { it.groupId == group.groupId }) {
                    groups.add(group)
                }

                userDocument.update("groups", groups)
                    .addOnSuccessListener {
                        println("Group added successfully")
                    }
                    .addOnFailureListener { e ->
                        e.printStackTrace()
                        println("Error updating groups: ${e.message}")
                    }
            } else {
                println("User document does not exist")
            }
        }
        .addOnFailureListener { e ->
            e.printStackTrace()
            println("Error fetching user data: ${e.message}")
        }
}
fun updateGroupStatus(userId: String, groupId: String, newStatus: String) {
    val firestore = FirebaseFirestore.getInstance()

    firestore.collection("users")
        .document(userId)
        .get()
        .addOnSuccessListener { document ->
            if (document.exists()) {
                val userData = document.data
                val groups = userData?.get("groups") as? List<GroupStatus> ?: emptyList()
                val updatedGroups = groups.map { if (it.groupId == groupId) it.copy(status = newStatus) else it }

//                saveGroupStatusToFirebase(userId, updatedGroups)
            }
        }
        .addOnFailureListener { e ->
            e.printStackTrace()
            println("Error fetching user data: ${e.message}")
        }
}
