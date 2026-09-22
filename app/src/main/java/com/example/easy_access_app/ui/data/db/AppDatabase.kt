package com.example.easy_access_app.ui.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.easy_access_app.ui.data.entities.User


@Database(entities = [User::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
