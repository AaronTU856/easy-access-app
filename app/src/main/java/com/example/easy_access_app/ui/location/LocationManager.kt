package com.example.easy_access_app.ui.location

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import android.widget.Toast
import com.google.android.gms.location.*

class LocationManager(private val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private var locationCallback: LocationCallback? = null

    /**
     * Starts location updates with a specified interval.
     *
     * @param interval The interval in milliseconds for location updates.
     * @param onLocationUpdate Callback invoked with each updated location.
     */
    fun startLocationUpdates(
        interval: Long,
        onLocationUpdate: (android.location.Location) -> Unit
    ) {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            interval
        ).build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    onLocationUpdate(location) // Pass each location to the callback
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback!!,
            context.mainLooper
        )
    }

    /**
     * Stops ongoing location updates.
     */
    fun stopLocationUpdates() {
        locationCallback?.let {
            fusedLocationClient.removeLocationUpdates(it)
        }
        locationCallback = null // Clear the callback
    }

    /**
     * Checks if GPS is enabled.
     *
     * @return True if GPS is enabled, false otherwise.
     */
    fun isGPSEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /**
     * Prompts the user to enable GPS.
     */
    fun promptEnableGPS() {
        Toast.makeText(context, "Please enable GPS", Toast.LENGTH_SHORT).show()
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        context.startActivity(intent)
    }
}
