package net.runner.fitbit.OrganizerDashboard.OrgFragments.RequestsFragment

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import net.runner.fitbit.Database.GroupStatus
import net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.ActivityFragment.Notification
import net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.ActivityFragment.saveNotification
import java.text.SimpleDateFormat

fun ApproveRequest(userId:String){

    val db = FirebaseFirestore.getInstance()
    val groupId = FirebaseAuth.getInstance().currentUser?.uid
    val userDocRef = db.collection("users").document(groupId!!)

    userDocRef.get()
        .addOnSuccessListener { documentSnapshot ->
            val groupData = documentSnapshot.data ?: emptyMap()
            val pendingUsers = groupData["pending"] as? List<String> ?: emptyList()
            val users = groupData["users"] as? List<String> ?: emptyList()
            val updatedUsers = pendingUsers.filter { it != userId }
            userDocRef.update("pending", updatedUsers)
                .addOnSuccessListener {
                    println("success")
                    val sdf = SimpleDateFormat("dd MMM, hh:mm aa")
                    val currentDate = sdf.format(System.currentTimeMillis())
                    saveNotification(Notification("Request status","Group Request Accepted",currentDate,groupId,groupId),userId)
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    println("Error: ${e.message}")
                }
            updateUsers(userId, groupId)

        }
        .addOnFailureListener { exception ->
        }



}
fun updateUsers(userId: String, groupId: String) {
    val db = FirebaseFirestore.getInstance()
    val groupCollection = db.collection("users")
    val groupDocument = groupCollection.document(groupId)

    groupDocument.get().addOnSuccessListener { document ->
        if (document != null) {
            val users = document.get("users") as? List<String> ?: emptyList()
            val updatedUsers =
                if (users.contains(userId)) users
                else{
                    users + userId
                }
            groupDocument.update("users", updatedUsers)
                .addOnSuccessListener {
                    updateGroupStatusOnUser(userId, groupId, "Accepted")
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    println("Error: ${e.message}")
                }
        }
    }.addOnFailureListener { exception ->
    }
}
fun updateGroupStatusOnUser(userId: String, groupId: String, newStatus: String) {
    val firestore = FirebaseFirestore.getInstance()

    firestore.collection("users")
        .document(userId)
        .get()
        .addOnSuccessListener { document ->
            if (document.exists()) {
                val userData = document.data
                val groups = userData?.get("groups") as? List<Map<String, Any>> ?: emptyList()
                val updatedGroups = groups.map {
                    if (it["groupId"] == groupId) {
                        mapOf("groupId" to groupId, "status" to newStatus)
                    } else {
                        it
                    }
                }
                firestore.collection("users")
                    .document(userId)
                    .update("groups", updatedGroups)
                    .addOnSuccessListener {
                        println("Group status updated successfully")
                    }
                    .addOnFailureListener { e ->
                        e.printStackTrace()
                        println("Error updating group status: ${e.message}")
                    }

            }
        }
        .addOnFailureListener { e ->
            e.printStackTrace()
            println("Error fetching user data: ${e.message}")
        }
}
