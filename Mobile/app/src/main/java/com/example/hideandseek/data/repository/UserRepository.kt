package com.example.hideandseek.data.repository

import android.content.Context
import androidx.annotation.WorkerThread
import com.example.hideandseek.data.datasource.local.UserDao
import com.example.hideandseek.data.datasource.local.UserData
import com.example.hideandseek.data.datasource.local.UserRoomDatabase
import kotlinx.coroutines.flow.Flow

class UserRepository (private val userDao: UserDao) {

    val allUsers: Flow<List<UserData>> = userDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getLatest(): UserData {
        return userDao.getLatest()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(user: UserData) {
        userDao.insert(user)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll() {
        userDao.deleteAll()
    }
}