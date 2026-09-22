package com.example.easy_access_app.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easy_access_app.ui.data.db.UserRepository
import com.example.easy_access_app.ui.data.entities.User
import kotlinx.coroutines.launch

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {

    val allUsers: LiveData<List<User>> = userRepository.getAllUsers()

    fun insertUser(user: User) {
        viewModelScope.launch {
            userRepository.insert(user)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            userRepository.delete(user)
        }
    }
}
