package com.example.easy_access_app.ui.registration

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.easy_access_app.R
import com.example.easy_access_app.ui.login.LoginActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class RegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val studentIdEditText = findViewById<EditText>(R.id.studentIdEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val roleRadioGroup = findViewById<RadioGroup>(R.id.roleRadioGroup)
        val registerButton = findViewById<Button>(R.id.registerSubmitButton)

        // Initialise firestore database object
        val fireDb = Firebase.firestore

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val userId = studentIdEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val selectedRoleId = roleRadioGroup.checkedRadioButtonId
            val role = if (selectedRoleId == R.id.studentRadioButton) {
                "Student"
            } else if (selectedRoleId == R.id.staffRadioButton) {
                "Staff"
            } else {
                null
            }

            if (name.isEmpty() || userId.isEmpty() || password.isEmpty() || role == null) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else if (userId.length != 9) {
                Toast.makeText(this, "Invalid Student ID", Toast.LENGTH_SHORT).show()
            } else {

                fireDb.collection("users")
                    .whereEqualTo("user_ID", userId)
                    .get()
                    .addOnSuccessListener { result ->
                        if (result.isEmpty) {
                            // Insert User into firebase database
                            val user = hashMapOf(
                                "user_ID" to userId,
                                "user_name" to name,
                                "password" to password,
                                "role" to role
                            )

                            fireDb.collection("users")
                                .add(user)
                                .addOnSuccessListener { documentReference ->
                                    Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                                    // Show a success message
                                    Toast.makeText(this, "Registered as $role successfully!", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { e ->
                                    Log.w(TAG, "Error adding document", e)
                                    // Show a failure message
                                    Toast.makeText(this, "Registration unsuccessful", Toast.LENGTH_SHORT).show()
                                }


                        } else {
                            Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show()
                        }
                    }


                // Redirect to LoginActivity
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish() // Close the RegistrationActivity
            }
        }
    }
}
