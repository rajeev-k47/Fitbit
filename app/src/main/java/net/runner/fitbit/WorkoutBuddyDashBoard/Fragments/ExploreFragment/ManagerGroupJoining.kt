package net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.ExploreFragment

import com.google.firebase.firestore.FirebaseFirestore
import net.runner.fitbit.Database.GroupStatus
import net.runner.fitbit.Database.SaveAndUpdateBuddyGroups
import net.runner.fitbit.ServerSide.makeGroupJoinRequest

fun ManagerGroupJoining(userId: String,groupId: String,onresult:()->Unit) {
    val db = FirebaseFirestore.getInstance()
    val groupCollection = db.collection("users")
    val groupDocument = groupCollection.document(groupId)

    groupDocument.get().addOnSuccessListener { document ->
        if (document != null) {
            val groupData = document.data
            val isPrivate = groupData?.get("private") as? Boolean ?: false

            if (isPrivate) {
                makeGroupJoinRequest(userId, groupId)
            }else{
                SaveAndUpdateBuddyGroups(userId, GroupStatus(groupId, "Accepted"))
                onresult()
            }
        } else {
            println("No such group")
        }
    }
    .addOnFailureListener { exception ->
    }
}