package com.example.easy_access_app.ui.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Int,
    val name: String,
    val id: String
)
