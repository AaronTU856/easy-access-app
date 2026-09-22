package com.example.easy_access_app.ui.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.easy_access_app.ui.data.entities.User

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User) {
        println("User inserted into database: ${user.name}")
    }
    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM user_table ORDER BY userId ASC")
    fun getAllUsers(): LiveData<List<User>>

}
