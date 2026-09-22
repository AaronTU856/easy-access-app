package com.example.easy_access_app.ui

import android.content.ContentValues.TAG
import android.content.Intent // Missing import
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button // Import for the button
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.easy_access_app.R
import com.example.easy_access_app.ui.main.MainActivity // Ensure MainActivity is correctly imported
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {

    private lateinit var darkModeSwitch: Switch
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var backToHomeButton: Button // Declare the Back to Home button
    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        db = AppDatabase.getInstance(this)
        userDao = db.userDao()
        val userId = intent?.getStringExtra("USER_ID") ?: "Unknown ID"

        // Initialize Dark Mode Switch
        darkModeSwitch = findViewById(R.id.darkModeSwitch)
        sharedPreferences = getSharedPreferences("user_preferences", MODE_PRIVATE)
        CoroutineScope(Dispatchers.IO).launch {
            val isDarkMode = userDao.getDarkModeById(userId)
            Log.d(TAG, "Dark Mode: $isDarkMode")
        }

        darkModeSwitch.isChecked = sharedPreferences.getBoolean("DARK_MODE", false)
        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("DARK_MODE", isChecked).apply()
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                CoroutineScope(Dispatchers.IO).launch {
                    userDao.updateUserSettings(userId, true)
                }
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                CoroutineScope(Dispatchers.IO).launch {
                    userDao.updateUserSettings(userId, false)
                }
            }
        }

        // Initialize Back to Home Button
        backToHomeButton = findViewById(R.id.backToHomeButton)
        backToHomeButton.setOnClickListener {
            finish() // Return to Main Activity
        }
    }

}
