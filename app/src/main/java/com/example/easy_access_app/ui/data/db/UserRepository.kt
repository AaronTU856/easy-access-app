package com.example.easy_access_app.ui.data.db

import androidx.lifecycle.LiveData
import com.example.easy_access_app.ui.data.entities.User

class UserRepository(private val userDao: UserDao) {

    // Function to get all users from the database
    fun getAllUsers(): LiveData<List<User>> {
        return userDao.getAllUsers()
    }

    // Function to insert a user into the database
    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    // Function to delete a specific user
    suspend fun delete(user: User) {
        userDao.delete(user)
    }


}
