package net.runner.fitbit.Chat

import com.google.firebase.firestore.FirebaseFirestore

fun getUserChat(uId: String,toUser:String, callback: (List<Map<String, Any>>) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val userDocRef = db.collection("UserChats")

    userDocRef.whereEqualTo("participants", listOf(uId, toUser))
        .addSnapshotListener { chatSnapshot, e ->
            if (e != null) {
                println("Error fetching chat: ${e.message}")
                return@addSnapshotListener
            }

            if (chatSnapshot != null && !chatSnapshot.isEmpty) {
                val chatDocument = chatSnapshot.documents.first()

                chatDocument.reference.collection("messages")
                    .orderBy("timestamp")
                    .addSnapshotListener { messagesSnapshot, messagesError ->
                        if (messagesError != null) {
                            println("Error fetching mess:${messagesError.message}")
                            callback(emptyList())
                            return@addSnapshotListener
                        }

                        if (messagesSnapshot != null && !messagesSnapshot.isEmpty) {
                            val messages = messagesSnapshot.documents.map { messageDoc ->
                                messageDoc.data ?: mapOf<String, Any>()
                            }
                            callback(messages)
                        } else {
                            println("No messages found.")
                            callback(emptyList())
                        }
                    }
            } else {
                println("Chat not found.")
                callback(emptyList())
            }
        }
}