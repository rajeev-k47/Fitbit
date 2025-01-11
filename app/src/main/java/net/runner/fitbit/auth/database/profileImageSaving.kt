package net.runner.fitbit.auth.database

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

fun profileImageSaving(imageUri: Uri) {
    uploadImageToFirebaseStorage(imageUri)
}
fun uploadImageToFirebaseStorage(imageUri: Uri) {
    val storageRef = FirebaseStorage.getInstance().reference
    val userUid = FirebaseAuth.getInstance().currentUser?.uid
    val imageRef = storageRef.child("user_images/${userUid}.jpg")

    imageRef.putFile(imageUri)
        .addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                Log.d("FirebaseStorage", "Image uploaded successfully: $uri")
                saveImageUrlToFirestore(uri.toString())
            }
        }
        .addOnFailureListener { exception ->
            Log.e("FirebaseStorage", "Error uploading image", exception)
        }
}

fun saveImageUrlToFirestore(imageUrl: String) {
    val db = FirebaseFirestore.getInstance()
    val userUid = FirebaseAuth.getInstance().currentUser?.uid

    if (userUid != null) {
        val userDocRef = db.collection("users").document(userUid)
        userDocRef.update("profileImageUrl", imageUrl)
            .addOnSuccessListener {
                Log.d("Firestore", "Image URL saved successfully")
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error saving image URL", exception)
            }
    }
}
