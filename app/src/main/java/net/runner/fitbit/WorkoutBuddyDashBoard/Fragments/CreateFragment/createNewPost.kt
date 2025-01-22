package net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.CreateFragment

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.ExploreFragment.savePostBannerImage
import java.util.Calendar

fun createNewPost(
    author: String,
    PostTitle: String,
    PostDescription: String,
    tags: List<String>,
    bannerImageUri: Uri,
    referenceLink: String
){

    val db = FirebaseFirestore.getInstance()
    val postCollection = db.collection("posts")
    val postId = postCollection.document().id
    val newPost = hashMapOf(
        "postId" to postId,
        "author" to author,
        "title" to PostTitle,
        "dateTime" to Calendar.getInstance().time.toString(),
        "description" to PostDescription,
        "tags" to tags,
        "referenceLink" to referenceLink,
        "likes" to emptyList<String>(),
        "readers" to emptyList<String>()
    )

    postCollection.document(postId).set(newPost)
        .addOnSuccessListener {
            savePostBannerImage(bannerImageUri, postId)
        }
        .addOnFailureListener { exception ->
            Log.w("error", "Error adding document", exception)
        }

}