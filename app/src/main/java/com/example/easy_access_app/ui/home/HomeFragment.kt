package com.example.easy_access_app.ui.home

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.activity.result.contract.ActivityResultContracts
import com.example.easy_access_app.R
import com.example.easy_access_app.ui.location.LocationHelper
import com.example.easy_access_app.ui.location.LocationUtils
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineStart
import kotlin.io.encoding.Base64

class HomeFragment : Fragment() {

    private var welcomeTextView: TextView? = null
    private lateinit var studentNameTextView: TextView
    private lateinit var studentIdTextView: TextView
    private lateinit var locationTextView: TextView
    private lateinit var locationHelper: LocationHelper

    // Permission launcher for location access
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            Toast.makeText(requireContext(), "Location permission granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Retrieve data from MainActivity
        val userId = activity?.intent?.getStringExtra("USER_ID") ?: "Unknown ID"
        val userName = activity?.intent?.getStringExtra("USER_NAME") ?: "Unknown Name"

        // Initialize UI components
        welcomeTextView = view.findViewById(R.id.homeWelcomeTextView)

        locationTextView = view.findViewById(R.id.homeLocationTextView)

        studentNameTextView = view.findViewById(R.id.studentNameTextView)
        studentNameTextView.text = userName

        studentIdTextView = view.findViewById(R.id.studentIdTextView)
        studentIdTextView.text = userId

        val showCollegeButton = view.findViewById<Button>(R.id.showCollegeButton)

        // Initialize LocationHelper
        locationHelper = LocationHelper(requireContext())

        // Set button click listener
        showCollegeButton.setOnClickListener {
            fetchUserLocation { userLat, userLon ->
                displayCollegeLocation(userLat, userLon)
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        welcomeTextView?.text = "Welcome to Home"

        // Load and display the captured image
        loadCapturedImage()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        welcomeTextView = null
    }

    // Fetch user's current location
    private fun fetchUserLocation(onLocationFetched: (Double, Double) -> Unit) {
        if (context?.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED ||
            context?.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED) {

            // Use LocationHelper to get the last known location
            locationHelper.fetchLastKnownLocation { location ->
                if (location != null) {
                    Log.d(TAG, "Location: ${location.latitude}, ${location.longitude}")
                    onLocationFetched(location.latitude, location.longitude)
                    Toast.makeText(requireContext(), "User location fetched successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e(TAG, "Last known location is null.")
                    locationTextView.text = "Unable to fetch user location."
                    Toast.makeText(requireContext(), "Unable to fetch location", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(requireContext(), "Location permissions are not granted.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadCapturedImage() {
        val sharedPreferences = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val encodedImage = sharedPreferences.getString("IDCardImage", null)

        if (encodedImage != null) {
            // Decode the Base64-encoded image string into a bitmap
            val byteArray = android.util.Base64.decode(encodedImage, android.util.Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            val idCardImageView = view?.findViewById<ImageView>(R.id.idCardImageView)
            idCardImageView?.setImageBitmap(bitmap)
            Toast.makeText(requireContext(), "ID card image loaded", Toast.LENGTH_SHORT).show()
        }
    }

    // Display the college location and calculate the distance from the user's location
    private fun displayCollegeLocation(userLat: Double, userLon: Double) {
        // Coordinates for Central Quad
        val collegeLat = 53.356580
        val collegeLon = -6.281735
        val address = LocationUtils.getAddressFromCoordinates(requireContext(), collegeLat, collegeLon)
        val distance = LocationUtils.calculateDistance(userLat, userLon, collegeLat, collegeLon)

        // Update the location TextView with details
        locationTextView.text = """
            Central Quad Location:
            Address: $address
            Distance: ${"%.2f".format(distance)} meters
        """.trimIndent()

        Snackbar.make(
            requireView(),
            "Central Quad location displayed successfully.",
            Snackbar.LENGTH_LONG
        ).show()
    }
}
