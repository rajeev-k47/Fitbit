package net.runner.fitbit.auth.UserLocation

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

fun saveUserLocation(context: Context) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
        return
    }
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        if (location != null) {
            val userLocation = LatLng(location.latitude, location.longitude)
            val db = FirebaseFirestore.getInstance()
            val userUid = FirebaseAuth.getInstance().currentUser?.uid

            if (userUid != null) {
                val userDocRef = db.collection("users").document(userUid)
                userDocRef.update("userLocation", userLocation)
                    .addOnSuccessListener {
                        Log.d("Firestore", "Location saved successfully")
                    }
                    .addOnFailureListener { exception ->
                        Log.e("Firestore", "Error saving Location", exception)
                    }
            }else{
                Log.d("Location","User UID is null")
            }
        }
    }.addOnFailureListener {
        Log.e("Location","Failed to get location", it)
    }
}
