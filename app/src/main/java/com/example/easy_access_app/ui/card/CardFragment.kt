package com.example.easy_access_app.ui.card

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.easy_access_app.R
import java.io.ByteArrayOutputStream
import android.util.Base64

class CardFragment : Fragment() {

    // UI components
    private lateinit var captureButton: Button
    private lateinit var capturedImageView: ImageView

    // Save the captured bitmap image to SharedPreferences
    private fun saveBitmapToSharedPreferences(bitmap: Bitmap) {
        try {

            val sharedPreferences =
                requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            // Convert the bitmap to a Base64-encoded string
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val encodedImage =
                Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
            // Save the encoded image string
            editor.putString("IDCardImage", encodedImage)
            editor.apply()
            // Notify the user of success
            Toast.makeText(
                requireContext(),
                "Image captured and saved successfully",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Failed to save the image. Please try again.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Crop Bitmap to Focus on the Card
    private fun cropCardFromBitmap(bitmap: Bitmap): Bitmap {
        return try {
            // Example: Cropping the center with a fixed ratio (e.g., 4:3)
            val width = bitmap.width
            val height = bitmap.height
            val left = (width * 0.1).toInt() // Cropping margins
            val top = (height * 0.2).toInt()
            val right = (width * 0.9).toInt()
            val bottom = (height * 0.8).toInt()
            Bitmap.createBitmap(bitmap, left, top, right - left, bottom - top)
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Failed to crop image. Please try again.",
                Toast.LENGTH_SHORT
            ).show()
            bitmap
        }
    }

    // Launcher to capture an image using the camera
        private val captureImageLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
                if (bitmap != null) {
                    val croppedBitmap = cropCardFromBitmap(bitmap) // Apply the crop logic
                    capturedImageView.setImageBitmap(croppedBitmap) // Display the cropped image
                    saveBitmapToSharedPreferences(croppedBitmap) // Save the cropped image
                } else {
                    // Notify the user if no image was captured
                    Toast.makeText(requireContext(), "No image captured", Toast.LENGTH_SHORT).show()
                }
            }

        // Launcher to request camera permission
        private val requestCameraPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    captureImageLauncher.launch(null) // Pass null explicitly
                } else {
                    Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT)
                        .show()
                }
            }


        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.fragment_card, container, false)

            captureButton = view.findViewById(R.id.captureImageButton)
            capturedImageView = view.findViewById(R.id.capturedImageView)

            // Set up the capture button to request permission or launch the camera
            captureButton.setOnClickListener {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }

            return view
        }
    }



