package net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.ExploreFragment

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

fun saveBannerImage(bannerImage: Uri) {
    val storageRef = FirebaseStorage.getInstance().reference
    val userUid = FirebaseAuth.getInstance().currentUser?.uid
    val imageRef = storageRef.child("banner_images/${userUid}.jpg")

    imageRef.putFile(bannerImage)
        .addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                Log.d("FirebaseStorage", "Image uploaded successfully: $uri")
                val db = FirebaseFirestore.getInstance()
                val userUid = FirebaseAuth.getInstance().currentUser?.uid

                if (userUid != null) {
                    val userDocRef = db.collection("users").document(userUid)
                    userDocRef.update("banner", uri.toString())
                        .addOnSuccessListener {
                            Log.d("Firestore", "Image URL saved successfully")
                        }
                        .addOnFailureListener { exception ->
                            Log.e("Firestore", "Error saving image URL", exception)
                        }
                }
            }
        }
        .addOnFailureListener { exception ->
            Log.e("FirebaseStorage", "Error uploading image", exception)
        }
}