package com.example.hideandseek.data.repository

import android.content.Context
import com.example.hideandseek.data.datasource.local.UserDao
import com.example.hideandseek.data.datasource.local.UserData
import com.example.hideandseek.data.datasource.local.UserRoomDatabase
import kotlinx.coroutines.flow.Flow

class UserRepository (private val context: Context) {

    private val userDao: UserDao by lazy { UserRoomDatabase.getInstance(context).userDao() }

    val allUsers: Flow<List<UserData>> = userDao.getAll()

    val nowUser: UserData = userDao.getLatest()

    suspend fun insert(user: UserData) {
        userDao.insert(user)
    }

    suspend fun deleteAll() {
        userDao.deleteAll()
    }
}