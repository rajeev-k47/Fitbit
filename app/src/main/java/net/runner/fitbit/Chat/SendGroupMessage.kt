package net.runner.fitbit.Chat

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import net.runner.fitbit.ServerSide.SendNotificationGroup
import net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.ActivityFragment.Notification
import net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.ActivityFragment.saveNotification
import java.text.SimpleDateFormat

fun sendGroupMessage(groupUid: String, content: String) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val db = FirebaseFirestore.getInstance()


    val message = Message(
        timestamp = System.currentTimeMillis(),
        userId = userId,
        content = content
    )

    db.collection("Chats")
        .document(groupUid)
        .collection("messages")
        .add(message)
        .addOnSuccessListener { documentReference ->
            SendNotificationGroup(groupUid,userId,content){

        }
            val ndb = FirebaseFirestore.getInstance()
            ndb.collection("users")
                .document(groupUid)
                .get()
                .addOnSuccessListener { document ->
                    val sdf = SimpleDateFormat("dd MMM, hh:mm aa")
                    val currentDate = sdf.format(System.currentTimeMillis())
                    val users = document.data?.get("users") as? List<String> ?: emptyList()
                    users.forEach { user ->
                        if(user != userId){
                            saveNotification(Notification("New Message in Group",content,currentDate,userId,groupUid),user)

                        }
                    }
                }
        }
}

