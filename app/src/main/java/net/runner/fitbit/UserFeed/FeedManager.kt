package net.runner.fitbit.UserFeed

import android.os.Parcelable
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat

//@Parcelize
//data class FeedElement(
//    val postId: String,
//    val author: String,
//    val title: String,
//    val description: String,
//    val dateTime: String,
//    val tags : List<String>,
//    val bannerImageUri : String,
//    val likes : List<String>,
//    val referenceLink : String,
//    val readers : List<String>
//):Parcelable

fun getPosts(callback: (List<Map<String, Any>>) -> Unit){
    val db = FirebaseFirestore.getInstance()
    val postsCollection = db.collection("posts")
    postsCollection.get()
        .addOnSuccessListener { documents ->
            val postsList = mutableListOf<Map<String, Any>>()
            for (document in documents) {
                postsList.add(document.data)
            }
            callback(postsList)
        }
        .addOnFailureListener { exception ->
            //error
        }
}

fun getFormatedTimeFeed(dateTime: String): String {
    val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
    val date = sdf.parse(dateTime)
    val sdf2 = SimpleDateFormat("dd MMM yyyy, hh:mm a")
    return sdf2.format(date)
}