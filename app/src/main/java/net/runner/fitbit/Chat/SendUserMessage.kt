package net.runner.fitbit.Chat

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import net.runner.fitbit.ServerSide.SendNotificationUser
import net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.ActivityFragment.Notification
import net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.ActivityFragment.saveNotification
import java.text.SimpleDateFormat

fun SendUserMessage( uId: String, toUser: String, content: String) {
    val db = FirebaseFirestore.getInstance()
    val userDocRef = db.collection("UserChats")

    val messageData = mapOf(
        "userId" to FirebaseAuth.getInstance().currentUser?.uid,
        "content" to content,
        "timestamp" to System.currentTimeMillis()
    )

    userDocRef.whereEqualTo("participants", listOf(uId, toUser).sorted())
        .get()
        .addOnSuccessListener { querySnapshot ->
            if (!querySnapshot.isEmpty) {
                val chatDocument = querySnapshot.documents.first()
                chatDocument.reference.collection("messages")
                    .add(messageData)
                    .addOnSuccessListener {
                        SendNotificationUser(uId,toUser,content){
                            val sdf = SimpleDateFormat("dd MMM, hh:mm aa")
                            val currentDate = sdf.format(System.currentTimeMillis())
                            saveNotification(Notification("New Message",content,currentDate,uId,""),toUser)
                        }
                    }
                    .addOnFailureListener { ex ->
                    }
            } else {
                val newChatData = mapOf(
                    "participants" to listOf(uId, toUser).sorted(),
                    "createdAt" to System.currentTimeMillis()
                )
                userDocRef.add(newChatData)
                    .addOnSuccessListener { newChatDoc ->
                        newChatDoc.collection("messages")
                            .add(messageData)
                            .addOnSuccessListener {
                                SendNotificationUser(uId,toUser,content){
                                    val sdf = SimpleDateFormat("dd MMM, hh:mm aa")
                                    val currentDate = sdf.format(System.currentTimeMillis())
                                    saveNotification(Notification("New Message",content,currentDate,uId,""),toUser)
                                }
                            }
                            .addOnFailureListener { ex ->
                            }
                    }
                    .addOnFailureListener { ex ->
                    }
            }
        }
        .addOnFailureListener { e ->
        }

}