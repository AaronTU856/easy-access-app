package com.example.easy_access_app.ui.location

import android.content.Context
import android.location.Location
import android.location.Geocoder
import java.util.Locale

object LocationUtils {

    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        return results[0] // Distance in meters
    }

    fun formatCoordinates(lat: Double, lon: Double): String {
        return "Latitude: %.5f, Longitude: %.5f".format(lat, lon)
    }

    fun getAddressFromCoordinates(context: Context, lat: Double, lon: Double): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        return try {
            // Attempt to retrieve address from coordinates
            val addresses = geocoder.getFromLocation(lat, lon, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                "${address.locality}, ${address.countryName}"
            } else {
                "Unknown location"
            }
        } catch (e: Exception) {
            "Error retrieving location"
        }
    }
}
