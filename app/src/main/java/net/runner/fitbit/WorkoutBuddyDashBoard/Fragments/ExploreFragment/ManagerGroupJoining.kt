package net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.ExploreFragment

import com.google.firebase.firestore.FirebaseFirestore
import net.runner.fitbit.Database.GroupStatus
import net.runner.fitbit.Database.SaveAndUpdateBuddyGroups
import net.runner.fitbit.Database.addUserToGroup
import net.runner.fitbit.ServerSide.makeGroupJoinRequest

fun ManagerGroupJoining(userId: String,groupId: String,onresult:(Boolean)->Unit) {
    val db = FirebaseFirestore.getInstance()
    val groupCollection = db.collection("users")
    val groupDocument = groupCollection.document(groupId)

    groupDocument.get().addOnSuccessListener { document ->
        if (document != null) {
            val groupData = document.data
            val isPrivate = groupData?.get("private") as? Boolean ?: false

            if (isPrivate) {
                makeGroupJoinRequest(userId, groupId)
                SaveAndUpdateBuddyGroups(userId, GroupStatus(groupId, "Pending"))//for user database entry that it is pending or not and to make easily accessible to show in group fragments
                onresult(true)
            }else{
                SaveAndUpdateBuddyGroups(userId, GroupStatus(groupId, "Accepted"))
                addUserToGroup(userId, groupId)//for organizer to control the users of the group
                onresult(false)
            }
        } else {
            println("No such group")
        }
    }
    .addOnFailureListener { exception ->
    }
}