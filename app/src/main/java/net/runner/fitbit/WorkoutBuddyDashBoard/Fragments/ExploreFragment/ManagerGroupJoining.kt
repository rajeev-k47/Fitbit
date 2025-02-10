package net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.ExploreFragment

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import net.runner.fitbit.Database.GroupStatus
import net.runner.fitbit.Database.SaveAndUpdateBuddyGroups
import net.runner.fitbit.Database.addUserToGroup
import net.runner.fitbit.ServerSide.makeGroupJoinRequest
import net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.ActivityFragment.Notification
import net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.ActivityFragment.saveNotification

fun ManagerGroupJoining(context: Context, userId: String, groupId: String, onresult:(Boolean)->Unit) {
    val db = FirebaseFirestore.getInstance()
    val groupCollection = db.collection("users")
    val groupDocument = groupCollection.document(groupId)

    groupDocument.get().addOnSuccessListener { document ->
        if (document != null) {
            val groupData = document.data
            val isPrivate = groupData?.get("private") as? Boolean ?: false
            val sdf= SimpleDateFormat("dd MMM, hh:mm aa")
            val date=sdf.format(System.currentTimeMillis())
            if (isPrivate) {
                makeGroupJoinRequest(userId, groupId){
//                    Toast.makeText(context,it, Toast.LENGTH_SHORT).show()
                }
                SaveUserOnPendingList(userId, groupId)
                SaveAndUpdateBuddyGroups(userId, GroupStatus(groupId, "Pending"))//for user database entry that it is pending or not and to make easily accessible to show in group fragments

                saveNotification(Notification("New Joining Request", userId, date, userId,"Pending"), groupId)
                onresult(true)

            }else{
                SaveAndUpdateBuddyGroups(userId, GroupStatus(groupId, "Accepted"))
                addUserToGroup(userId, groupId)//for organizer to control the users of the group
                saveNotification(Notification("New Joining", userId, date, userId,""), groupId)
                onresult(false)
            }
        } else {
            println("No such group")
        }
    }
    .addOnFailureListener { exception ->
    }
}