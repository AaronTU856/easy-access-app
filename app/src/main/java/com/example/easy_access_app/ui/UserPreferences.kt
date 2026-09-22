package com.example.easy_access_app.ui

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "user_preferences",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userID"],
            childColumns = ["userID"]
        )
    ]
)
data class UserPreferences (
    @PrimaryKey(autoGenerate = true) val userPrefID: Int = 0,
    val userID: String,
    val darkMode: Boolean = false
)