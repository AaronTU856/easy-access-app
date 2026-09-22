package com.example.easy_access_app.ui

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Insert
    suspend fun insertUserPref(userPref: UserPreferences)

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>

    @Query("SELECT * FROM users WHERE userID = :userId")
    suspend fun getUser(userId: String): User

    @Query("SELECT * FROM user_preferences WHERE userID = :userId")
    suspend fun getUserSettings(userId: String): UserPreferences

    @Query("UPDATE user_preferences SET darkMode = :value WHERE userId = :userId")
    suspend fun updateUserSettings(userId: String, value: Boolean)

    @Query("SELECT darkMode FROM user_preferences WHERE userId = :userId")
    suspend fun getDarkModeById(userId: String): Boolean
}
