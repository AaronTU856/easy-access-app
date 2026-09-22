package com.example.easy_access_app.ui

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User (
    @PrimaryKey val userID: String,
    val userName: String?
)