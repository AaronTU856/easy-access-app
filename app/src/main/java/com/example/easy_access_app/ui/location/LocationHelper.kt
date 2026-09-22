package com.example.easy_access_app.ui.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationHelper(private val context: Context) {

    // FusedLocationProviderClient for accessing location services
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    /**
     * Requests location permissions using a provided ActivityResultLauncher.
     *
     * @param locationPermissionLauncher A launcher to request permissions.
     */
    fun requestLocationPermissions(locationPermissionLauncher: ActivityResultLauncher<Array<String>>) {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    /**
     * Fetches the last known location.
     * Ensure that location permissions are granted before calling this method.
     *
     * @param onResult Callback invoked with the retrieved Location or null if failed.
     */
    @SuppressLint("MissingPermission")
    fun fetchLastKnownLocation(onResult: (Location?) -> Unit) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                onResult(location) // Pass the retrieved location to the callback
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to get location", Toast.LENGTH_SHORT).show()
                onResult(null) // Return null on failure
            }
    }
}
