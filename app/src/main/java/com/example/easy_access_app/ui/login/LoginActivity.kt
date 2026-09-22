package com.example.easy_access_app.ui.login

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.easy_access_app.R
import com.example.easy_access_app.ui.AppDatabase
import com.example.easy_access_app.ui.SettingsActivity
import com.example.easy_access_app.ui.User
import com.example.easy_access_app.ui.UserPreferences
import com.example.easy_access_app.ui.main.MainActivity
import com.example.easy_access_app.ui.registration.RegistrationActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val idEditText = findViewById<EditText>(R.id.idEditText) // Student ID
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText) // Password
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton = findViewById<Button>(R.id.registerButton) // Register Button

        // Initialise firestore database object
        val fireDb = Firebase.firestore

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "app_database"
        ).build()

        val userDao = db.userDao()

        // Handle Login Button Click
        loginButton.setOnClickListener {
            val userId = idEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            var userName: String? = null

            if (userId.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                // Verify user credentials
                fireDb.collection("users")
                    .whereEqualTo("user_ID", userId)
                    .whereEqualTo("password", password)
                    .get()
                    .addOnSuccessListener { result ->
                        if (!result.isEmpty) {

                            for (user in result) {
                                // Pass Student ID, Name and Password to MainActivity
                                userName = user.data["user_name"].toString()
                                val intent = Intent(this, MainActivity::class.java)

                                intent.putExtra("USER_ID", userId)
                                intent.putExtra("USER_NAME", userName)
                                intent.putExtra("PASSWORD", password)

                                // Pass Student ID to SettingsActivity
                                val intent2 = Intent(this, SettingsActivity::class.java)

                                intent2.putExtra("USER_ID", userId)

                                CoroutineScope(Dispatchers.IO).launch {
                                    val u = userDao.getUser(userId)
                                    // check if user already exists in the internal database
                                    // adds the user and userPref if the user is non-existent
                                    if (u == null) {
                                        userDao.insertUser(User(userID = userId, userName = "$userName"))
                                        userDao.insertUserPref(UserPreferences(userID = userId))
                                    }
                                    val up = userDao.getUserSettings(userId)
                                    Log.d(TAG, "User: $u UserPref: $up")

                                    val users = userDao.getAllUsers()
                                    for (u in users) { Log.d(TAG, "User: $u") }
                                }

                                startActivity(intent)
                                finish() // Close LoginActivity
                            }
                        }
                        else {
                            Toast.makeText(this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d(TAG, "Error getting documents: ", exception)
                    }
            }
        }

        // Handle Register Button Click
        registerButton.setOnClickListener {
            // Navigate to the RegistrationActivity
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }
}
