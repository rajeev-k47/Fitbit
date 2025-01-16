package net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.ExploreFragment

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

fun GroupsRelatedFeed(
    currentUserGoals: List<String>,
    onResult: (List<Pair<Map<String, Any>, Int>>) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    val usersCollection = db.collection("users")

    usersCollection.get()
        .addOnSuccessListener { querySnapshot ->
            val similarUsers = mutableListOf<Pair<Map<String, Any>, Int>>()

            for (document in querySnapshot.documents) {
                val groupData = document.data
                if(groupData?.get("email")== FirebaseAuth.getInstance().currentUser?.email || groupData?.get("accountType")!="Organizer")continue
                val groupGoals = groupData["facilities"] as? List<String>?:continue
                val matchingGoals = groupGoals.intersect(currentUserGoals.toSet())
                val similarityScore = matchingGoals.size

                if (similarityScore > 0) {
                    similarUsers.add(groupData to similarityScore)
                }
            }
            val sortedSimilarUsers = similarUsers.sortedByDescending { it.second }
            onResult(sortedSimilarUsers)
        }
        .addOnFailureListener { exception ->
            Log.e("Firestore", "Error fetching groups: ${exception.message}")
        }
}
