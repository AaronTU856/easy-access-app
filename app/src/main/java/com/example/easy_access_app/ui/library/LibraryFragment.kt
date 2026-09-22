package com.example.easy_access_app.ui.library

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.activity.result.contract.ActivityResultContracts
import com.example.easy_access_app.R
import com.example.easy_access_app.ui.location.LocationHelper
import com.example.easy_access_app.ui.location.LocationUtils
import android.net.Uri
import android.widget.CalendarView
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

class LibraryFragment : Fragment(R.layout.fragment_library) {

    private var libraryWebsiteButton: Button? = null
    private lateinit var showLibraryButton: Button
    private lateinit var libraryLocationTextView: TextView
    private lateinit var locationHelper: LocationHelper
    private lateinit var libraryCalendar: CalendarView
    private lateinit var openingHoursTextView: TextView

    // Permission launcher for location access
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            fetchAndDisplayLibraryLocation()
            Toast.makeText(requireContext(), "Location permission granted", Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize UI components
        libraryWebsiteButton = view.findViewById(R.id.libraryWebsiteButton)
        showLibraryButton = view.findViewById(R.id.showLibraryButton)
        libraryLocationTextView = view.findViewById(R.id.libraryLocationTextView)
        libraryCalendar = view.findViewById(R.id.calendarView)
        openingHoursTextView = view.findViewById(R.id.openingHoursTextView)
        locationHelper = LocationHelper(requireContext())


        // Set up calendar logic
        setupCalendar()

        // Open library website
        libraryWebsiteButton?.setOnClickListener {
            openLibraryWebsite()
        }

        // Show library location
        showLibraryButton.setOnClickListener {
            requestLocationPermission()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        libraryWebsiteButton = null
    }

    // Request location permission
    private fun requestLocationPermission() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    // Fetch and display library location
    private fun fetchAndDisplayLibraryLocation() {
        locationHelper.fetchLastKnownLocation { location ->
            if (location != null) {
                displayLibraryLocation(location.latitude, location.longitude)
                Snackbar.make(
                    requireView(),
                    "Library location displayed successfully.",
                    Snackbar.LENGTH_LONG
                ).show()
                // Notify user if location cannot be fetched
            } else {
                Toast.makeText(requireContext(), "Unable to fetch location", Toast.LENGTH_SHORT)
            }
        }
    }

    // Open library website in the browser
    private fun openLibraryWebsite() {
        val libraryUrl = "https://www.tudublin.ie/library/"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(libraryUrl))
        startActivity(intent)
        Toast.makeText(requireContext(), "Opening library website...", Toast.LENGTH_SHORT).show()
    }

    // Display library location and distance
    private fun displayLibraryLocation(userLat: Double, userLon: Double) {
        val libraryLat = 53.357999
        val libraryLon = -6.287344
        val address =
            LocationUtils.getAddressFromCoordinates(requireContext(), libraryLat, libraryLon)
        val distance = LocationUtils.calculateDistance(userLat, userLon, libraryLat, libraryLon)

        libraryLocationTextView.text = """
            TUD Library Location:
            Address: $address
            Distance: ${"%.2f".format(distance)} meters
        """.trimIndent()
    }

    // Set up calendar to display opening hours
    private fun setupCalendar() {
        updateOpeningHours(libraryCalendar.date)

        libraryCalendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            updateOpeningHours(selectedDate.timeInMillis)

            val formattedDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(selectedDate.time)
            Toast.makeText(requireContext(), "Selected Date: $formattedDate", Toast.LENGTH_SHORT).show()
        }
    }
    // Update opening hours based on selected date
    private fun updateOpeningHours(dateInMillis: Long) {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault()) // Get the day of the week
        val dayOfWeek = sdf.format(Date(dateInMillis))

        val openingHours = when (dayOfWeek.lowercase(Locale.getDefault())) {
            "monday", "tuesday", "wednesday", "thursday", "friday" -> "9:00 AM - 5:00 PM"
            "saturday" -> "10:00 AM - 2:00 PM"
            "sunday" -> "Closed"
            else -> "Closed"
        }

        openingHoursTextView.text = "Opening Hours: $openingHours"
    }
}