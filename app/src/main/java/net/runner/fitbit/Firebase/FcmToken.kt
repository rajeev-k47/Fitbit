package net.runner.fitbit.Firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import net.runner.fitbit.auth.database.getfcmToken

fun fcmTokenSave(put:Boolean){
    val db = FirebaseFirestore.getInstance()
    val userUid = FirebaseAuth.getInstance().currentUser?.uid
    val userDocRef = db.collection("users").document(userUid!!)
    userDocRef.get()
        .addOnSuccessListener { documentSnapshot ->
            val user = documentSnapshot.data
            if(put){
                    getfcmToken{
                        userDocRef.update("fcmToken",it)
                    }
            }else{
                if(user?.get("fcmToken") !=null){
                    Log.d("Firestore","FCM Token already present")
//                    getfcmToken{
                        userDocRef.update("fcmToken", FieldValue.delete())
//                    }
                }
            }
        }
        .addOnFailureListener { exception ->
            Log.e("Firestore","Error getting profile",exception)
        }
}