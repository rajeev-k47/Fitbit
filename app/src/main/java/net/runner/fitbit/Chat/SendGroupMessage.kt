package net.runner.fitbit.Chat

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import net.runner.fitbit.ServerSide.SendNotificationGroup

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
            println("Message  ${documentReference.id}")
        }
        .addOnFailureListener { e ->
            println("Error message: $e")
        }
}

