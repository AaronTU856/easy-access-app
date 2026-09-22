package com.example.easy_access_app.ui

import android.app.Application
import androidx.room.Room
import com.example.easy_access_app.ui.data.db.AppDatabase
import com.example.easy_access_app.ui.data.db.UserRepository

class MyApplication : Application() {
    lateinit var userRepository: UserRepository

    override fun onCreate() {
        super.onCreate()
        val database = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "app_database"
        ).build()
        userRepository = UserRepository(database.userDao())
    }
}