package net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.ActivityFragment

import android.os.Parcelable
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.parcelize.Parcelize

@Parcelize
data class Notification(
    val title: String="",
    val description: String="",
    val date: String="",
    val sender: String="",
    val redirection:String="",
    ): Parcelable

fun saveNotification(notification: Notification, userUid: String) {
    val db = FirebaseFirestore.getInstance()
    val notificationCollection = db.collection("users").document(userUid).collection("notifications")
    val notificationDocument = notificationCollection.document()
    notificationDocument.set(notification)
        .addOnSuccessListener {
            Log.d("NotificationHandler","Notification added successfully")
        }
        .addOnFailureListener { exception ->
            Log.e("NotificationHandler","Error adding notification", exception)
        }
}

fun fetchNotifications(onResult: (List<Notification>) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val userUid = FirebaseAuth.getInstance().currentUser?.uid
    val notificationCollection = db.collection("users").document(userUid!!).collection("notifications")
    notificationCollection.get()
        .addOnSuccessListener { querySnapshot ->
            val notifications = mutableListOf<Notification>()
            for (document in querySnapshot.documents) {
                val notification = document.toObject(Notification::class.java)
                if (notification != null) {
                    notifications.add(notification)
                }
            }
            onResult(notifications)
        }
        .addOnFailureListener { exception ->
            Log.e("NotificationHandler", "Error fetching notifications", exception)
        }
}