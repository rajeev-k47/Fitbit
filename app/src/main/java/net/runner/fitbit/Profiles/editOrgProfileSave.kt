package net.runner.fitbit.Profiles

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.ExploreFragment.saveBannerImage
import net.runner.fitbit.auth.database.profileImageSaving
import net.runner.fitbit.auth.extendedComposables.organizer.OrganizerGroupData

fun editOrgProfileSave(profileImageUri: Uri, bannerImageUri: Uri?, facilities: Set<String>, selectedStartTime: String, selectedEndTime: String, newGroupData: OrganizerGroupData, orgPrivacy: Boolean) {

            val db = FirebaseFirestore.getInstance()
            val auth = FirebaseAuth.getInstance()

            val userData = hashMapOf(
                "facilities" to facilities.toList(),
                "groupData" to newGroupData,
                "orgStartTime" to selectedStartTime,
                "orgEndTime" to selectedEndTime,
                "private" to orgPrivacy
            )

            auth.currentUser?.uid?.let { uid ->
                db.collection("users").document(uid)
                    .update(userData)
                    .addOnSuccessListener {
                        if(profileImageUri!=Uri.EMPTY){
                            profileImageSaving(imageUri = profileImageUri)
                        }
                        if(bannerImageUri!=null){
                            saveBannerImage(bannerImageUri)
                        }
                    }
            }

}