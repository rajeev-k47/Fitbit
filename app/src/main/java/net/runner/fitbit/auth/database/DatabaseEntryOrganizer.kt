package net.runner.fitbit.auth.database

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.ExploreFragment.saveBannerImage
import net.runner.fitbit.auth.UserLocation.saveUserLocation
import net.runner.fitbit.auth.extendedComposables.organizer.OrganizerGroupData
import java.util.Calendar

fun DataBaseEntryOrganizer(
    groupData: OrganizerGroupData,
    bannerUri:Uri,
    facilities:Set<String>,
    orgStartTime:String,
    orgEndTime: String,
    orgPrivacy: Boolean,
    imageUri:Uri,
    context: Context,
    onresult: (String) -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    val userData = hashMapOf(
        "email" to auth.currentUser?.email.toString()
        ,"facilities" to facilities.toList()
        ,"groupData" to groupData
        ,"orgStartTime" to orgStartTime
        ,"orgEndTime" to orgEndTime
        ,"accountType" to "Organizer"
        ,"joiningDate" to Calendar.getInstance().time.toString(),
        "email" to auth.currentUser?.email.toString(),
        "groupId" to auth.uid.toString(),
        "private" to orgPrivacy

    )

    db.collection("users")
        .document(auth.uid.toString())
        .set(userData)
        .addOnSuccessListener { documentReference ->
            onresult("success")
            profileImageSaving(imageUri)
            saveBannerImage(bannerUri)
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
            }
            saveUserLocation(context)


//            val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
//            val editor = sharedPreferences.edit()
//            editor.putString("user_exists_${auth.uid}", "org")
//            editor.apply()
        }
        .addOnFailureListener { e ->
            Log.w("TAG", "Error adding document", e)
        }
}