package net.runner.fitbit.Chat

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

fun SendUserMessage( uId: String, toUser: String, content: String) {
    val db = FirebaseFirestore.getInstance()
    val userDocRef = db.collection("UserChats")

    val messageData = mapOf(
        "userId" to FirebaseAuth.getInstance().currentUser?.uid,
        "content" to content,
        "timestamp" to System.currentTimeMillis()
    )

    userDocRef.whereEqualTo("participants", listOf(uId, toUser))
        .get()
        .addOnSuccessListener { querySnapshot ->
            if (!querySnapshot.isEmpty) {
                val chatDocument = querySnapshot.documents.first()
                chatDocument.reference.collection("messages")
                    .add(messageData)
                    .addOnSuccessListener {
                    }
                    .addOnFailureListener { ex ->
                    }
            } else {
                val newChatData = mapOf(
                    "participants" to listOf(uId, toUser),
                    "createdAt" to System.currentTimeMillis()
                )
                userDocRef.add(newChatData)
                    .addOnSuccessListener { newChatDoc ->
                        newChatDoc.collection("messages")
                            .add(messageData)
                            .addOnSuccessListener {
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