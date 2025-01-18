package net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.GroupsFragment

import com.google.firebase.firestore.FirebaseFirestore

fun GetGroupData(groupId:String,onresult:( Map<String, Any>)->Unit) {
    val db = FirebaseFirestore.getInstance()
    val groupCollection = db.collection("users")
    val groupDocument = groupCollection.document(groupId)
    groupDocument.get().addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val groupData = task.result.data
            if(groupData!=null){

                onresult(groupData)
            }
        }
    }
}