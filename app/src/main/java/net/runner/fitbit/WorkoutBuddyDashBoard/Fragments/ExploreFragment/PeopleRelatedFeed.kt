package net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.ExploreFragment

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


fun PeopleRelatedFeed(
    currentUserGoals: List<String>,
    onResult: (List<Pair<Map<String, Any>, Int>>) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    val usersCollection = db.collection("users")

    usersCollection.get()
        .addOnSuccessListener { querySnapshot ->
            val similarUsers = mutableListOf<Pair<Map<String, Any>, Int>>()

            for (document in querySnapshot.documents) {
                val userData = document.data
                if(userData?.get("email")== FirebaseAuth.getInstance().currentUser?.email || userData?.get("accountType")!="Workoutbuddy")continue
                val userGoals = userData?.get("goalType") as? List<String>?:continue
                val matchingGoals = userGoals.intersect(currentUserGoals.toSet())
                val similarityScore = matchingGoals.size

                if (similarityScore > 0) {
                    similarUsers.add(userData to similarityScore)
                }
            }
            val sortedSimilarUsers = similarUsers.sortedByDescending { it.second }
            onResult(sortedSimilarUsers)
        }
        .addOnFailureListener { exception ->
            Log.e("Firestore", "Error fetching users: ${exception.message}")
        }
}
