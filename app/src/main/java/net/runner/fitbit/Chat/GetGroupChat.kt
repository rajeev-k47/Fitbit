package net.runner.fitbit.Chat

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


fun getGroupChat(uId: String, onResult: (List<Map<String, Any>>) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val groupCollection = db.collection("Chats")
    val groupDocumentCollection = groupCollection.document(uId).collection("messages")
        .orderBy("timestamp", Query.Direction.ASCENDING)

    groupDocumentCollection.addSnapshotListener { snapshot, e ->
        val groupData = mutableListOf<Map<String, Any>>()
        if (e != null) {
            println("Error fetching messages: ${e.message}")
            onResult(groupData)
            return@addSnapshotListener
        }

        if (snapshot != null && !snapshot.isEmpty) {
            val messages = snapshot.documents.map { document ->
                document.data ?: mapOf<String, Any>()
            }
            groupData.addAll(messages)
        } else {
            println("No messages found.")
        }

        onResult(groupData)
    }
}