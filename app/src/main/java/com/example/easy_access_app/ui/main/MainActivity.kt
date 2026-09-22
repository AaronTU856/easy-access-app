/*Student Name: Aniket Bedade
  Student Number C22448826
  Student Name: Aaron Baggot
  Student Number: C22716399
  Date: 3rd December 2024
  TU856/3
* */

package com.example.easy_access_app.ui.main

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.easy_access_app.R
import com.example.easy_access_app.ui.location.LocationHelper
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.location.Geocoder
import android.view.Menu
import java.util.Locale
import android.view.MenuItem
import com.example.easy_access_app.ui.SettingsActivity
import com.google.android.material.snackbar.Snackbar
import android.view.View
import com.example.easy_access_app.ui.AppDatabase
import com.example.easy_access_app.ui.login.LoginActivity


class MainActivity : AppCompatActivity() {

    private lateinit var _viewPager: ViewPager2
    private lateinit var _bottomNavigationView: BottomNavigationView
    private lateinit var locationHelper: LocationHelper
    private lateinit var locationTextView: TextView
    private lateinit var db: AppDatabase

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            fetchCurrentLocation()
        } else {
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI components
        _viewPager = findViewById(R.id._viewPager)
        _bottomNavigationView = findViewById(R.id.bottomNavigationView)
        locationTextView = findViewById(R.id.locationTextView)

        // Initialize LocationHelper
        locationHelper = LocationHelper(this)

        // Set up navigation components
        setupNavigation()


        // Toolbar for settings dark/light mode
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_request_permission -> {
                // toast message
                Toast.makeText(this, "Requesting location permissions", Toast.LENGTH_SHORT).show()
                // Request location permissions
                requestLocationPermission()

                true
            }
            R.id.action_fetch_location -> {
                val parentLayout = findViewById<View>(android.R.id.content)
                Snackbar.make(parentLayout, "Fetching current location", Snackbar.LENGTH_LONG)
                    .setAction("Retry") {
                        // Fetch current location
                        fetchCurrentLocation()
                    }
                    .show()
                // call function to fetch current location
                fetchCurrentLocation()
                true
            }
            R.id.action_settings -> {
                //toast message
                Toast.makeText(this, "Opening settings", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.action_logout -> {
                Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show()
                performLogout()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun performLogout() {
        // Shutdown Internal Database
        db = AppDatabase.getInstance(this)
        db.close()

        // Redirect to LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Clear activity stack
        startActivity(intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu) // Ensure this references your `main_menu.xml`
        return true
    }
    
    private fun requestLocationPermission() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    // Fetch the last known location using LocationHelper
    private fun fetchCurrentLocation() {
        locationHelper.fetchLastKnownLocation { location ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                val locationName = getAddressFromCoordinates(latitude, longitude)
                updateLocationUI(latitude, longitude, locationName)
            } else {
                Toast.makeText(this, "Unable to fetch location. Ensure GPS is enabled.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Update the TextView with location details
    private fun updateLocationUI(lat: Double, lon: Double, locationName: String) {
        locationTextView.text = "Latitude: $lat, Longitude: $lon\nLocation: $locationName"
    }

    private fun getAddressFromCoordinates(lat: Double, lon: Double): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        // Get address from coordinates using Geocoder
        return try {
            val addresses = geocoder.getFromLocation(lat, lon, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0]
                "${address.locality}, ${address.countryName}"
            } else {
                "Unknown location"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Error retrieving location"
        }
    }

    private fun setupNavigation() {
        val adapter = ViewPagerAdapter(this)
        _viewPager.adapter = adapter
        _viewPager.offscreenPageLimit = 1

        _bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> _viewPager.currentItem = 0
                R.id.card -> _viewPager.currentItem = 1
                R.id.library -> _viewPager.currentItem = 2
                R.id.printer -> _viewPager.currentItem = 3
                else -> false
            }
            true
        }

        _viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // Manage visibility of locationTextView
                locationTextView.visibility = if (position == 0) View.VISIBLE else View.GONE

                when (position) {
                    0 -> _bottomNavigationView.selectedItemId = R.id.home
                    1 -> _bottomNavigationView.selectedItemId = R.id.card
                    2 -> _bottomNavigationView.selectedItemId = R.id.library
                    3 -> _bottomNavigationView.selectedItemId = R.id.printer
                }
            }
        })
    }

}
